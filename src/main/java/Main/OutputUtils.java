package Main;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class OutputUtils {

    BufferedOutputStream writer;
    public OutputUtils(BufferedOutputStream writer) {
        this.writer = writer;
    }

    public void print(String str) {
        try {
            writer.write(str.getBytes(StandardCharsets.UTF_8));
            writer.flush();
        } catch (IOException ignored) {}
    }
    public void println(String str) {
        print(str + "\r\n");
    }
}
