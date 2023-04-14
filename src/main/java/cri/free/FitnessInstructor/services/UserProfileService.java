package cri.free.FitnessInstructor.services;

import cri.free.FitnessInstructor.dto.UserDTO;
import cri.free.FitnessInstructor.dto.UserDetailsDTO;
import cri.free.FitnessInstructor.models.User;
import cri.free.FitnessInstructor.models.UserDetails;

import java.util.List;

public interface UserProfileService {
//    UserProfile saveUserProfile(UserProfile userProfile);
//    UserProfileDTO findUserProfileById(Long id);
//    List<UserProfileDTO> findAllUserProfiles();
//    void deleteUserProfileById(Long id);
//    boolean isUserSubscribed(Long chatId);
//    boolean subscribeUser(Long chatId);
//    List<String> getUserContextFromDatabase(long chatId);
//    public UserProfile convertToEntity(UserProfileDTO userProfileDTO);
//    public UserProfileDTO convertToDTO(UserProfile userProfile);
// User methods
User saveUser(User user);
    UserDTO findUserByChatId(Long chatId);

    // UserDetails methods
    UserDetails saveUserDetails(UserDetails userDetails);
    UserDetailsDTO findUserDetailsByUserId(Long userId);
    List<UserDetailsDTO> findAllUserDetails();
    void deleteUserDetailsById(Long id);

    // Subscription methods
    boolean isUserSubscribed(Long chatId);
    boolean subscribeUser(Long chatId);
    public User convertToEntity(UserDTO userDTO);
    public UserDTO convertToDTO(User user);
    public UserDetails convertToEntity(UserDetailsDTO userDetailsDTO);
    public UserDetailsDTO convertToDTO(UserDetails userDetails);
}