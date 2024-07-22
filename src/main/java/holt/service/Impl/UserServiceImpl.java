package holt.service.Impl;

import holt.model.User;
import holt.repository.UserRepository;
import holt.service.UserService;
import holt.uitl.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

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

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * User login
     * @param username user's username, should not include special characters
     * @param password user's password
     * @return User without sensitive information
     */
    public User userLogin(String username, String password, HttpServletResponse response) {
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
        return userOptional.map(this::getSafeUser).orElse(null);
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
            return false;
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
        newUser.setName("Anonymous User");
        userRepository.save(newUser);
        return 1;
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
        String username = (String) request.getAttribute("username");
        Long id = Long.valueOf(request.getAttribute("id").toString());
        User claimedUser = userRepository.findById(id).orElseThrow(Error::new);
        boolean correctUsername = claimedUser.getUsername().equals(username);
        if (!correctUsername) {
            throw new Error("JWT contains non-existed user");
        }

        boolean isAdmin = claimedUser.getRole().equals(ADMIN_ROLE);
        if (!isAdmin) {
            throw new Error("User is not an admin");
        }
    }
}
