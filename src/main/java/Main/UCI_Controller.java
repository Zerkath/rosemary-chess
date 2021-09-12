package Main;

import BoardRepresentation.BoardState;
import DataTypes.Move;
import DataTypes.Moves;

import Evaluation.EvaluationThread;
import MoveGeneration.MoveGenerator;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class UCI_Controller {
    public BoardState boardState;
    public boolean uci_mode;
    public int depth = 6;
    public boolean debug = false;
    public ThreadGroup threadGroup = new ThreadGroup("evaluation");
    MoveGenerator moveGenerator = new MoveGenerator();
    public final BufferedOutputStream bufferedWriter = new BufferedOutputStream(System.out);

    private final String defaultBoard = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public UCI_Controller() {
        boardState = new BoardState(defaultBoard);
    }

    public void setToDefault() {
        boardState = new BoardState(boardState.parseFen(defaultBoard));
    }

    public void handleMessage(String message) {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        handleMessage(message, queue);
    }

    static class waitForReady implements Runnable {

        BlockingQueue<String> queue;
        public waitForReady(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while(!queue.isEmpty()) {
                try {
                    wait(100);
                } catch (InterruptedException ignored) {}
            }
            System.out.println("readyok");
        }
    }

    public void handleMessage(String message, BlockingQueue<String> queue) { //todo rework to tokenize the commands given by the ui (change to switch case)
        String [] split = message.split(" ");
        if(message.equals("uci")) {
            setToUCI();
            return;
        }
        if(split[0].equals("position")) {
            if(split[1].equals("fen")) {
                int startIndex = message.indexOf('"') + 1;
                int endIndex = message.indexOf('"', startIndex+1);
                setFen(message.substring(startIndex, endIndex));
                if(endIndex+2 < message.length()) {
                    String [] moves = message.substring(endIndex+2).split(" ");
                    moves = Arrays.copyOfRange(moves, 1, moves.length);
                    boardState.playMoves(moves);
                }
                return;
            }

            if(split[1].equals("startpos")) {
                setToDefault();
                String [] moves = Arrays.copyOfRange(split, 3, split.length);
                boardState.playMoves(moves);
                return;
            }
        }
        if(split[0].equals("isready")) {
            readyResponse(queue);
            return;
        }
        if(split[0].equals("go")) {
            if(split.length > 2) {
                if(split[1].equals("perft")) {
                    int depth = Integer.parseInt(split[2]);
                    getPerft(depth);
                    return;
                }
            }
            startEval();
            return;
        }
        if(split[0].equals("stop")) {
            endEval();
            return;
        }
        if(split[0].equals("setoption")) {
            if(split.length >= 4) {
//                if(split[2].equals("Threads")) {
//                    this.threadCount = Integer.parseInt(split[4]);
//                    return;
//                }
                if(split[2].equals("depth")) {
                    this.depth = Integer.parseInt(split[4]);
                    return;
                }
            }
        }
        if(split[0].equals("quit")) {
            System.exit(0);
        }
        if(split[0].equals("debug")) {
            debug = split[1].equals("on");
            return;
        }
        if(split[0].equals("register")) {
            println("register later");
            return;
        }
        if(split[0].equals("ucinewgame")) {
            setToDefault();
            return;
        }
        if(split[0].equals("xboard")) return;
        println("?");
    }


    public void setFen(String fen) {
        boardState = new BoardState(boardState.parseFen(fen));
    }
    public String getFen() {
        return boardState.toFenString();
    }

    public void setToUCI() {
        uci_mode = true;

        String data = "id name Rosemary\nid author Rosemary_devs\noption name depth type spin default 6 min 1 max 7\nuciok";
//        System.out.print("option name Threads type spin default 2 min 1 max 250\n");
        print(data);
    }

    public void readyResponse(BlockingQueue<String> queue) {
        new Thread(new waitForReady(queue)).start(); //should wait for queue to empty
    }

    public void startEval() {
        startEval(this.depth);
    }

    public void endEval() {
        if(threadGroup.activeCount() > 0) {
            threadGroup.interrupt();
        }
    }

    public void startEval(int depth) {
        if(threadGroup.activeCount() < 1) {
            new Thread(threadGroup, new EvaluationThread(this.boardState, depth, debug)).start();
        }
    }

    public void getPerft(int depth) {
        long start = System.currentTimeMillis();
        String str = "\nDepth " + depth + " nodes: " + runPerft(depth, true);
        print(str);
        long end = System.currentTimeMillis();
        print(" " + (end - start) + "ms \n");
    }

    public int runPerft(int depth, boolean print) {
        return runPerft(depth, depth, print, this.boardState);
    }

    public int runPerft(int depth, int start, boolean print, BoardState boardState) {
        if(depth <= 0) {
            return 1;
        }
        Moves moves = moveGenerator.getLegalMoves(boardState);
        int numPositions = 0;

        for (Move move: moves) {
            boardState.makeMove(move);
            int result = runPerft(depth-1, start, print, boardState);
            if(depth == start && print) println(move + ": " + result);
            numPositions += result;
            boardState.unMakeMove();
        }
        return numPositions;
    }


    private void print(String str) {
        try {
            bufferedWriter.write(str.getBytes(StandardCharsets.UTF_8));
            bufferedWriter.flush();
        } catch (IOException ignored) {}
    }
    private void println(String str) {
        print(str + "\n");
    }
}