package holt.controller;

import holt.model.User;
import holt.model.request.UserLoginRequest;
import holt.model.request.UserRegisterRequest;
import holt.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Weiyang Wu
 * @date 2024/7/4 12:15
 */
@RestController
@CrossOrigin(origins = "http://127.0.0.1:3030/", allowCredentials = "true", exposedHeaders = "token")
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Long register(@RequestBody UserRegisterRequest registerRequest) {

        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();
        String confirmPassword = registerRequest.getConfirmPassword();
        if (StringUtils.isAnyBlank(username, password, confirmPassword)) {
            return null;
        }
        long result = userService.userRegister(username, password, confirmPassword);
        return result;
    }

    @PostMapping("/login")
    public User login(@RequestBody UserLoginRequest loginRequest, HttpServletResponse response) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        if (StringUtils.isAnyBlank(username, password)) {
            return null;
        }
        return userService.userLogin(username, password, response);
    }

    @GetMapping("/search/{id}")
    public User searchUser(@PathVariable Long id, HttpServletRequest request) {
        if (id == null) {
            System.out.println("Null username, error");
            return null;
        }
        return userService.searchUser(id);
    }

    @GetMapping("/searchAll")
    public List<User> searchUsers(HttpServletRequest request) {
        userService.checkAdmin(request);
        return userService.searchUsers();
    }

    @DeleteMapping("/delete/{id}")
    public boolean deleteUser(@PathVariable Long id, HttpServletRequest request) {
        userService.checkAdmin(request);

        // Admin cannot delete its own account
        Long userId = Long.valueOf(request.getAttribute("id").toString());
        if (userId.equals(id)) {
            return false;
        }
        return userService.deleteUser(id);
    }
}
