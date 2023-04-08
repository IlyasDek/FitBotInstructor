package cri.free.FitnessInstructor.services;

import cri.free.FitnessInstructor.models.Question;
import cri.free.FitnessInstructor.models.UserQuizState;
import cri.free.FitnessInstructor.repo.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuizService {
    private List<Question> questions;
    private Map<Long, UserQuizState> userQuizStateMap;

    @Autowired
    public QuizService(QuestionRepository questionRepository) {
        questions = questionRepository.findAllWithOptions();
        userQuizStateMap = new HashMap<>();
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public UserQuizState getUserQuizState(long chatId) {
        return userQuizStateMap.get(chatId);
    }

    public void addUserQuizState(long chatId, UserQuizState userQuizState) {
        userQuizStateMap.put(chatId, userQuizState);
    }

    public void removeUserQuizState(long chatId) {
        userQuizStateMap.remove(chatId);
    }
}
