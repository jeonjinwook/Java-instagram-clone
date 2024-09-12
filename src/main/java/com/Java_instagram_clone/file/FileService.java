package com.Java_instagram_clone.file;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@Service
public class FileService {

    private static final String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/uploadFile/";

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

        String uploadFileName = UUID.randomUUID() + fileExtension;

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

    public void removeFile(String[] files) throws IOException {

        Arrays.stream(files).forEach(file -> {

            Path filePath = Paths.get(uploadDir).resolve(file);

            if (Files.exists(filePath)) {
                try {
                    Files.deleteIfExists(filePath);
                } catch (IOException e) {
                }

            }

        });
    }
}
