package cri.free.FitnessInstructor.services.impl;


import cri.free.FitnessInstructor.dto.UserProfileDTO;
import cri.free.FitnessInstructor.models.UserProfile;
import cri.free.FitnessInstructor.repo.UserProfileRepository;
import cri.free.FitnessInstructor.services.UserProfileService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Override
    public UserProfileDTO saveUserProfile(UserProfileDTO userProfileDto) {
        UserProfile userProfile = new UserProfile();
        BeanUtils.copyProperties(userProfileDto, userProfile);
        userProfile = userProfileRepository.save(userProfile);
        BeanUtils.copyProperties(userProfile, userProfileDto);
        return userProfileDto;
    }

    @Override
    public UserProfileDTO findUserProfileById(Long id) {
        UserProfile userProfile = userProfileRepository.findById(id).orElse(null);
        if (userProfile != null) {
            UserProfileDTO userProfileDto = new UserProfileDTO();
            BeanUtils.copyProperties(userProfile, userProfileDto);
            return userProfileDto;
        }
        return null;
    }

    @Override
    public List<UserProfileDTO> findAllUserProfiles() {
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        List<UserProfileDTO> userProfileDTOs = new ArrayList<>();
        for (UserProfile userProfile : userProfiles) {
            UserProfileDTO userProfileDto = new UserProfileDTO();
            BeanUtils.copyProperties(userProfile, userProfileDto);
            userProfileDTOs.add(userProfileDto);
        }
        return userProfileDTOs;
    }

    @Override
    public void deleteUserProfileById(Long id) {
        userProfileRepository.deleteById(id);
    }
    @Override
    public boolean isUserSubscribed(Long chatId) {
        try {

            Optional<UserProfile> userProfileOptional = userProfileRepository.findByChatId(chatId);
            return userProfileOptional.map(UserProfile::isHasSubscription).orElse(false);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> getUserContextFromDatabase(long chatId) {
        if (isUserSubscribed(chatId)) {
            // В данном примере возвращается пустой список
            return new ArrayList<>();
        } else {
            // Возвращаем стандартный контекст или пустой контекст для неподписанных пользователей
            return getDefaultOrEmptyUserContext();
        }
    }

    private List<String> getDefaultOrEmptyUserContext() {
        return new ArrayList<>();
    }

    public UserProfileDTO convertToDTO(UserProfile userProfile) {
        return UserProfileDTO.builder()
                .id(userProfile.getId())
                .chatId(userProfile.getChatId())
                .gender(userProfile.getGender())
                .age(userProfile.getAge())
                .height(userProfile.getHeight())
                .weight(userProfile.getWeight())
                .fitnessLevel(userProfile.getFitnessLevel())
                .fitnessGoal(userProfile.getFitnessGoal())
                .daysPerWeek(userProfile.getDaysPerWeek())
                .limitations(userProfile.getLimitations())
                .hasSubscription(userProfile.isHasSubscription())
                .build();
    }

    // Convert UserProfileDTO to UserProfile
    public UserProfile convertToEntity(UserProfileDTO userProfileDTO) {
        return UserProfile.builder()
                .id(userProfileDTO.getId())
                .chatId(userProfileDTO.getChatId())
                .gender(userProfileDTO.getGender())
                .age(userProfileDTO.getAge())
                .height(userProfileDTO.getHeight())
                .weight(userProfileDTO.getWeight())
                .fitnessLevel(userProfileDTO.getFitnessLevel())
                .fitnessGoal(userProfileDTO.getFitnessGoal())
                .daysPerWeek(userProfileDTO.getDaysPerWeek())
                .limitations(userProfileDTO.getLimitations())
                .hasSubscription(userProfileDTO.isHasSubscription())
                .build();
    }
}
