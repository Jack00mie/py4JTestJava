package utils;

import java.io.FileWriter;
import java.io.IOException;

public class Writer {
    public static <A> void store(A toStore, String fileName) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(String.valueOf(toStore) + ", ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
