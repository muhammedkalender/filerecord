package dev.siyah.filemanager.utility;

import dev.siyah.filemanager.exception.FileSaveException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Component
public class FileUtility {
    public byte[] readByPath(String path) throws IOException {
       return Files.readAllBytes(Paths.get(path));
    }

    /**
     * @param filename String
     * @return String ( Fallback value is empty string )
     * @see <a href="https://www.baeldung.com/java-file-extension">Baeldung - How to Get the File Extension of a File in Java</a>
     */
    public String getExtensionByFileName(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1))
                .orElse("");
    }

    public void saveMultipartAsFile(String path, MultipartFile file) throws IOException {
        File saveFile = new File(path);

        if (!saveFile.getParentFile().exists()) {
            boolean createFolderResult = saveFile.mkdirs();

            if (!createFolderResult) {
                throw new FileSaveException();
            }
        }

        file.transferTo(saveFile);
    }

    public boolean deleteFileIfExists(String path) {
        try {
            Path fileToDeletePath = Paths.get(path);
            Files.delete(fileToDeletePath);

            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean deleteFileParentIfExists(String path) {
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

    public boolean move(String oldPath, String newPath) {
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
