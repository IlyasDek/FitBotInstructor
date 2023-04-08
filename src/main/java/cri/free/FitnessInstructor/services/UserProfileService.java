package cri.free.FitnessInstructor.services;

import cri.free.FitnessInstructor.dto.UserProfileDTO;

import java.util.List;

public interface UserProfileService {
    UserProfileDTO saveUserProfile(UserProfileDTO userProfileDto);
    UserProfileDTO findUserProfileById(Long id);
    List<UserProfileDTO> findAllUserProfiles();
    void deleteUserProfileById(Long id);
    boolean isUserSubscribed(Long chatId);
    List<String> getUserContextFromDatabase(long chatId);
}