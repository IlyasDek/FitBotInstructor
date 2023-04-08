//package cri.free.FitnessInstructor.models;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//
//import java.util.List;
//
//@Entity
//@Table(name = "questions")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Question {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String text;
//
//    @ElementCollection
//    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
//    @Column(name = "option", nullable = false)
//    private List<String> options;
//}

package cri.free.FitnessInstructor.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Nullable
    private List<Option> options;

    public Question(String text, List<Option> options) {
        this.text = text;
        this.options = options;
    }
    @Override
    public String toString() {
        String optionsStr = options.stream()
                .map(option -> option.getId() + ": " + option.getText())
                .collect(Collectors.joining(", "));

        return "Question{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", options=[" + optionsStr + "]" +
                '}';
    }

}