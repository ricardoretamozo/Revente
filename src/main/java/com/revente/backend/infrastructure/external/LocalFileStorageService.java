package com.revente.backend.infrastructure.external;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.revente.backend.application.service.FileStorageService;

@Service
@Primary
public class LocalFileStorageService implements FileStorageService {

    private final Path rootLocation = Paths.get("uploads");

    public LocalFileStorageService() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    @Override
    public String store(MultipartFile file, String subDirectory) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot store empty file");
        }
        try {
            Path destinationDir = rootLocation;
            if (subDirectory != null && !subDirectory.isEmpty()) {
                destinationDir = rootLocation.resolve(subDirectory);
                Files.createDirectories(destinationDir);
            }

            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path destinationFile = destinationDir.resolve(filename);

            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            return destinationFile.toAbsolutePath().toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}
