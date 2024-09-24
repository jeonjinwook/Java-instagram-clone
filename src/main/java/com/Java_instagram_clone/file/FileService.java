package com.Java_instagram_clone.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class FileService {

  private final Path uploadDir;

  public FileService(@Value("${upload.dir}") String uploadDir) {
    if (uploadDir == null || uploadDir.isEmpty()) {
      throw new IllegalArgumentException("upload.dir 프로퍼티가 설정되지 않았습니다.");
    }
    this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
    try {
      Files.createDirectories(this.uploadDir);
    } catch (IOException e) {
      throw new RuntimeException("Could not create upload directory!", e);
    }
  }

  public List<String> uploadFiles(MultipartFile[] uploadFiles) throws IOException {
    List<String> uploadedFileNames = new ArrayList<>();

    for (MultipartFile file : uploadFiles) {
      String fileName = saveFile(file);
      uploadedFileNames.add(fileName);
    }

    return uploadedFileNames;
  }

  private String saveFile(MultipartFile file) throws IOException {
    String originalFileName = StringUtils.cleanPath(
        Objects.requireNonNull(file.getOriginalFilename()));

    if (originalFileName.isEmpty()) {
      throw new IOException("Invalid file name");
    }

    String fileExtension = "";

    int dotIndex = originalFileName.lastIndexOf('.');
    if (dotIndex >= 0) {
      fileExtension = originalFileName.substring(dotIndex);
    }

    String newFileName = UUID.randomUUID() + fileExtension;

    Path targetLocation = this.uploadDir.resolve(newFileName).normalize();

    if (!targetLocation.startsWith(this.uploadDir)) {
      throw new IOException("현재 디렉토리 외부에 파일을 저장할 수 없습니다.");
    }

    // 파일 저장
    file.transferTo(targetLocation.toFile());

    return newFileName;
  }

  public void removeFiles(String[] fileNames) {
    for (String fileName : fileNames) {
      try {
        Path filePath = this.uploadDir.resolve(fileName).normalize();

        if (!filePath.startsWith(this.uploadDir)) {
          throw new IOException("정상적인 경로가 아닙니다.");
        }

        if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
          Files.delete(filePath);
        } else {
          log.warn("해당 파일을 찾을 수 없습니다.: {}", fileName);
        }
      } catch (IOException e) {
        log.error("Could not delete file: {}", fileName, e);
      }
    }
  }
}
