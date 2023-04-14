package cri.free.FitnessInstructor.models;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long chatId;

    private boolean hasSubscription;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserDetails> userDetails;

}
