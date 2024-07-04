package holt;
import java.util.Date;

import holt.repository.UserRepository;
import holt.model.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Weiyang Wu
 * @date 2024/7/4 10:33
 *
 */
@SpringBootTest
class UserRepositoryTest {
    @Resource
    private UserRepository userRepository;
    @Test
    public void testAddUsers() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setName("Holt");
        user.setAvatar("");
        user.setProfile("");
        user.setRole("admin");
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setEmail("");
        userRepository.save(user);
        System.out.println("User added successfully");
    }
}