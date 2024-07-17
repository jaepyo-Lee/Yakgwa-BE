package com.prography.yakgwa.domain.common.impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwsS3Util {
    private final AmazonS3Client s3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * Todo
     * Work) 테스트 코드
     * Write-Date) 2024-07-17
     * Finish-Date)
     */
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {

        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new RuntimeException("이미지 업로드가 안되었습니다."));

        return upload(uploadFile, dirName);
    }

    private String upload(File uploadFile, String dirName){

        String fileName = dirName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);

        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName){

        s3.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
        );

        return s3.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile){

        String name = targetFile.getName();

        if (targetFile.delete()){
            log.info(name + "파일 삭제 완료");
        } else {
            log.info(name + "파일 삭제 실패");
        }
    }

    private Optional<File> convert(MultipartFile multipartFile) throws IOException{

        File convertFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        if (convertFile.createNewFile()){

            try (FileOutputStream fos = new FileOutputStream(convertFile)) {

                fos.write(multipartFile.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }
}
