package me.gv7.woodpecker.requests.body;

import me.gv7.woodpecker.requests.HttpHeaders;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

class ContentTypes {
    static String probeContentType(File file) {
        String contentType;
        try {
            contentType = Files.probeContentType(file.toPath());
        } catch (IOException e) {
            contentType = null;
        }
        if (contentType == null) {
            contentType = HttpHeaders.CONTENT_TYPE_BINARY;
        }
        return contentType;
    }

    /**
     * If content type looks like a text content.
     */
    static boolean isText(String contentType) {
        return contentType.contains("text") || contentType.contains("json")
                || contentType.contains("xml") || contentType.contains("html");
    }
}
