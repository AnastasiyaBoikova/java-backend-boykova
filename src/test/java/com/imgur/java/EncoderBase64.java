package com.imgur.java;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class EncoderBase64 {

    String INPUT_IMAGE_FILE_PATH;

      public String fileString (){
        byte[] fileContent = getFileContent();
        return Base64.getEncoder().encodeToString(fileContent);

    }

     private byte[] getFileContent() {
        ClassLoader classLoader = getClass().getClassLoader();
        File inputFile = new File(classLoader.getResource(INPUT_IMAGE_FILE_PATH).getFile());

        byte[] bytes = new byte[0];
        try {
            bytes = FileUtils.readFileToByteArray(inputFile);
            //           bytes = FileUtils.readFileToByteArray("src/test/resources/bird.jpg"); - абсолютный путь. 66-71 не нужно было бы
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
    public EncoderBase64(String INPUT_IMAGE_FILE_PATH) {
        this.INPUT_IMAGE_FILE_PATH = INPUT_IMAGE_FILE_PATH;
    }

}
