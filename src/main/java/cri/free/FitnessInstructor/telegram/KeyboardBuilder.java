package cri.free.FitnessInstructor.telegram;

import cri.free.FitnessInstructor.models.Option;
import cri.free.FitnessInstructor.models.Question;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KeyboardBuilder {
    public InlineKeyboardMarkup createStartButton() {
        InlineKeyboardButton startButton = new InlineKeyboardButton();
        startButton.setText("Начать");
        startButton.setCallbackData("/startQuiz");

        InlineKeyboardButton subscribeButton = new InlineKeyboardButton();
        subscribeButton.setText("Купить подписку");
        subscribeButton.setCallbackData("subscribe");

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(startButton);
        row.add(subscribeButton);

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(row);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);
        return markup;
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
