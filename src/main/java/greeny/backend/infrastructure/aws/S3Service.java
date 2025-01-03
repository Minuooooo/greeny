package greeny.backend.infrastructure.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import greeny.backend.exception.situation.common.EmptyFileException;
import greeny.backend.exception.situation.common.FileUploadFailureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile multipartFile) {
        validateFileExists(multipartFile);
        String fileName = createFileName(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            objectMetadata.setContentLength(bytes.length);
            s3Client.putObject(new PutObjectRequest(
                    bucketName,
                    fileName,
                    new ByteArrayInputStream(bytes),
                    objectMetadata
            ).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (Exception e) {
            throw new FileUploadFailureException(e.getMessage());
        }

        return fileName;
    }

    public void deleteFile(String fileName) {
        s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    }

    private void validateFileExists(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new EmptyFileException();
        }
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) {
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if(!StringUtils.hasText(fileExtension)) {
            throw new StringIndexOutOfBoundsException();
        }
        return fileExtension;
    }
}