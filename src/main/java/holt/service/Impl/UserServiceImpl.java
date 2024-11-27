package holt.service.Impl;

import holt.common.ErrorCode;
import holt.constant.S3FolderName;
import holt.exception.BusinessException;
import holt.model.User;
import holt.model.request.UpdateSettingRequest;
import holt.repository.UserRepository;
import holt.service.S3Service;
import holt.service.UserService;
import holt.uitl.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static holt.constant.UserConstant.ADMIN_ROLE;

/**
 * @author Weiyang Wu
 * @date 2024/7/4 12:12
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    public static final int USERNAME_MIN = 4;
    public static final int PASSWORD_MIN = 8;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final S3Service s3Service;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, S3Service s3Service) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.s3Service = s3Service;
    }

    /**
     * User login
     * @param username user's username, should not include special characters
     * @param password user's password
     * @return User without sensitive information
     */
    public User userLogin(String username, String password, HttpServletResponse response) {
        // Check input
        if (username == null || password == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "Empty username or password");
        }
        if (username.length() < USERNAME_MIN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Username too short");
        }
        if (password.length() < PASSWORD_MIN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Password too short");
        }

        // No special characters in username
        String validPattern = "^[a-zA-Z0-9]{"+ USERNAME_MIN +",}$";
        Matcher matcher = Pattern.compile(validPattern).matcher(username);
        if (!matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Invalid username format");
        }

        // Compare passwords
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "Username not found");
        }

        String encryptedPassword = user.getPassword();
        if (!bCryptPasswordEncoder.matches(password, encryptedPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Wrong password");
        }

        // Return a JWT to the client
        String jwt = JwtUtil.generateToken(user.getUsername(), user.getId());
        response.setHeader("token", jwt);

        // Remove sensitive user information
        return getSafeUser(user);
    }

    /**
     * Search a user information according to its id
     * @param id user id
     * @return User without sensitive information
     */
    @Override
    public User searchUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional
                .map(this::getSafeUser)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR, "User not found"));
    }

    /**
     * Display a list of users
     * @return a list of users without sensitive information
     */
    @Override
    public List<User> searchUsers() {
        List<User> users= (List<User>) userRepository.findAll();
        users.forEach(user->{user.setPassword(null);});
        return users;
    }

    /**
     * Delete a user from the database according to the user id
     * @param id user id
     * @return true if the deletion is successful, or else return false
     */
    @Override
    public boolean deleteUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
            return true;
        } else {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "User not found");
        }
    }


    /**
     * User registers an account
     * @param username user's username, should not include special characters
     * @param password user's password
     * @param confirmPassword requires users to type in the password again to confirm correctness
     * @return 1 to indicate success, -1 to indicate failure
     */
    @Override
    public long userRegister(String username, String password, String confirmPassword) {
        // Check input
        if (username.length() < USERNAME_MIN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Username too short");
        }
        if (password.length() < PASSWORD_MIN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Password too short");
        }
        if (!confirmPassword.equals(password) ) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Confirm password not match");
        }

        // No special characters in username
        String validPattern = "^[a-zA-Z0-9]{"+ USERNAME_MIN +",}$";
        Matcher matcher = Pattern.compile(validPattern).matcher(username);
        if (!matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Invalid username format");
        }

        // No repeated username
        User user = userRepository.findByUsername(username);
        if (user != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Username is already in use");
        }

        // Encryption
        String encryptedPassword = bCryptPasswordEncoder.encode(password);


        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(encryptedPassword);
        newUser.setName("Anonymous User");
        userRepository.save(newUser);
        return 1;
    }

    /**
     * Updates user setting, stores avatar to AWS S3
     * @param settingRequest request that contains user information that should be updated
     * @param user The user who requests for updating setting
     * @return True if update is successful
     */
    public User updateSetting(UpdateSettingRequest settingRequest, MultipartFile avatar, User user) {
        String name = settingRequest.getName();
        String email = settingRequest.getEmail();
        String profile = settingRequest.getProfile();
        Long userId = user.getId();

        if (avatar != null) {
            String avatarURL = s3Service.uploadFile(avatar, S3FolderName.AVATAR, userId);
            user.setAvatar(avatarURL);
        }

        user.setName(name);
        user.setEmail(email);
        user.setProfile(profile);
        userRepository.save(user);
        return getSafeUser(user);
    }

    /**
     * Strip all the sensitive information from the given user object
     * @param user User object retrieved from the database
     * @return User object without sensitive information
     */
    private User getSafeUser(User user) {
        User safeUser = new User();
        safeUser.setUsername(user.getUsername());
        safeUser.setProfile(user.getProfile());
        safeUser.setName(user.getName());
        safeUser.setAvatar(user.getAvatar());
        safeUser.setEmail(user.getEmail());
        return safeUser;
    }

    /**
     * Check if the current user exists and if the user is admin
     * @param request Http request intercepted from the Login Interceptor
     */
    public void checkAdmin(HttpServletRequest request) {
        User claimedUser = retrieveUser(request);

        boolean isAdmin = claimedUser.getRole().equals(ADMIN_ROLE);
        if (!isAdmin) {
            throw new BusinessException(ErrorCode.NO_AUTH, "JWT contains non-admin user");
        }
    }

    /**
     * Check if the current user exists
     * @param request Http request intercepted from the Login Interceptor
     */
    public void checkUser(HttpServletRequest request) {
        retrieveUser(request);
    }

    /**
     * Validate if the claimed user exists in the database
     * @param request Http request intercepted from the Login Interceptor
     * @return a user object if the user exists
     */
    public User retrieveUser(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        Long id = Long.valueOf(request.getAttribute("id").toString());
        User claimedUser = userRepository.findById(id).orElseThrow(Error::new);
        boolean correctUsername = claimedUser.getUsername().equals(username);
        if (!correctUsername) {
            throw new BusinessException(ErrorCode.NO_AUTH, "JWT contains non-existed user");
        }
        return claimedUser;
    }
}
