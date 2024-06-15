package rosemary;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class OutputUtils {

    private static final BufferedOutputStream writer = new BufferedOutputStream(System.out);

    public static void print(String str) {
        try {
            writer.write(str.getBytes(StandardCharsets.UTF_8));
            writer.flush();
        } catch (IOException ignored) {
        }
    }

    public static void println(String str) {
        print(str + "\r\n");
    }
}
