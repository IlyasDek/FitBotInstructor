package cri.free.FitnessInstructor.repo;


import cri.free.FitnessInstructor.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("SELECT q FROM Question q LEFT JOIN FETCH q.options")
    List<Question> findAllWithOptions();
}