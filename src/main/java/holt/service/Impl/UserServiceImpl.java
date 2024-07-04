package holt.service.Impl;

import holt.model.User;
import holt.repository.UserRepository;
import holt.service.UserService;
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
public class UserServiceImpl implements UserService {
    public static final int USERNAME_MIN = 4;
    public static final int PASSWORD_MIN = 8;
    private static final int ENCRYPT_KEY_LENGTH = 16;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder(ENCRYPT_KEY_LENGTH);
    }



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
