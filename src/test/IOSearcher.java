package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class IOSearcher {

    public static boolean search(String word, String... fileNames) throws IOException {
        for (String fileName : fileNames) {
            if (fileName.startsWith("[")) {
                fileName = fileName.substring(1);
                try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                    String line;
                    while ((line = reader.readLine()) != null)
                        if (line.contains(word)) {
                            reader.close();
                            return true;
                        }
                } catch (FileNotFoundException ignored) {}
            } else {
                if (fileName.endsWith("]"))
                    fileName = fileName.substring(0, fileName.length() - 1);
                try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                    String line;
                    while ((line = reader.readLine()) != null)
                        if (line.contains(word)) {
                            reader.close();
                            return true;
                        }
                } catch (FileNotFoundException ignored) {}
            }
        }
        return false;
    }
}