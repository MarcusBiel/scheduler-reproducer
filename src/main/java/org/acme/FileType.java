package org.acme;

public enum FileType {

    JPEG("image/jpeg", "images"),
    PNG("image/png", "images");

    private final String contentType;
    private final String directory;

    FileType(String contentType, String directory) {
        this.contentType = contentType;
        this.directory = directory;
    }

    public static FileType from(String contentType) {
        for (FileType fileType : FileType.values()) {
            if (fileType.contentType.equals(contentType)) {
                return fileType;
            }
        }
        throw new IllegalArgumentException("invalid contentType: " + contentType);
    }

    public static boolean isAllowed(String contentType) {
        for (FileType fileType : FileType.values()) {
            if (fileType.contentType.equals(contentType)) {
                return true;
            }
        }
        return false;
    }

    public String directory() {
        return directory;
    }

    public String contentType() {
        return contentType;
    }
}
