package cri.free.FitnessInstructor.config;


import cri.free.FitnessInstructor.models.Option;
import cri.free.FitnessInstructor.models.Question;
import cri.free.FitnessInstructor.repo.QuestionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DatabaseInitializer {

    private final QuestionRepository questionRepository;

    @Autowired
    public DatabaseInitializer(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @PostConstruct
    public void init() {
        // Проверка, пуста ли таблица вопросов
        if (questionRepository.count() == 0) {
            // Вопросы
            List<Question> questions = new ArrayList<>();
            questions.add(new Question("Введите ваш пол.", Arrays.asList(new Option("Мужской"), new Option("Женский"))));
            questions.add(new Question("Введите ваш возраст.", null));
            questions.add(new Question("Введите ваш рост (см) и вес (кг).", null));
            questions.add(new Question("Какой у вас уровень фитнеса?", Arrays.asList(new Option("Начинающий"), new Option("Средний"), new Option("Продвинутый"))));
            questions.add(new Question("Какие ваши основные фитнес-цели?", Arrays.asList(new Option("Похудение"), new Option("Набор мышечной массы"), new Option("Улучшение общей физической формы"), new Option("Другое"))));
            questions.add(new Question("Сколько дней в неделю вы готовы тренироваться?", null));
            questions.add(new Question("Есть ли у вас ограничения по здоровью " +
                    "или предпочтения в тренировках, о которых я должен знать?", null));

            // Добавление в базу данных
            for (Question question : questions) {
                List<Option> options = question.getOptions();
                if (options != null) {
                    for (Option option : options) {
                        option.setQuestion(question);
                    }
                }
                questionRepository.save(question);
            }
        }
    }
}