package holt;

import holt.uitl.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Weiyang Wu
 * @date 2024/7/19 14:59
 */
@SpringBootTest
public class JwtServiceTest {


    @Autowired
    private JwtUtil jwtUtil;


    @Test
    void testJwtUtil() {
        Long id = 10L;
        String username = "User";
        String jwt = jwtUtil.generateToken(username, id);
    }
}
