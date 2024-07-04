package holt.service;

/**
 * @author Weiyang Wu
 * @date 2024/7/4 12:10
 */
public interface UserService {

    long userRegister(String username, String password, String confirmPassword);
}
