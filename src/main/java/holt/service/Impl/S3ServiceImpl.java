package holt.service.Impl;

import holt.service.S3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

/**
 * @author Weiyang Wu
 * @date 2024/11/23 9:34
 */
@Service
public class S3ServiceImpl implements S3Service {
    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    private String region = "ap-southeast-2";

    private final S3Client s3Client;

    public S3ServiceImpl(@Value("${aws.s3.access-key-id}") String accessKeyId,
                          @Value("${aws.s3.secret-access-key}") String secretAccessKey) {
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(() -> AwsBasicCredentials.create(accessKeyId, secretAccessKey))
                .build();
    }

    /**
     * Uploads a file to AWS S3 bucket's specific folder
     * @param file File that is to be uploaded to AWS S3
     * @param folderName The specified folder name in AWS S3 bucket
     * @return URL that access to the file
     */
    public String uploadFile(MultipartFile file, String folderName, Long userId){
        String fileName = folderName + userId;
        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .acl(ObjectCannedACL.PUBLIC_READ)
                            .build(),
                    RequestBody.fromBytes(file.getBytes())
            );
            // TODO: Change to self-defined exception handler
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);
    }
}
