package rosemary;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import rosemary.board.*;
import rosemary.generation.MoveGenerator;
import rosemary.types.MoveUtil;
import rosemary.types.Moves;

public class PerftRunner {

    MoveGenerator moveGenerator;

    public PerftRunner(MoveGenerator moveGenerator) {
        this.moveGenerator = moveGenerator;
    }

    private class PerftResult {
        Integer moveCount;
        short move;

        PerftResult(Integer count, short move) {
            this.moveCount = count;
            this.move = move;
        }

        int getMoveCount() {
            return moveCount;
        }

        void toStdOut(boolean print) {
            if (print) System.out.println(MoveUtil.moveToString(move) + ": " + moveCount);
        }
    }

    private CompletableFuture<PerftResult> futurePerft(
            short move, int depth, int start, BoardState boardState) {
        return CompletableFuture.supplyAsync(
                () ->
                        new PerftResult(
                                perftProcess(depth - 1, start, Mover.makeMove(boardState, move)),
                                move));
    }

    private int perft(int depth, int start, boolean print, BoardState boardState) {

        List<CompletableFuture<PerftResult>> list =
                moveGenerator.getLegalMoves(boardState).stream()
                        .filter(Objects::nonNull)
                        .map(move -> futurePerft(move, depth, start, boardState))
                        .collect(Collectors.toList());

        CompletableFuture<Integer> result =
                CompletableFuture.allOf(list.toArray(new CompletableFuture[0]))
                        .thenApplyAsync(
                                future ->
                                        list.stream()
                                                .map(CompletableFuture::join)
                                                .peek(pr -> pr.toStdOut(print))
                                                .map(PerftResult::getMoveCount)
                                                .reduce(0, Integer::sum));

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
            int result = perftProcess(depth - 1, start, Mover.makeMove(boardState, move));
            numPositions += result;
        }
        return numPositions;
    }

    /**
     * Runs perft at set depth & return an array of how many nodes were found & how long the
     * operation took in MS
     *
     * @param depth ply depth
     * @param print whether to print to standard out, useful for debugging
     * @return long array where index 0 is for number of moves found & index 1 ms the operation took
     */
    public long[] getPerftScore(int depth, boolean print, BoardState bState) {
        long start = System.currentTimeMillis();
        int perftMoves = perft(depth, depth, print, bState);
        long end = System.currentTimeMillis();
        return new long[] {perftMoves, end - start};
    }
}
