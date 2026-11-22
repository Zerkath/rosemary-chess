package rosemary.generation;

import rosemary.board.BoardState;
import rosemary.types.BoardUtils;
import rosemary.types.MoveUtil;
import rosemary.types.Moves;
import rosemary.types.Utils;

final class Knight {

    private static final Moves[] TABLE = new Moves[64];

    static {
        for (short origin = 0; origin < 64; origin++) {
            short row = MoveUtil.getRow(origin);
            short col = MoveUtil.getColumn(origin);

            short[] columns = new short[] {(short) (col - 2), (short) (col + 2)};
            short[] rows = new short[] {(short) (row - 2), (short) (row + 2)};
            Moves moves = new Moves();
            for (int dColumn : columns) {
                Utils.addToCollection(row + 1, dColumn, row, col, moves);
                Utils.addToCollection(row - 1, dColumn, row, col, moves);
            }

            for (int dRow : rows) {
                Utils.addToCollection(dRow, col + 1, row, col, moves);
                Utils.addToCollection(dRow, col - 1, row, col, moves);
            }
            TABLE[origin] = moves;
        }
    }

    private Knight() {}

    static void getMoves(short origin, BoardState boardState, Moves moves) {
        Moves table = TABLE[origin];
        byte[] board = boardState.getBoard();

        for (int i = 0; i < table.size(); i++) {
            short move = table.getShort(i);
            if (BoardUtils.isValidMove(move, board)) moves.add(move);
        }
    }
}
