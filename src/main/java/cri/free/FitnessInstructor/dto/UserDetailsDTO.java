package cri.free.FitnessInstructor.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailsDTO {
    private Long id;
    @NotNull
    private Long userId;
    @NotBlank
    private String gender;
    @Min(1)
    @Max(120)
    @Positive
    private int age;
    @Positive
    private Double height;
    @Positive
    private Double weight;
    @NotBlank
    private String fitnessLevel;
    @NotBlank
    private String fitnessGoal;
    @Min(1)
    @Max(7)
    private int daysPerWeek;
    private String limitations;
    private String chatgptResponse;
    private LocalDateTime questionnaireDate;
}
