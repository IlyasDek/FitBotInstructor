package cri.free.FitnessInstructor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDTO {
    private Long id;
    @NotNull
    private long chatId;
    @NotBlank
    private String gender;
    @Min(1)
    @Max(120)
    private int age;
    @Positive
    private double height;
    @Positive
    private double weight;
    @NotBlank
    private String fitnessLevel;
    @NotBlank
    private String fitnessGoal;
    @Min(1)
    @Max(7)
    private int daysPerWeek;
    private String limitations;
    private boolean hasSubscription;
}