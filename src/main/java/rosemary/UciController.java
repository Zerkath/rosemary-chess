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
    private final String defaultBoard = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public UciController() {
        boardState = new BoardState(defaultBoard);
    }

    public void setToDefault() {
        boardState = new BoardState(FenUtils.parseFen(defaultBoard));
    }

    public void handleMessage(String message) { // todo rework to tokenize the commands
        // given
        // by the ui (change to switch case)
        String[] split = message.split(" ");
        if (message.equals("uci")) {
            setToUCI();
            return;
        }
        if (split[0].equals("position")) {
            if (split[1].equals("fen")) {
                int startIndex = message.indexOf('"') + 1;
                int endIndex = message.indexOf('"', startIndex + 1);
                setFen(message.substring(startIndex, endIndex));
                if (endIndex + 2 < message.length()) {
                    String[] moves = message.substring(endIndex + 2).split(" ");
                    moves = Arrays.copyOfRange(moves, 1, moves.length);
                    boardState = Mover.makeMoves(boardState, moves);
                }
                return;
            }

            if (split[1].equals("startpos")) {
                setToDefault();
                String[] moves = Arrays.copyOfRange(split, 3, split.length);
                boardState = Mover.makeMoves(boardState, moves);
                return;
            }
        }
        if (split[0].equals("go")) {
            if (split.length > 1) {
                if (split[1].equals("perft")) {
                    int depth = Integer.parseInt(split[2]);

                    if (split.length > 3) {
                        int iterations = Integer.parseInt(split[3]);
                        superPerft(depth, iterations);
                    } else {
                        getPerft(depth);
                    }
                    return;
                }
            }
            startEval();
            return;
        }
        if (split[0].equals("stop")) {
            endEval();
            return;
        }
        if (split[0].equals("setoption")) {
            if (split.length >= 4) {
                if (split[2].equals("depth")) {
                    this.depth = Integer.parseInt(split[4]);
                    return;
                }
            }
        }
        if (split[0].equals("quit")) {
            System.exit(0);
        }
        if (split[0].equals("debug")) {
            debug = split[1].equals("on");
            return;
        }
        if (split[0].equals("register")) {
            OutputUtils.println("register later");
            return;
        }
        if (split[0].equals("ucinewgame")) {
            setToDefault();
            return;
        }
        if (split[0].equals("xboard")) return;
        OutputUtils.println("?");
    }

    public void setFen(String fen) {
        boardState = new BoardState(FenUtils.parseFen(fen));
    }

    public String getFen() {
        return boardState.toFenString();
    }

    public void setToUCI() {
        uci_mode = true;
        // System.out.print("option name Threads type spin default 2 min 1 max 250\n");
        OutputUtils.println("id name Rosemary");
        OutputUtils.println("id author Rosemary_Devs");
        OutputUtils.println("option name depth type spin default 6 min 1 max 8");
        OutputUtils.println("uciok");
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
        variance = variance / (double) iterations;
        OutputUtils.println("avg " + average + " var " + variance + " runs " + iterations);
    }

    public void getPerft(int depth) {
        long[] result = runPerft(depth, true);
        String str = "\nDepth " + depth + " nodes: " + result[0];
        OutputUtils.println(str + " " + Long.toUnsignedString(result[1]) + "ms");
    }

    public long[] runPerft(int depth, boolean print) {
        return PerftRunner.getPerftScore(depth, print, this.boardState);
    }
}
