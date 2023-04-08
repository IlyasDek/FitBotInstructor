package cri.free.FitnessInstructor.repo;


import cri.free.FitnessInstructor.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByChatId(Long chatId);
}