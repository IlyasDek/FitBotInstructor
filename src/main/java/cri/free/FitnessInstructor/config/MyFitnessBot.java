package cri.free.FitnessInstructor.config;

import cri.free.FitnessInstructor.config.BotConfig;
import cri.free.FitnessInstructor.services.UserProfileService;
import cri.free.FitnessInstructor.services.QuizService;
import cri.free.FitnessInstructor.telegram.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MyFitnessBot extends TelegramLongPollingBot {
    private MessageHandler messageHandler;
    private final BotConfig botConfig;


    @Autowired
    public MyFitnessBot(BotConfig botConfig, QuizService quizService, UserProfileService userProfileService) {
        this.botConfig = botConfig;
        this.messageHandler = new MessageHandler(this, userProfileService, quizService);
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        long chatId;
        String messageText = "";

        if (update.hasMessage() && update.getMessage().hasText()) {
            messageText = update.getMessage().getText();
            chatId = update.getMessage().getChatId();
        } else if (update.hasCallbackQuery()) {
            messageText = update.getCallbackQuery().getData();
            chatId = update.getCallbackQuery().getMessage().getChatId();
        } else {
            return;
        }
        messageHandler.handleMessage(chatId, messageText);
    }
}