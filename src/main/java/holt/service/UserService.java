package holt.service;

import holt.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author Weiyang Wu
 * @date 2024/7/4 12:10
 */
public interface UserService {

    long userRegister(String username, String password, String confirmPassword);
    User userLogin(String username, String password, HttpServletRequest request);
    User searchUser(Long id);
    List<User> searchUsers();
    boolean deleteUser(Long id);

}
