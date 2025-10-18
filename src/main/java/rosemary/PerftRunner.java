package rosemary;

import rosemary.board.*;
import rosemary.generation.MoveGenerator;
import rosemary.types.MoveUtil;

public class PerftRunner {

    public static long perft(int depth, boolean print, BoardState boardState) {
        return MoveGenerator.getLegalMoves(boardState)
                .intStream()
                .map(move -> {
                    int result = perftProcess(depth - 1, Mover.makeMove(boardState, (short) move));
                    if (print) {
                        System.out.println(MoveUtil.moveToString((short) move) + " " + result);
                    }
                    return result;
                })
                .sum();
    }

    private static int perftProcess(int depth, BoardState boardState) {
        if (depth <= 0) return 1;
        return MoveGenerator.getLegalMoves(boardState)
                .intStream()
                .map(move -> perftProcess(depth - 1, Mover.makeMove(boardState, (short) move)))
                .sum();
    }

    /**
     * Runs perft at set depth & return an array of how many nodes were found & how long the
     * operation took in MS
     *
     * @param depth ply depth
     * @param print whether to print to standard out, useful for debugging
     * @return long array where index 0 is for number of moves found & index 1 ms the operation took
     */
    public static long[] getPerftScore(int depth, boolean print, BoardState bState) {
        long start = System.currentTimeMillis();
        long perftMoves = perft(depth, print, bState);
        long end = System.currentTimeMillis();
        return new long[] {perftMoves, end - start};
    }
}
