package com.Java_instagram_clone.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class FileService {

    private static final String uploadDir = System.getProperty("user.dir") + "/src/main/resources/uploadFile/";

    public ArrayList<String> uploadFile(MultipartFile[] uploadFiles) throws IOException {

        ArrayList<String> createFiles = new ArrayList<>();

        try {
            // 다중 파일 저장
            for (MultipartFile file : uploadFiles) {
                createFiles.add(saveFile(file));
            }
        } catch (IOException e) {
            throw e;
        }
        // 파일 추출 테스트
        return createFiles;
    }

    private String saveFile(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();

        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        String uploadFileName = UUID.randomUUID().toString() + fileExtension;

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(uploadFileName);
        try {
            file.transferTo(filePath.toFile());
        } catch (IOException e) {
            throw e;
        }

        return uploadFileName;

    }

}
