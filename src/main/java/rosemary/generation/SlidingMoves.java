package rosemary.generation;

import it.unimi.dsi.fastutil.shorts.ShortIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import rosemary.board.BoardState;
import rosemary.types.MoveUtil;
import rosemary.types.Moves;
import rosemary.types.Pieces;
import rosemary.types.TargetSquare;
import rosemary.types.Utils;

final class SlidingMoves {

    private SlidingMoves() {}

    static short getMove(int rowOffset, int colOffset, short origin) {
        return MoveUtil.getMove(
                origin,
                Utils.getCoordinate(
                        MoveUtil.getRow(origin) + rowOffset,
                        MoveUtil.getColumn(origin) + colOffset));
    }

    static byte getSquareState(short move, byte[] board, boolean isWhite) {
        int target = board[MoveUtil.getDestination(move)];
        if (target == 0) return TargetSquare.EMPTY;

        return isWhite == Pieces.isWhite(target) ? TargetSquare.FRIENDLY : TargetSquare.ENEMY;
    }

    static IntStream moves(Moves[] directions, short origin, BoardState boardState) {
        byte[] board = boardState.getBoard();
        boolean isWhite = Pieces.isWhite(board[origin]);

        return Stream.of(directions)
                .flatMapToInt(direction -> movesUntilBlocked(direction, board, isWhite));
    }

    /**
     * Streams the moves of one direction until the first blocked square, looking at each square
     * exactly once. The blocking square is included only when it holds an enemy piece.
     */
    private static IntStream movesUntilBlocked(Moves direction, byte[] board, boolean isWhite) {
        ShortIterator iter = direction.iterator();

        return StreamSupport.intStream(
                new Spliterators.AbstractIntSpliterator(direction.size(), Spliterator.ORDERED) {
                    private boolean done;

                    @Override
                    public boolean tryAdvance(IntConsumer action) {
                        if (done || !iter.hasNext()) return false;

                        short move = iter.nextShort();
                        byte state = getSquareState(move, board, isWhite);

                        if (state == TargetSquare.FRIENDLY) {
                            done = true;
                            return false;
                        }

                        action.accept(move);
                        done = state == TargetSquare.ENEMY;
                        return true;
                    }
                },
                false);
    }
}
