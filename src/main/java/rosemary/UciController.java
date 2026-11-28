package rosemary;

import java.util.*;
import rosemary.board.*;
import rosemary.eval.*;

public class UciController {

    public BoardState boardState;
    public boolean uci_mode = true;
    public int depth = 6;
    public boolean debug = true;
    public ThreadGroup threadGroup = new ThreadGroup("evaluation");
    private final BoardState defaultBoard = FenUtils.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    private static final int FEN_TOKEN_COUNT = 6;

    public UciController() {
        boardState = defaultBoard.clone();
    }

    public void setToDefault() {
        boardState = defaultBoard.clone();
    }

    /** @return false when the engine should shut down */
    public boolean handleMessage(String message) {
        String[] split = message.trim().split("\\s+");
        switch (split[0]) {
            case "uci" -> setToUCI();
            case "isready" -> System.out.println("readyok");
            case "ucinewgame" -> setToDefault();
            case "position" -> handlePosition(split);
            case "go" -> handleGo(split);
            case "stop" -> endEval();
            case "setoption" -> handleSetOption(split);
            case "debug" -> debug = split.length > 1 && split[1].equals("on");
            case "register" -> System.out.println("info string register later");
            case "quit" -> {
                return false;
            }
            case "xboard" -> {}
            default -> {} // UCI: ignore unknown commands ("?" is XBoard, not UCI)
        }
        return true;
    }

    /**
     * position [startpos | fen <fenstring>] [moves <move1> ...]
     *
     * <p>The fenstring may be quoted or bare; both spellings are accepted.
     */
    private void handlePosition(String[] split) {
        if (split.length < 2) return;

        int next;
        if (split[1].equals("startpos")) {
            setToDefault();
            next = 2;
        } else if (split[1].equals("fen")) {
            int fenEnd = Math.min(split.length, FEN_TOKEN_COUNT + 2);
            if (fenEnd <= 2) return;
            setFen(unquote(String.join(" ", Arrays.copyOfRange(split, 2, fenEnd))));
            next = fenEnd;
        } else {
            return;
        }

        if (next < split.length && split[next].equals("moves") && next + 1 < split.length) {
            boardState = Mover.makeMoves(boardState, Arrays.copyOfRange(split, next + 1, split.length));
        }
    }

    /** go perft <depth> [iterations], otherwise start a search at the configured depth */
    private void handleGo(String[] split) {
        if (split.length > 1 && split[1].equals("perft")) {
            int perftDepth = split.length > 2 ? parseInt(split[2], -1) : -1;
            if (perftDepth < 0) return;

            if (split.length > 3) {
                int iterations = parseInt(split[3], -1);
                if (iterations < 1) return;
                superPerft(perftDepth, iterations);
            } else {
                getPerft(perftDepth);
            }
            return;
        }
        startEval();
    }

    /** setoption name <name> value <value> */
    private void handleSetOption(String[] split) {
        int name = indexOf(split, "name");
        int value = indexOf(split, "value");
        if (name < 0 || name + 1 >= split.length || value < 0 || value + 1 >= split.length) {
            return;
        }
        if (split[name + 1].equals("depth")) {
            this.depth = parseInt(split[value + 1], this.depth);
        }
    }

    private static String unquote(String s) {
        return s.replace("\"", "").trim();
    }

    private static int indexOf(String[] tokens, String needle) {
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equals(needle)) return i;
        }
        return -1;
    }

    private static int parseInt(String token, int fallback) {
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    public void setFen(String fen) {
        boardState = FenUtils.parseFen(fen);
    }

    public String getFen() {
        return FenUtils.getFenString(boardState);
    }

    public void setToUCI() {
        uci_mode = true;
        // System.out.print("option name Threads type spin default 2 min 1 max 250\n");
        System.out.println("id name Rosemary");
        System.out.println("id author Rosemary_Devs");
        System.out.println("option name depth type spin default 6 min 1 max 8");
        System.out.println("uciok");
    }

    public void startEval() {
        startEval(this.depth);
    }

    public void endEval() {
        if (threadGroup.activeCount() > 0) threadGroup.interrupt();
    }

    public void startEval(int depth) {
        if (threadGroup.activeCount() < 1)
            new Thread(threadGroup, new EvaluationThread(this.boardState, depth, debug)).start();
    }

    public void superPerft(int depth, int iterations) {
        long[] allRuns = new long[iterations];

        // warm up
        for (int i = 0; i < 3; i++) {
            runPerft(depth, false);
        }

        // gather results
        for (int i = 0; i < iterations; i++) {
            long[] result = runPerft(depth, false);
            allRuns[i] = result[1];
        }

        long sum = 0;
        for (long l : allRuns) {
            sum += l;
        }

        double average = sum / (double) iterations;
        double variance = 0;
        for (long l : allRuns) {
            variance += Math.abs(l - average);
        }
        variance /= (double) iterations;
        System.out.println("avg " + average + " var " + variance + " runs " + iterations);
    }

    public void getPerft(int depth) {
        long[] result = runPerft(depth, true);
        String str = "\nDepth " + depth + " nodes: " + result[0];
        System.out.println(str + " " + Long.toUnsignedString(result[1]) + "ms");
    }

    public long[] runPerft(int depth, boolean print) {

        long[] scores = PerftRunner.getPerftScore(depth, print, this.boardState);
        return scores;
    }
}
