package rosemary.generation;

import it.unimi.dsi.fastutil.shorts.ShortIterator;
import rosemary.board.BoardState;
import rosemary.types.*;

public class Knight {

    static Moves[] knightMoves = new Moves[64];

    static {
        for (short origin = 0; origin < 64; origin++) {
            short row = MoveUtil.getRow(origin);
            short col = MoveUtil.getColumn(origin);

            short[] columns = new short[] {(short) (col - 2), (short) (col + 2)};
            short[] rows = new short[] {(short) (row - 2), (short) (row + 2)};
            Moves moves = new Moves();
            for (int d_column : columns) {
                Utils.addToCollection(row + 1, d_column, row, col, moves);
                Utils.addToCollection(row - 1, d_column, row, col, moves);
            }

            for (int d_row : rows) {
                Utils.addToCollection(d_row, col + 1, row, col, moves);
                Utils.addToCollection(d_row, col - 1, row, col, moves);
            }
            knightMoves[origin] = moves;
        }
    }

    public static void getMoves(short origin, BoardState boardState, Moves moves) {

        ShortIterator iter = knightMoves[origin].iterator();
        while (iter.hasNext()) {
            short move = iter.nextShort();
            if (BoardUtils.isValidMove(move, boardState.getBoard())) moves.add(move);
        }
    }
}
