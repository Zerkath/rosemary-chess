package rosemary.generation;

import java.util.stream.IntStream;
import rosemary.board.BoardState;
import rosemary.types.MoveUtil;
import rosemary.types.Moves;

final class Rook implements Piece {

    static final Rook INSTANCE = new Rook();

    private static final class MoveTable {
        final Moves up = new Moves(8);
        final Moves down = new Moves(8);
        final Moves left = new Moves(8);
        final Moves right = new Moves(8);

        final Moves[] allMoves = new Moves[] {up, down, left, right};
    }

    // Moves should be generated from origin, outwards and be ordered
    private static final MoveTable[] TABLE = new MoveTable[64];

    static {
        for (short origin = 0; origin < 64; origin++) {
            MoveTable moves = new MoveTable();

            // Runs as long as destination is within board limits
            for (int i = 1; MoveUtil.getColumn(origin) - i >= 0; i++) {
                moves.left.add(SlidingMoves.getMove(0, -i, origin));
            }

            for (int i = 1; MoveUtil.getRow(origin) - i >= 0; i++) {
                moves.up.add(SlidingMoves.getMove(-i, 0, origin));
            }

            for (int i = 1; MoveUtil.getColumn(origin) + i <= 7; i++) {
                moves.right.add(SlidingMoves.getMove(0, i, origin));
            }

            for (int i = 1; MoveUtil.getRow(origin) + i <= 7; i++) {
                moves.down.add(SlidingMoves.getMove(i, 0, origin));
            }

            TABLE[origin] = moves;
        }
    }

    private Rook() {}

    @Override
    public IntStream moves(short origin, BoardState boardState) {
        return SlidingMoves.moves(TABLE[origin].allMoves, origin, boardState);
    }
}
