package dev.siyah.filemanager.exception;

public class FileDeleteException extends RuntimeException {
    public FileDeleteException() {
        super("Caught exception on file delete");
    }
}
