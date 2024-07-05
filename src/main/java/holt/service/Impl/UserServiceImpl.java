package holt.service.Impl;

import holt.model.User;
import holt.repository.UserRepository;
import holt.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Weiyang Wu
 * @date 2024/7/4 12:12
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    public static final int USERNAME_MIN = 4;
    public static final int PASSWORD_MIN = 8;
    private static final String USER_LOGIN_STATE = "user_login_state";

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * User login
     * @param username user's username, should not include special characters
     * @param password user's password
     * @return
     */
    public User userLogin(String username, String password, HttpServletRequest request) {
        // Check input
        if (username.length() < USERNAME_MIN) {
            return null;
        }
        if (password.length() < PASSWORD_MIN) {
            return null;
        }

        // No special characters in username
        String validPattern = "^[a-zA-Z0-9]{"+ USERNAME_MIN +",}$";
        Matcher matcher = Pattern.compile(validPattern).matcher(username);
        if (!matcher.find()) {
            return null;
        }

        // Compare passwords
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.info("user is not found with the given password");
            return null;
        }

        String encryptedPassword = user.getPassword();
        if (!bCryptPasswordEncoder.matches(password, encryptedPassword)) {
            log.info("incorrect password");
            return null;
        }
        // Record session attribute
        request.getSession().setAttribute(USER_LOGIN_STATE, user);

        // Remove sensitive user information
        User safeUser = new User();
        safeUser.setUsername(username);
        safeUser.setProfile(user.getProfile());
        safeUser.setName(user.getName());
        safeUser.setAvatar(user.getAvatar());
        safeUser.setEmail(user.getEmail());
        return safeUser;
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
            return -1;
        }
        if (password.length() < PASSWORD_MIN) {
            return -1;
        }
        if (!confirmPassword.equals(password) ) {
            return -1;
        }

        // No special characters in username
        String validPattern = "^[a-zA-Z0-9]{"+ USERNAME_MIN +",}$";
        Matcher matcher = Pattern.compile(validPattern).matcher(username);
        if (!matcher.find()) {
            return -1;
        }

        // No repeated username
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return -1;
        }

        // Encryption
        String encryptedPassword = bCryptPasswordEncoder.encode(password);


        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(encryptedPassword);
        userRepository.save(newUser);
        return 1;
    }
}
