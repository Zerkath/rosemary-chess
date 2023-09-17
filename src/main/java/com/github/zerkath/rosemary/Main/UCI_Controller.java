package com.github.zerkath.rosemary.Main;

import com.github.zerkath.rosemary.BoardRepresentation.BoardState;
import com.github.zerkath.rosemary.BoardRepresentation.FenUtils;
import com.github.zerkath.rosemary.DataTypes.MoveUtil;
import com.github.zerkath.rosemary.DataTypes.Moves;
import com.github.zerkath.rosemary.Evaluation.EvaluationThread;
import com.github.zerkath.rosemary.MoveGeneration.MoveGenerator;
import java.io.BufferedOutputStream;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class UCI_Controller extends OutputUtils {
  public BoardState boardState;
  public boolean uci_mode = true;
  public int depth = 6;
  public boolean debug = true;
  public ThreadGroup threadGroup = new ThreadGroup("evaluation");
  MoveGenerator moveGenerator = new MoveGenerator();

  private final String defaultBoard = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

  public UCI_Controller() {
    super(new BufferedOutputStream(System.out));
    boardState = new BoardState(defaultBoard);
  }

  public void setToDefault() {
    boardState = new BoardState(FenUtils.parseFen(defaultBoard));
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
      while (!queue.isEmpty()) {
        try {
          wait(500);
        } catch (InterruptedException ignored) {
        }
      }
      System.out.println("readyok");
    }
  }

  public void handleMessage(
      String message, BlockingQueue<String> queue) { // todo rework to tokenize the commands
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
          boardState.playMoves(moves);
        }
        return;
      }

      if (split[1].equals("startpos")) {
        setToDefault();
        String[] moves = Arrays.copyOfRange(split, 3, split.length);
        boardState.playMoves(moves);
        return;
      }
    }
    if (split[0].equals("isready")) {
      readyResponse(queue);
      return;
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
      println("register later");
      return;
    }
    if (split[0].equals("ucinewgame")) {
      setToDefault();
      return;
    }
    if (split[0].equals("xboard")) return;
    println("?");
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
    println("id name Rosemary");
    println("id author Rosemary_Devs");
    println("option name depth type spin default 6 min 1 max 8");
    println("uciok");
  }

  public void readyResponse(BlockingQueue<String> queue) {
    new Thread(new waitForReady(queue)).start(); // should wait for queue to empty
  }

  public void startEval() {
    startEval(this.depth);
  }

  public void endEval() {
    if (threadGroup.activeCount() > 0) threadGroup.interrupt();
  }

  public void startEval(int depth) {
    if (threadGroup.activeCount() < 1)
      new Thread(threadGroup, new EvaluationThread(this.boardState, depth, debug, writer)).start();
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
    println("avg " + average + " var " + variance + " totalRuns" + iterations);
  }

  public void getPerft(int depth) {
    long[] result = runPerft(depth, true);
    String str = "\nDepth " + depth + " nodes: " + result[0];
    println(str + " " + result[1] + "ms");
  }

  public long[] runPerft(int depth, boolean print) {
    return getPerftScore(depth, print, this.boardState);
  }

  public long[] runPerft(int depth, boolean print, BoardState v_boardState) {
    return getPerftScore(depth, print, v_boardState);
  }

  /**
   * Runs perft at set depth & return an array of how many nodes were found & how long the operation
   * took in MS
   *
   * @param depth ply depth
   * @param print whether to print to standard out, useful for debugging
   * @return long array where index 0 is for number of moves found & index 1 ms the operation took
   */
  private long[] getPerftScore(int depth, boolean print, BoardState bState) {
    long start = System.currentTimeMillis();
    int perftMoves = perft(depth, depth, print, bState);
    long end = System.currentTimeMillis();
    return new long[] {perftMoves, end - start};
  }

  private class PerftResult {
    Integer moveCount;
    short move;

    PerftResult(Integer count, short move) {
      this.moveCount = count;
      this.move = move;
    }

    void toStdOut(boolean print) {
      if (print) println(MoveUtil.moveToString(move) + ":" + moveCount);
    }
  }

  private int perft(int depth, int start, boolean print, BoardState boardState) {

    List<CompletableFuture<PerftResult>> list =
        moveGenerator.getLegalMoves(boardState).stream()
            .filter(m -> m != null)
            .map(
                move ->
                    CompletableFuture.supplyAsync(
                        () ->
                            new PerftResult(
                                perftProcess(
                                    depth - 1, start, boardState.makeNonModifyingMove(move)),
                                move)))
            .collect(Collectors.toList());

    CompletableFuture<Void> futures =
        CompletableFuture.allOf(list.toArray(new CompletableFuture[list.size()]));

    CompletableFuture<Integer> result =
        futures.thenApply(
            future -> {
              return list.stream()
                  .map(CompletableFuture::join)
                  .peek(pr -> pr.toStdOut(print))
                  .map(pr -> pr.moveCount)
                  .reduce(0, Integer::sum);
            });

    try {
      return result.get();
    } catch (Throwable e) {
      return 0;
    }
  }

  private int perftProcess(int depth, int start, BoardState boardState) {
    if (depth <= 0) return 1;

    int numPositions = 0;
    Moves moves = moveGenerator.getLegalMoves(boardState);

    for (short move : moves) {
      boardState.makeMove(move);
      int result = perftProcess(depth - 1, start, boardState);
      numPositions += result;
      boardState.unMakeMove();
    }
    return numPositions;
  }
}
