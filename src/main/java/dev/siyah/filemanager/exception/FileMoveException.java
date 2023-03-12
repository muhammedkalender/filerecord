package dev.siyah.filemanager.exception;

public class FileMoveException extends RuntimeException {
    public FileMoveException() {
        super("Caught exception on file move");
    }
}
