package holt.controller;

import holt.common.BaseResponse;
import holt.common.ErrorCode;
import holt.common.ResultUtil;
import holt.exception.BusinessException;
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
    public BaseResponse<Long> register(@RequestBody UserRegisterRequest registerRequest) {
        if (registerRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "No register request");
        }
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();
        String confirmPassword = registerRequest.getConfirmPassword();
        if (StringUtils.isAnyBlank(username, password, confirmPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "Null inputs");
        }
        Long result = userService.userRegister(username, password, confirmPassword);
        return ResultUtil.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> login(@RequestBody UserLoginRequest loginRequest, HttpServletResponse response) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        if (StringUtils.isAnyBlank(username, password)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "Null login request");
        }
        User user = userService.userLogin(username, password, response);
        return ResultUtil.success(user);
    }

    @GetMapping("/search/{id}")
    public BaseResponse<User> searchUser(@PathVariable Long id, HttpServletRequest request) {
        if (id == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "Null id");
        }
        User user = userService.searchUser(id);
        return ResultUtil.success(user);
    }

    @GetMapping("/searchAll")
    public BaseResponse<List<User>> searchUsers(HttpServletRequest request) {
        userService.checkAdmin(request);
        List<User> users = userService.searchUsers();
        return ResultUtil.success(users);
    }

    @DeleteMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        userService.checkAdmin(request);

        // Admin cannot delete his/her own account
        Long userId = Long.valueOf(request.getAttribute("id").toString());
        if (userId.equals(id)) {
            throw new BusinessException(ErrorCode.NOT_ALLOWED, "Cannot delete self");
        }
        Boolean result = userService.deleteUser(id);
        return ResultUtil.success(result);
    }
}
