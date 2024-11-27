package holt.model.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Weiyang Wu
 * @date 2024/11/22 15:57
 */
@Data
public class UpdateSettingRequest {
    private String name;

    private String email;

    private String profile;
}
