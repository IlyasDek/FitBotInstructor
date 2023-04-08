package cri.free.FitnessInstructor.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;
    @Column(nullable = false)
    private long chatId;
    private String gender;
    private int age;
    private double height;
    private double weight;
    private String fitnessLevel;
    private String fitnessGoal;
    private int daysPerWeek;
    private String limitations;
    private boolean hasSubscription;

}