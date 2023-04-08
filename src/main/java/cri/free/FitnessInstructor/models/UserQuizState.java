package cri.free.FitnessInstructor.models;

import java.util.ArrayList;
import java.util.List;

public class UserQuizState  {
    private int questionIndex;
    private List<String> answers;

    public UserQuizState () {
        questionIndex = 0;
        answers = new ArrayList<>();
    }

    public int getQuestionIndex() {
        return questionIndex;
    }

    public void setQuestionIndex(int questionIndex) {
        this.questionIndex = questionIndex;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }


    public void incrementQuestionIndex() {
        questionIndex++;
    }
}
