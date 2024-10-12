package com.commenau.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WriteImage {
    public static String generateFileName(String fileName, String realPath) {
        String originalFileName = Paths.get(fileName).getFileName().toString();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newFileName = originalFileName.replace(fileExtension, "") + System.currentTimeMillis() + fileExtension;
        if (!Files.exists(Paths.get(realPath))) {
            try {
                Files.createDirectories(Paths.get(realPath));
            } catch (IOException e) {
                return null;
            }
        }
        return newFileName;
    }

    public static void writeImage(InputStream is, String realPath, String newFileName) throws IOException {
        // create buffer to read data from Part of InputStream
        byte[] buffer = new byte[1024];
        int bytesRead;
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        bis = new BufferedInputStream(is);
        bos = new BufferedOutputStream(new FileOutputStream(realPath + File.separator + newFileName));
        // read from InputStream and write into file
        while ((bytesRead = bis.read(buffer)) != -1)
            bos.write(buffer, 0, bytesRead);
        bos.flush();

        try {
            if (bis != null) bis.close();
            if (bos != null) bos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("writeImage success");
    }
}
