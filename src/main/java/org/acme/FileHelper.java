package org.acme;

import java.io.File;

public final class FileHelper {

    public static File getFile(String fileName) {
        return new File(FileHelper.class.getClassLoader().getResource(fileName).getFile());
    }

    private FileHelper() {
        throw new IllegalStateException("do not invoke");
    }
}
