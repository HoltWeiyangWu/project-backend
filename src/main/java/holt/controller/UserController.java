package holt.controller;

import holt.model.User;
import holt.model.UserRegisterRequest;
import holt.service.UserService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Weiyang Wu
 * @date 2024/7/4 12:15
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Long register(UserRegisterRequest registerRequest) {

        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();
        String confirmPassword = registerRequest.getConfirmPassword();
        if (StringUtils.isAnyBlank(username, password, confirmPassword)) {
            return null;
        }
        long result = userService.userRegister(username, password, confirmPassword);
        return result;
    }

}
