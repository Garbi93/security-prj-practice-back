package com.example.securityprjpracticeback.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    @PostConstruct
    public void init() {
        File tempFolder = new File(uploadPath);
        // 템프 폴더가 없을때 폴더 생성
        if (!tempFolder.exists()) {
            tempFolder.mkdirs();
        }

        uploadPath = tempFolder.getAbsolutePath();
        log.info("-------------------------");
        log.info(uploadPath);
    }

    public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException {

        if (files == null || files.size() == 0) {
            return null;
        }
        
        List<String> uploadNames = new ArrayList<>();

        for (MultipartFile file : files) {
            String savedName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            Path savePath = Paths.get(uploadPath, savedName);

            try {
                Files.copy(file.getInputStream(), savePath); // 원본 파일 업로드

                // 파일이 이미지 파일일때만 썸네일 이미지로 만들어 주기
                String contentType = file.getContentType(); // Mim type 이라고 나온다.

                // 컨텐트 타입이 안 비어 있고 파일형식이 image 로 시작 한다면
                if (contentType != null || contentType.startsWith("image")) {
                    Path thumbnailPath = Paths.get(uploadPath, "s_" + savedName);

                    Thumbnails.of(savePath.toFile()).size(200, 200).toFile(thumbnailPath.toFile());

                }



                uploadNames.add(savedName);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }// end for
        
        return uploadNames;
        
    }
}
