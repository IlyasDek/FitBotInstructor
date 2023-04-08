package cri.free.FitnessInstructor.quiz;

import cri.free.FitnessInstructor.telegram.KeyboardBuilder;
import cri.free.FitnessInstructor.telegram.MessageSender;
import cri.free.FitnessInstructor.models.Question;
import cri.free.FitnessInstructor.models.UserQuizState;
import cri.free.FitnessInstructor.services.ChatGPTService;
import cri.free.FitnessInstructor.services.QuizService;
import cri.free.FitnessInstructor.services.UserProfileService;
import cri.free.FitnessInstructor.utils.JsonParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class QuizHandler {
    private MessageSender messageSender;
    private UserProfileService userProfileService;
    private QuizService quizService;
    private final JsonParserUtil jsonParserUtil;
    private final ChatGPTService chatGPTService;
    private KeyboardBuilder keyboard;
    private InlineKeyboardMarkup startButtonMarkup;

    @Autowired
    public QuizHandler(MessageSender messageSender, UserProfileService userProfileService, QuizService quizService,
                       JsonParserUtil jsonParserUtil, ChatGPTService chatGPTService) {
        this.messageSender = messageSender;
        this.userProfileService = userProfileService;
        this.quizService = quizService;
        this.jsonParserUtil = jsonParserUtil;
        this.chatGPTService = chatGPTService;
        this.keyboard = new KeyboardBuilder();
        this.startButtonMarkup = keyboard.createStartButton();
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
        sendMessage.setText("Нажмите кнопку ниже, чтобы начать заново.");
        sendMessage.setReplyMarkup(startButtonMarkup);
        messageSender.sendMessage(chatId, sendMessage);
    }
}