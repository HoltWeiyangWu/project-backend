package holt.service;

import holt.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * @author Weiyang Wu
 * @date 2024/7/4 12:10
 */
public interface UserService {

    long userRegister(String username, String password, String confirmPassword);
    User userLogin(String username, String password, HttpServletResponse response);
    User searchUser(Long id);
    List<User> searchUsers();
    boolean deleteUser(Long id);
    void checkAdmin(HttpServletRequest request);

}
