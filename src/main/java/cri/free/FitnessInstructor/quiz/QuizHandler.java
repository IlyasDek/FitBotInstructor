package cri.free.FitnessInstructor.quiz;

import cri.free.FitnessInstructor.dto.UserDetailsDTO;
import cri.free.FitnessInstructor.models.UserDetails;
import cri.free.FitnessInstructor.telegram.KeyboardBuilder;
import cri.free.FitnessInstructor.telegram.MessageSender;
import cri.free.FitnessInstructor.models.Question;
import cri.free.FitnessInstructor.models.UserQuizState;
import cri.free.FitnessInstructor.services.ChatGPTService;
import cri.free.FitnessInstructor.services.QuizService;
import cri.free.FitnessInstructor.services.UserProfileService;
import cri.free.FitnessInstructor.utils.JsonParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class QuizHandler {
    private MessageSender messageSender;
    private UserProfileService userProfileService;
    private QuizService quizService;
    private final JsonParserUtil jsonParserUtil;
    private final ChatGPTService chatGPTService;
    private KeyboardBuilder keyboard;
    private static final Logger logger = LoggerFactory.getLogger(QuizHandler.class);

    @Autowired
    public QuizHandler(MessageSender messageSender, UserProfileService userProfileService, QuizService quizService,
                       JsonParserUtil jsonParserUtil, ChatGPTService chatGPTService) {
        this.messageSender = messageSender;
        this.userProfileService = userProfileService;
        this.quizService = quizService;
        this.jsonParserUtil = jsonParserUtil;
        this.chatGPTService = chatGPTService;
        this.keyboard = new KeyboardBuilder(userProfileService);
    }

    public void handleStartQuizCommand(long chatId) {
        UserQuizState userQuizState = new UserQuizState();
        userQuizState.setQuestionIndex(0);
        quizService.addUserQuizState(chatId, userQuizState);
        sendQuestion(chatId, quizService.getQuestions().get(0));
    }

    public void handleAnswer(long chatId, String messageText) {
        UserQuizState userQuizState = quizService.getUserQuizState(chatId);
        if (userQuizState == null || userQuizState.getQuestionIndex() >= quizService.getQuestions().size()) {
            System.out.println("UserQuizState is null or questions are finished.");
            return;
        }
        if (messageText.trim().isEmpty()) {
            messageSender.sendMessage(chatId, "Пожалуйста, введите ответ.");
            return;
        }
        processUserAnswer(chatId, messageText, userQuizState);
    }

    private void processUserAnswer(long chatId, String messageText, UserQuizState userQuizState) {
        Question currentQuestion = quizService.getQuestions().get(userQuizState.getQuestionIndex());
        userQuizState.getAnswers().add(messageText);

        if (userQuizState.getQuestionIndex() == quizService.getQuestions().size() - 1) {
            handleLastQuestion(chatId, userQuizState);
        }
        userQuizState.incrementQuestionIndex();

        if (userQuizState.getQuestionIndex() < quizService.getQuestions().size()) {
            sendQuestion(chatId, quizService.getQuestions().get(userQuizState.getQuestionIndex()));
        } else {
            completeQuizAndSaveUserData(chatId);
        }
    }

    private void handleLastQuestion(long chatId, UserQuizState userQuizState) {
        String chatGPTResponse = chatGPTService.getResponseFromChatGPTAPI(userQuizState.getAnswers());

        logger.info("User Answers: {}", userQuizState.getAnswers());

        List<String> answers = userQuizState.getAnswers();
        String heightAndWeight = answers.get(5);
        String[] heightAndWeightArr = heightAndWeight.split(" ");

        if (userProfileService.isUserSubscribed(chatId)) {
            LocalDateTime currentDate = LocalDateTime.now();
            logger.info("Current Date: {}", currentDate);
            logger.info("GPT Response: {}", chatGPTResponse);
            UserDetailsDTO userDetailsDTO = UserDetailsDTO.builder()
                    .userId(userProfileService.findUserByChatId(chatId).getId())
                    .gender(answers.get(0))
                    .fitnessLevel(answers.get(1))
                    .fitnessGoal(answers.get(2))
                    .age(Integer.parseInt(answers.get(3).replaceAll("[^\\d]", "")))
                    .daysPerWeek(Integer.parseInt(answers.get(4).replaceAll("[^\\d]", "")))
                    .height(Double.parseDouble(heightAndWeightArr[0].replaceAll("[^\\d]", "")))
                    .weight(Double.parseDouble(heightAndWeightArr[1].replaceAll("[^\\d]", "")))
                    .limitations(answers.get(6))
                    .questionnaireDate(currentDate)
                    .chatgptResponse(chatGPTResponse)
                    .build();
            UserDetails userDetails = userProfileService.convertToEntity(userDetailsDTO);
            userProfileService.saveUserDetails(userDetails);
        }
        String personalizedPlanMessage = "Ваш персонализированный план: " + chatGPTResponse;
        messageSender.sendMessage(chatId, personalizedPlanMessage);
    }

    private void completeQuizAndSaveUserData(long chatId) {
        quizService.removeUserQuizState(chatId);
        sendStartButton(chatId);
    }

    public void sendQuestion(long chatId, Question question) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(question.getText());

        InlineKeyboardMarkup markup = keyboard.getKeyboardForQuestion(question);
        if (markup != null) {
            sendMessage.setReplyMarkup(markup);
        }

        messageSender.sendMessage(chatId, sendMessage);
    }

    public void sendStartButton(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Нажмите кнопку ниже, чтобы начать опросник.");
        sendMessage.setReplyMarkup(keyboard.createStartButton(chatId));
        messageSender.sendMessage(chatId, sendMessage);
    }
}