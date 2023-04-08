package cri.free.FitnessInstructor.telegram;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Service
public interface MessageSender {
    void sendMessage(Long chatId, String textToSend);
    void sendMessage(Long chatId, String textToSend, InlineKeyboardMarkup replyMarkup);
    void sendMessage(Long chatId, SendMessage message);
}