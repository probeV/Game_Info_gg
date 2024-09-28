package probeV.GameInfogg.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3Client amazonS3Client;

    // 이미지 저장
    public String createImageFile(MultipartFile multipartFile, String directoryPath) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String filePath = directoryPath + "/" + originalFilename; // 경로와 파일명을 결합

        // 파일 중복 시 오류
        if (amazonS3Client.doesObjectExist(bucketName, filePath)) {
            throw new IOException("이미 존재하는 파일입니다.");
        }

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3Client.putObject(bucketName, filePath, multipartFile.getInputStream(), metadata);
        return amazonS3Client.getUrl(bucketName, filePath).toString();
    }

    // 이미지 수정
    public String updateFile(MultipartFile multipartFile, String preFileUrl, String directoryPath) throws IOException {
        //preImageUrl을 filePath로 변환
        String preFilePath = preFileUrl.substring(preFileUrl.indexOf("/", preFileUrl.indexOf(".com/"))+1);
        log.info("preFilePath: {}", preFilePath);

        // 파일 없을 때
        if (!amazonS3Client.doesObjectExist(bucketName, preFilePath)) {
            throw new IOException("이전 파일이 존재하지 않습니다.");
        }

        // 이전 파일 삭제
        amazonS3Client.deleteObject(bucketName, preFilePath);

        // 새로운 파일 생성
        return createImageFile(multipartFile, directoryPath);
    }

    // 이미지 삭제
    public void deleteFile(String fileUrl) throws IOException {

        // url을 filePath로 변환
        String filePath = fileUrl.substring(fileUrl.indexOf("/", fileUrl.indexOf(".com/"))+1);
        log.info("filePath: {}", filePath);

        // 파일 존재 하지 않을 때 오류
        if (!amazonS3Client.doesObjectExist(bucketName, filePath)) {
            throw new IOException("파일이 존재하지 않습니다.");
        }

        amazonS3Client.deleteObject(bucketName, filePath);
    }

}
