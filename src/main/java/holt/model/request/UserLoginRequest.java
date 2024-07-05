package holt.model.request;

import lombok.Data;

/**
 * @author Weiyang Wu
 * @date 2024/7/5 10:29
 */
@Data
public class UserLoginRequest {

    private String username;

    private String password;
}
