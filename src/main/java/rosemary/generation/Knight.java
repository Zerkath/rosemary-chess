package rosemary.generation;

import java.util.stream.IntStream;
import rosemary.board.BoardState;
import rosemary.types.BoardUtils;
import rosemary.types.MoveUtil;
import rosemary.types.Moves;
import rosemary.types.Utils;

final class Knight implements Piece {

    static final Knight INSTANCE = new Knight();

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

    @Override
    public IntStream moves(short origin, BoardState boardState) {
        byte[] board = boardState.getBoard();
        return TABLE[origin]
                .intStream()
                .filter(move -> BoardUtils.isValidMove((short) move, board));
    }
}
