package com.imgur.java.dto.Response;

public enum Images {

    POSITIVE("src/test/resources/bird.jpg"),
    TOO_BIG("src/test/resources/Big.png"),
    ZERO_SIZE("src/test/resources/null_jpg.jpg"),
    NOT_FORMAT("src/test/resources/notFormat.docx");

    public final String path;

    Images(String path) {
        this.path = path;
    }
}
