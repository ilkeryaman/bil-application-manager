package com.yaman.bil.helper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ByteOperations {
    public static List<String> getLines(byte[] byteArray) {
        List<String> lines = new ArrayList<>();
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = new ByteArrayInputStream(byteArray);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String temp = null;
            while ((temp = bufferedReader.readLine()) != null) {
                lines.add(temp);
            }
        } catch (IOException exc) {

        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception exc) {
            }
        }
        return lines;
    }
}
