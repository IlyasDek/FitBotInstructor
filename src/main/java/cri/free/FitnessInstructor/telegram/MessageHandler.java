package cri.free.FitnessInstructor.telegram;

import cri.free.FitnessInstructor.config.MyFitnessBot;
import cri.free.FitnessInstructor.models.UserQuizState;
import cri.free.FitnessInstructor.quiz.QuizHandler;
import cri.free.FitnessInstructor.services.ChatGPTService;
import cri.free.FitnessInstructor.services.QuizService;
import cri.free.FitnessInstructor.services.UserProfileService;
import cri.free.FitnessInstructor.utils.JsonParserUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class MessageHandler implements MessageSender {
    private MyFitnessBot bot;
    private UserProfileService userProfileService;
    private QuizService quizService;
    private KeyboardBuilder keyboard;
    private QuizHandler quizHandler;
    JsonParserUtil jsonParserUtil = new JsonParserUtil();
    ChatGPTService chatGPTService = new ChatGPTService(jsonParserUtil);

    private static final String START_COMMAND = "/start";
    private static final String START_QUIZ_COMMAND = "/startQuiz";
    private static final String ERROR_SENDING_MESSAGE = "Error sending message: ";
    private static final String GREETINGS = "Добро пожаловать в Fitbot";

    public MessageHandler(MyFitnessBot bot, UserProfileService userProfileService, QuizService quizService) {
        this.bot = bot;
        this.userProfileService = userProfileService;
        this.quizService = quizService;
        this.keyboard = new KeyboardBuilder();

        this.quizHandler = new QuizHandler(this, userProfileService, quizService, jsonParserUtil, chatGPTService);
    }

    public void handleMessage(long chatId, String messageText) {
        if (messageText.equals(START_COMMAND)) {
            handleStartCommand(chatId);
        } else if (messageText.equals(START_QUIZ_COMMAND)) {
            quizHandler.handleStartQuizCommand(chatId);
        } else {
            quizHandler.handleAnswer(chatId, messageText);
        }
    }

    private void handleStartCommand(long chatId) {
        if (userProfileService.isUserSubscribed(chatId)) {
            handleSubscribedUser(chatId);
        } else {
            handleUnsubscribedUser(chatId);
        }
    }

    @Override
    public void sendMessage(Long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            System.out.println(ERROR_SENDING_MESSAGE + e.getMessage());
        }
    }

    @Override
    public void sendMessage(Long chatId, String textToSend, InlineKeyboardMarkup replyMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        message.setReplyMarkup(replyMarkup);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            System.out.println(ERROR_SENDING_MESSAGE + e.getMessage());
        }
    }

    @Override
    public void sendMessage(Long chatId, SendMessage message) {
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            System.out.println(ERROR_SENDING_MESSAGE + e.getMessage());
        }
    }

    public void handleSubscribedUser(long chatId) {

    }

    public void handleUnsubscribedUser(long chatId) {
        UserQuizState userQuizState = new UserQuizState();
        userQuizState.setQuestionIndex(0);
        quizService.addUserQuizState(chatId, userQuizState);

        sendMessage(chatId, GREETINGS);
        quizHandler.sendStartButton(chatId);
    }

    public void setQuizHandler(QuizHandler quizHandler) {
        this.quizHandler = quizHandler;
    }
}