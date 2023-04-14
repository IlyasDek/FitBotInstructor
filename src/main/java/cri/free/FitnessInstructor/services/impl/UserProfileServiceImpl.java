package cri.free.FitnessInstructor.services.impl;


import cri.free.FitnessInstructor.dto.UserDTO;
import cri.free.FitnessInstructor.dto.UserDetailsDTO;
import cri.free.FitnessInstructor.models.User;
import cri.free.FitnessInstructor.models.UserDetails;
import cri.free.FitnessInstructor.quiz.QuizHandler;
import cri.free.FitnessInstructor.repo.UserDetailsRepository;
import cri.free.FitnessInstructor.repo.UserRepository;
import cri.free.FitnessInstructor.services.UserProfileService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
    public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private static final Logger logger = LoggerFactory.getLogger(QuizHandler.class);

    // User methods
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserDTO findUserByChatId(Long chatId) {
        User user = userRepository.findByChatId(chatId).orElse(null);
        if (user != null) {
            return convertToDTO(user);
        }
        return null;
    }

    // UserDetails methods
    @Override
    public UserDetails saveUserDetails(UserDetails userDetails) {
//        return userDetailsRepository.save(userDetails);
        logger.info("Saving user details: {}", userDetails.getChatgptResponse());
        logger.info("Saved user details: {}", userDetails.getQuestionnaireDate());
        UserDetails savedUserDetails = userDetailsRepository.save(userDetails);
        logger.info("Saved user details: {}", savedUserDetails.getChatgptResponse());
        logger.info("Saved user details: {}", savedUserDetails.getQuestionnaireDate());
        return savedUserDetails;
    }

    @Override
    public UserDetailsDTO findUserDetailsByUserId(Long userId) {
        UserDetails userDetails = userDetailsRepository.findByUserId(userId).orElse(null);
        if (userDetails != null) {
            return convertToDTO(userDetails);
        }
        return null;
    }

    @Override
    public List<UserDetailsDTO> findAllUserDetails() {
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        return userDetailsList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUserDetailsById(Long id) {
        userDetailsRepository.deleteById(id);
    }

    // Subscription methods
    @Override
    public boolean isUserSubscribed(Long chatId) {
        try {
            Optional<User> userOptional = userRepository.findByChatId(chatId);
            return userOptional.map(User::isHasSubscription).orElse(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

   @Override
    public boolean subscribeUser(Long chatId) {
        Optional<User> userOptional = userRepository.findByChatId(chatId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (!user.isHasSubscription()) {
                user.setHasSubscription(true);
                userRepository.save(user);
            }

            return true;
        } else {
            User newUser = new User();
            newUser.setChatId(chatId);
            newUser.setHasSubscription(true);

            userRepository.save(newUser);

            return true;
        }
    }
    public User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setChatId(userDTO.getChatId());
        user.setHasSubscription(userDTO.isHasSubscription());
        return user;
    }

    public UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setChatId(user.getChatId());
        userDTO.setHasSubscription(user.isHasSubscription());
        return userDTO;
    }

    public UserDetails convertToEntity(UserDetailsDTO userDetailsDTO) {
        UserDetails userDetails = new UserDetails();
        Long userId = userDetailsDTO.getUserId();
        if (userId != null) {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                userDetails.setUser(user);
            }
        }
        userDetails.setGender(userDetailsDTO.getGender());
        userDetails.setFitnessLevel(userDetailsDTO.getFitnessLevel());
        userDetails.setFitnessGoal(userDetailsDTO.getFitnessGoal());
        userDetails.setAge(userDetailsDTO.getAge());
        userDetails.setDaysPerWeek(userDetailsDTO.getDaysPerWeek());
        userDetails.setHeight(userDetailsDTO.getHeight());
        userDetails.setWeight(userDetailsDTO.getWeight());
        userDetails.setLimitations(userDetailsDTO.getLimitations());
        userDetails.setChatgptResponse(userDetailsDTO.getChatgptResponse());
        userDetails.setQuestionnaireDate(userDetailsDTO.getQuestionnaireDate());
        return userDetails;
    }

    public UserDetailsDTO convertToDTO(UserDetails userDetails) {
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
        User user = userDetails.getUser();
        if (user != null) {
            userDetailsDTO.setUserId(user.getId());
        }
        userDetailsDTO.setGender(userDetails.getGender());
        userDetailsDTO.setFitnessLevel(userDetails.getFitnessLevel());
        userDetailsDTO.setFitnessGoal(userDetails.getFitnessGoal());
        userDetailsDTO.setAge(userDetails.getAge());
        userDetailsDTO.setDaysPerWeek(userDetails.getDaysPerWeek());
        userDetailsDTO.setHeight(userDetails.getHeight());
        userDetailsDTO.setWeight(userDetails.getWeight());
        userDetailsDTO.setLimitations(userDetails.getLimitations());
        userDetailsDTO.setChatgptResponse(userDetails.getChatgptResponse());
        userDetailsDTO.setQuestionnaireDate(userDetails.getQuestionnaireDate());
        return userDetailsDTO;
    }

}