package rosemary.generation;

import rosemary.board.BoardState;
import rosemary.types.MoveUtil;
import rosemary.types.Moves;

final class Bishop {

    private static final class MoveTable {
        final Moves downRight = new Moves(8);
        final Moves downLeft = new Moves(8);
        final Moves upRight = new Moves(8);
        final Moves upLeft = new Moves(8);

        final Moves[] allMoves = new Moves[] {downRight, downLeft, upRight, upLeft};
    }

    // Moves should be generated from origin, outwards and be ordered
    private static final MoveTable[] TABLE = new MoveTable[64];

    static {
        for (short origin = 0; origin < 64; origin++) {
            MoveTable moves = new MoveTable();

            // Bishop moves down and right
            for (int i = 1;
                    MoveUtil.getRow(origin) + i <= 7 && MoveUtil.getColumn(origin) + i <= 7;
                    i++) {
                moves.downRight.add(SlidingMoves.getMove(i, i, origin));
            }

            // Bishop moves down and left
            for (int i = 1;
                    MoveUtil.getRow(origin) + i <= 7 && MoveUtil.getColumn(origin) - i >= 0;
                    i++) {
                moves.downLeft.add(SlidingMoves.getMove(i, -i, origin));
            }

            // Bishop moves up and right
            for (int i = 1;
                    MoveUtil.getRow(origin) - i >= 0 && MoveUtil.getColumn(origin) + i <= 7;
                    i++) {
                moves.upRight.add(SlidingMoves.getMove(-i, i, origin));
            }

            // Bishop moves up and left
            for (int i = 1;
                    MoveUtil.getRow(origin) - i >= 0 && MoveUtil.getColumn(origin) - i >= 0;
                    i++) {
                moves.upLeft.add(SlidingMoves.getMove(-i, -i, origin));
            }

            TABLE[origin] = moves;
        }
    }

    private Bishop() {}

    static void getMoves(short origin, BoardState boardState, Moves moves) {
        SlidingMoves.moves(TABLE[origin].allMoves, origin, boardState, moves);
    }
}
