package holt.controller;

import holt.constant.UserConstant;
import holt.model.User;
import holt.model.request.UserLoginRequest;
import holt.model.request.UserRegisterRequest;
import holt.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static holt.constant.UserConstant.ADMIN_ROLE;


/**
 * @author Weiyang Wu
 * @date 2024/7/4 12:15
 */
@RestController
@CrossOrigin(origins = "http://127.0.0.1:3030", allowCredentials = "true")
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
    public User login(@RequestBody UserLoginRequest loginRequest, HttpServletRequest request) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        if (StringUtils.isAnyBlank(username, password)) {
            return null;
        }
        return userService.userLogin(username, password, request);
    }

    @GetMapping("/search/{id}")
    public User searchUser(@PathVariable Long id, HttpServletRequest request) {
        checkLogin(request);
        if (id == null) {
            System.out.println("Null username, error");
            return null;
        }
        return userService.searchUser(id);
    }

    @GetMapping("/searchAll")
    public List<User> searchUsers(HttpServletRequest request) {
        checkLogin(request);
        checkAdmin(request);
        return userService.searchUsers();
    }

    @DeleteMapping("/delete/{id}")
    public boolean deleteUser(@PathVariable Long id, HttpServletRequest request) {
        checkLogin(request);
        checkAdmin(request);
        return userService.deleteUser(id);
    }

    private void checkAdmin(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        boolean isAdmin = user.getRole().equals(ADMIN_ROLE);
        if (!isAdmin) {
            throw new Error("The user does not have enough privileges to perform this operation");
        }
    }

    private void checkLogin(HttpServletRequest request) {
        if (request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE) == null) {
            throw new Error("The session does not contain any user");
        }
    }
}
