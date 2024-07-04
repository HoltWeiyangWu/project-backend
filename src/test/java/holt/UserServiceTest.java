package holt;

import holt.model.User;
import holt.repository.UserRepository;
import holt.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import static org.mockito.Mockito.when;

/**
 * @author Weiyang Wu
 * @date 2024/7/4 15:15
 */
@SpringBootTest
public class UserServiceTest {


    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;


    @Test
    void testRegisterUser() {
        String username = "usernameTest";
        String password = "passwordTest";
        String confirmPassword = "passwordTest";
        long result = userService.userRegister(username, password, confirmPassword);

        Assertions.assertEquals(1, result);

        // Repeated account
        when(userRepository.findByUsername(username)).thenReturn(new User());
        result = userService.userRegister(username, password, confirmPassword);
        Assertions.assertEquals(-1, result);

        // Incorrect confirmPassword
        confirmPassword = "passwordTest2";
        result = userService.userRegister(username, password, confirmPassword);
        Assertions.assertEquals(-1, result);

        // Not enough length
        password = "7length";
        result = userService.userRegister(username, password, confirmPassword);
        Assertions.assertEquals(-1, result);

        // Null username
        password = "passwordTest";
        confirmPassword = "passwordTest";
        username = "";
        result = userService.userRegister(username, password, confirmPassword);
        Assertions.assertEquals(-1, result);

        // Username with special characters
        username = "username?";
        result = userService.userRegister(username, password, confirmPassword);
        Assertions.assertEquals(-1, result);

    }
}
