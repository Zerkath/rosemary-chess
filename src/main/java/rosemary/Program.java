package rosemary;

import java.io.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class Program {
    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("Rosemary by the Rosemary_devs");
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        InputHandler handler = new InputHandler(queue);
        handler.start();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = in.readLine()) != null) {
            queue.put(line);
            if (line.trim().equals("quit")) break;
        }

        handler.join();
    }
}

class InputHandler extends Thread {

    BlockingQueue<String> queue;
    UciController uci = new UciController();
    boolean active = true;

    public InputHandler(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    static String take(BlockingQueue<String> q) {
        try {
            return q.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }

    public void run() {
      Stream.generate(() -> take(queue))
        .takeWhile(s -> !s.trim().equals("quit"))
        .forEach(uci::handleMessage);
    }
}
