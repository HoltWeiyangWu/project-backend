package holt.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Weiyang Wu
 * @date 2024/11/23 9:35
 */
public interface S3Service {
    String uploadFile(MultipartFile file, String folderName, Long userId);
}
