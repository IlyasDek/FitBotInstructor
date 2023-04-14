package cri.free.FitnessInstructor.models;

import lombok.*;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_details")
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String gender;
    private int age;
    private Double height;
    private Double weight;
    private String fitnessLevel;
    private String fitnessGoal;
    private int daysPerWeek;
    private String limitations;
    @Column(columnDefinition = "TEXT")
    private String chatgptResponse;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime questionnaireDate;

}
