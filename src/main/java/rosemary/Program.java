package rosemary;

import java.io.*;

public class Program {
    public static void main(String[] args) throws InterruptedException, IOException {
        System.err.println("Rosemary by the Rosemary_devs");
        UciController uci = new UciController();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        in
          .lines()
          .takeWhile(s -> !s.trim().equals("quit"))
          .forEach(uci::handleMessage);
    }
}
