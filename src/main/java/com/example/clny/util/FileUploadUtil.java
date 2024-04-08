package com.example.clny.util;

import com.example.clny.exception.custom.EmptyFileException;
import com.example.clny.exception.custom.FileNotImageException;
import com.example.clny.exception.custom.FileTooLargeException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.util.UUID;

public final class FileUploadUtil {

    private FileUploadUtil(){}

    public static String processFileUpload(MultipartFile file, Path directory) throws Exception {
        if (file.isEmpty()) {
            throw new EmptyFileException();
        }

        String mimeType = file.getContentType();
        if (mimeType == null || !mimeType.startsWith("image/")) {
            throw new FileNotImageException();
        }

        long maxFileSize = 10 * 1024 * 1024; // 10MB
        if (file.getSize() > maxFileSize) {
            throw new FileTooLargeException();
        }

        String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + fileType;

        Path storePath = directory.resolve(newFileName);
        file.transferTo(storePath);

        return getWebPath(storePath);
    }

    public static String getWebPath(Path storePath) {
        return storePath.toString().substring(storePath.toString().indexOf("/uploads"));
    }

    public static void deleteFileAtPath(String webPath, Path directory) {
        if (webPath != null && !webPath.isEmpty()) {
            String fileName = webPath.substring(webPath.lastIndexOf('/') + 1);
            File fileToDelete = directory.resolve(fileName).toFile();
            fileToDelete.delete();
        }
    }

}
