package rosemary;

import java.util.ArrayDeque;
import java.util.Deque;
import rosemary.board.*;
import rosemary.generation.MoveGenerator;
import rosemary.types.MoveUtil;

public class PerftRunner {
    public static long perft(int depth, boolean print, BoardState boardState) {
        return MoveGenerator.getLegalMoves(boardState)
                .intStream()
                .map(move -> {
                    int result = perftProcess(boardState, (short) move, depth);
                    if (print) {
                        System.out.println(MoveUtil.moveToString((short) move) + " " + result);
                    }
                    return result;
                })
                .sum();
    }

    private static int perftProcess(BoardState boardState, short initialMove, int depth) {
        if (depth == 0) return 1;
        record Node(BoardState state, int depth) {}

        int nodes = 0;
        Deque<Node> stack = new ArrayDeque<>();
        stack.push(new Node(Mover.makeMove(boardState, (short) initialMove), depth - 1));

        while (!stack.isEmpty()) {
            Node current = stack.pop();
            if (current.depth == 0) {
                nodes++;
                continue;
            }

            for (int move : MoveGenerator.getLegalMoves(current.state)) {
                BoardState next = Mover.makeMove(current.state, (short) move);
                stack.push(new Node(next, current.depth - 1));
            }
        }
        return nodes;
    }

    /**
     * Runs perft at set depth & return an array of how many nodes were found & how
     * long the
     * operation took in MS
     *
     * @param depth ply depth
     * @param print whether to print to standard out, useful for debugging
     * @return long array where index 0 is for number of moves found & index 1 ms
     *         the operation took
     */
    public static long[] getPerftScore(int depth, boolean print, BoardState bState) {
        long start = System.currentTimeMillis();
        long perftMoves = perft(depth, print, bState);
        long end = System.currentTimeMillis();
        return new long[] {perftMoves, end - start};
    }
}
