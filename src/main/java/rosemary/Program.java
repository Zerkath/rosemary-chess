package rosemary;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.*;

public class Program {
    public static void main(String[] args) throws InterruptedException, IOException {
        System.err.println("Rosemary by the Rosemary_devs");
        UciController uci = new UciController();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in, UTF_8))) {
            in.lines().allMatch(uci::handleMessage); // stops when handleMessage returns false
        }
    }
}
