package cri.free.FitnessInstructor.telegram;

import cri.free.FitnessInstructor.config.GPTConfig;
import cri.free.FitnessInstructor.config.MyFitnessBot;
import cri.free.FitnessInstructor.models.UserQuizState;
import cri.free.FitnessInstructor.quiz.QuizHandler;
import cri.free.FitnessInstructor.services.ChatGPTService;
import cri.free.FitnessInstructor.services.QuizService;
import cri.free.FitnessInstructor.services.UserProfileService;
import cri.free.FitnessInstructor.utils.JsonParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
    ChatGPTService chatGPTService;

    private static final String START_COMMAND = "/start";
    private static final String START_QUIZ_COMMAND = "/startQuiz";
    private static final String SUBSCRIBE_COMMAND = "/subscribe";
    private static final String ERROR_SENDING_MESSAGE = "Error sending message: ";
    private static final String GREETINGS = "Добро пожаловать в Fitbot";

@Autowired
public MessageHandler(MyFitnessBot bot, UserProfileService userProfileService, QuizService quizService, ChatGPTService chatGPTService) {
    this.bot = bot;
    this.userProfileService = userProfileService;
    this.quizService = quizService;
    this.keyboard = new KeyboardBuilder(userProfileService);

    this.chatGPTService = chatGPTService;
    this.quizHandler = new QuizHandler(this, userProfileService, quizService, jsonParserUtil, chatGPTService);
}

public void handleMessage(long chatId, String messageText) {
    if (messageText.equals(START_COMMAND)) {
        handleUser(chatId);
    } else if (messageText.equals(START_QUIZ_COMMAND)) {
        quizHandler.handleStartQuizCommand(chatId);
    } else if (messageText.equals(SUBSCRIBE_COMMAND)) {
        handleSubscribeCommand(chatId);
    } else {
        UserQuizState userQuizState = quizService.getUserQuizState(chatId);
        if (userQuizState != null) {
            quizHandler.handleAnswer(chatId, messageText);
        } else {
            System.out.println("command: " + messageText);
            sendMessage(chatId, "Неизвестная команда. Пожалуйста, используйте /start или /subscribe для начала работы с ботом.");
        }
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

    public void handleUser(long chatId) {
        UserQuizState userQuizState = new UserQuizState();
        userQuizState.setQuestionIndex(0);
        quizService.addUserQuizState(chatId, userQuizState);

        sendMessage(chatId, GREETINGS);
        quizHandler.sendStartButton(chatId);
    }

    public void handleSubscribedUser(long chatId) {
        UserQuizState userQuizState = new UserQuizState();
        userQuizState.setQuestionIndex(0);
        quizService.addUserQuizState(chatId, userQuizState);

        sendMessage(chatId, GREETINGS);
        quizHandler.sendStartButton(chatId);
    }

    public void handleUnsubscribedUser(long chatId) {
        UserQuizState userQuizState = new UserQuizState();
        userQuizState.setQuestionIndex(0);
        quizService.addUserQuizState(chatId, userQuizState);

        sendMessage(chatId, GREETINGS);
        quizHandler.sendStartButton(chatId);
    }

    private void handleSubscribeCommand(long chatId) {
        boolean isSubscribed = userProfileService.isUserSubscribed(chatId);
        if (isSubscribed) {
            sendMessage(chatId, "Вы уже подписаны.");
        } else {
            boolean subscriptionResult = userProfileService.subscribeUser(chatId);
            if (subscriptionResult) {
                sendMessage(chatId, "Подождите, идет обработка вашей подписки...");


                sendMessage(chatId, "Поздравляем! Вы успешно подписались.");
                handleUser(chatId);
            } else {
                sendMessage(chatId, "К сожалению, произошла ошибка при регистрации вашей подписки. Пожалуйста, попробуйте еще раз.");
            }
        }
    }

    public void setQuizHandler(QuizHandler quizHandler) {
        this.quizHandler = quizHandler;
    }
}