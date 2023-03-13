package dev.siyah.filemanager.exception;

public class FileSaveException extends RuntimeException {
    public FileSaveException() {
        super("Caught exception on file save");
    }
}
