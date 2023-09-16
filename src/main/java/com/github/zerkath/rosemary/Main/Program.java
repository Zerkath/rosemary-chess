package com.github.zerkath.rosemary.Main;

import java.io.*;
import java.util.concurrent.*;

public class Program {
    public static void main(String[] args) {
        (new Program()).process();

    }
    public void process() {
        System.out.println("Rosemary by the Rosemary_devs");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        InputHandler handler = new InputHandler(queue);
        handler.start();

        while(handler.isAlive()) {

            try {
                while (in.ready()) {
                    in.read();
                }
            } catch (Exception ignored) {}

            String input = null;
            try {
                input = in.readLine();
            } catch (Exception ignored) {}

            if(input != null) queue.offer(input);
        }

    }
}

class InputHandler extends Thread {

    BlockingQueue<String> queue;
    UCI_Controller uci = new UCI_Controller();
    boolean active = true;
    public InputHandler(BlockingQueue<String> queue) {
        this.queue = queue;
        this.setDaemon(true);
    }

    public void run() {

        while(active) {
            String input;
            try {
                input = queue.poll(5000, TimeUnit.MILLISECONDS);

                if(input != null) {
                    uci.handleMessage(input, queue);
                }
            } catch (Exception ignored) {}
        }
    }
}
