package dev.siyah.filemanager.enums;

import lombok.Getter;

@Getter
public enum FileExtension {
    PNG("png"),
    JPEG("jpeg"),
    JPG("jpg"),
    DOCX("docx"),
    PDF("pdf"),
    XLSX("xlsx");

    private final String extension;

    FileExtension(String extension) {
        this.extension = extension;
    }
}
