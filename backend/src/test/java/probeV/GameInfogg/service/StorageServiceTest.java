package probeV.GameInfogg.service;

import com.amazonaws.services.s3.AmazonS3Client;
import io.findify.s3mock.S3Mock;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import probeV.GameInfogg.config.AwsS3MockConfig;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Import(AwsS3MockConfig.class)
class StorageServiceTest {
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Autowired
    private S3Mock s3Mock;
    @Autowired
    private AmazonS3Client amazonS3Client;
    @Autowired
    private StorageService storageService;

    @AfterEach
    public void tearDown() {
        s3Mock.stop();
    }

    @Test
    @Transactional
    void createFile_생성_성공() throws IOException {
        // given
        String path = "test";
        String originalFilename = "createFile_create_success.txt";

        MultipartFile multipartFile = new MockMultipartFile(originalFilename, originalFilename, "text/plain", "test content".getBytes());

        // when
        String result = storageService.createFile(multipartFile, path);

        //then
        assertTrue(result.contains(path));
    }

    @Test
    @Transactional
    void createFile_중복_실패() throws IOException {
        // given
        String path = "test";
        String originalFilename = "createFile_duplicate_fail.txt";

        MultipartFile multipartFile = new MockMultipartFile(originalFilename, originalFilename, "text/plain", "test content".getBytes());

        // when
        storageService.createFile(multipartFile, path);

        // then
        assertThrows(IOException.class, () -> storageService.createFile(multipartFile, path));
    }

    @Test
    @Transactional
    void updateFile_수정_성공() throws IOException {
        // given
        String path = "test";
        String originalFilename = "updateFile_update_success.txt";

        MultipartFile multipartFile = new MockMultipartFile(originalFilename, originalFilename, "text/plain", "test content".getBytes());

        String preImageUrl = storageService.createFile(multipartFile, path);

        //when
        String result = storageService.updateFile(multipartFile, preImageUrl, path);

        //then
        assertTrue(result.contains(path));
    }

    @Test
    @Transactional
    void updateFile_존재하지않음_실패() throws IOException {
        // given
        String path = "test";
        String originalFilename = "updateFile_not_exist_fail.txt";

        MultipartFile multipartFile = new MockMultipartFile(originalFilename, originalFilename, "text/plain", "test content".getBytes());

        String preImageUrl = "test/updateFile_not_exist_fail.txt";

        // when
        assertThrows(IOException.class, () -> storageService.updateFile(multipartFile, preImageUrl, path));
    }

    @Test
    @Transactional
    void deleteFile_삭제_성공() throws IOException {
        // given
        String path = "test";
        String originalFilename = "deleteFile_delete_success.txt";
        MultipartFile multipartFile = new MockMultipartFile(originalFilename, originalFilename, "text/plain", "test content".getBytes());
        
        String url = storageService.createFile(multipartFile, path);

        // when
        storageService.deleteFile(url);

        // then
    }

    @Test
    @Transactional
    void deleteFile_존재하지않음_실패() throws IOException {
        // given
        String path = "test";
        String originalFilename = "deleteFile_not_exist_fail.txt";

        String url = path + "/" + originalFilename;

        // when

        // then
        assertThrows(IOException.class, () -> storageService.deleteFile(path));
    }
}