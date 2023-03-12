package dev.siyah.filemanager.utility;

import dev.siyah.filemanager.exception.FileSaveException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtility {
    public static void saveMultipartAsFile(String path, MultipartFile file) throws IOException {
        File saveFile = new File(path);

        if (!saveFile.getParentFile().exists()) {
            boolean createFolderResult = saveFile.mkdirs();

            if (!createFolderResult) {
                throw new FileSaveException();
            }
        }

        file.transferTo(saveFile);
    }

    public static boolean deleteFileIfExists(String path) {
        try {
            Path fileToDeletePath = Paths.get(path);
            Files.delete(fileToDeletePath);

            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static boolean deleteFileParentIfExists(String path) {
        try {
            Path fileToDeletePath = Paths.get(path);
            Path parent = fileToDeletePath.getParent();

            Files.delete(fileToDeletePath);
            Files.delete(parent);

            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static boolean move(String oldPath, String newPath) {
        try {
            File oldFile = new File(oldPath);
            File newFile = new File(newPath);

            if (!oldFile.exists()) {
                throw new Exception("Old file not founded");
            }

            if (newFile.exists()) {
                throw new Exception("New file already exists");
            } else if (!newFile.getParentFile().exists()) {
                if (!newFile.mkdirs()) {
                    throw new Exception("New file path not created");
                }
            }

            Files.move(Paths.get(oldPath), Paths.get(newPath));

            return true;
        } catch (Exception ignored) {
            return false;
        }
    }
}