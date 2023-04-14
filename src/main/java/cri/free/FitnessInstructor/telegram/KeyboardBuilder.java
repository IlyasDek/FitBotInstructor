package cri.free.FitnessInstructor.telegram;

import cri.free.FitnessInstructor.models.Option;
import cri.free.FitnessInstructor.models.Question;
import cri.free.FitnessInstructor.services.UserProfileService;
import cri.free.FitnessInstructor.services.impl.UserProfileServiceImpl;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class KeyboardBuilder {

    private final UserProfileService userProfileService;

    public KeyboardBuilder(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }
    public InlineKeyboardMarkup createStartButton(long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton startButton = new InlineKeyboardButton();
        startButton.setText("Начать");
        startButton.setCallbackData("/startQuiz");

        row1.add(startButton);
        rows.add(row1);

        if (!userProfileService.isUserSubscribed(chatId)) {
            List<InlineKeyboardButton> row2 = new ArrayList<>();
            InlineKeyboardButton subscribeButton = new InlineKeyboardButton();
            subscribeButton.setText("Купить подписку");
            subscribeButton.setCallbackData("/subscribe");
            row2.add(subscribeButton);
            rows.add(row2);
        }

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getKeyboardForQuestion(Question question) {
        if (question.getOptions() != null) {
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            for (Option option : question.getOptions()) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(option.getText());
                button.setCallbackData(option.getText());
                keyboard.add(Collections.singletonList(button));
            }
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            markup.setKeyboard(keyboard);
            return markup;
        }
        return null;
    }

    public InlineKeyboardMarkup createOptionsKeyboard(List<Option> options) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (Option option : options) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(option.getText());
            button.setCallbackData(option.getText());
            keyboard.add(Collections.singletonList(button));
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);
        return markup;
    }
}
