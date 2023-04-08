package cri.free.FitnessInstructor.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "options")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    public Option(String text) {
        this.text=text;
    }
    @Override
    public String toString() {
        return "Option{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", questionId=" + (question != null ? question.getId() : "null") +
                '}';
    }

}