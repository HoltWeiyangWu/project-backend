package holt.service;

import holt.model.User;
import holt.model.request.UpdateSettingRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

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
    void checkUser(HttpServletRequest request);
    User retrieveUser(HttpServletRequest request);
    User updateSetting(UpdateSettingRequest settingRequest, MultipartFile avatar, User user);
}
