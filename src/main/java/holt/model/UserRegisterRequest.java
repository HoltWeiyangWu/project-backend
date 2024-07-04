package holt.model;

import lombok.Data;

/**
 * @author Weiyang Wu
 * @date 2024/7/4 14:44
 */
@Data
public class UserRegisterRequest {

    private String username;

    private String password;

    private String confirmPassword;
}
