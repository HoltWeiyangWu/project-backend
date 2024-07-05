package holt.service;

import holt.model.User;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Weiyang Wu
 * @date 2024/7/4 12:10
 */
public interface UserService {

    long userRegister(String username, String password, String confirmPassword);
    User userLogin(String username, String password, HttpServletRequest request);
}
