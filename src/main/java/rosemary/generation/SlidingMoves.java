package rosemary.generation;

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

    static void moves(Moves[] directions, short origin, BoardState boardState, Moves out) {
        byte[] board = boardState.getBoard();
        boolean isWhite = Pieces.isWhite(board[origin]);

        for (Moves direction : directions) {
            for (int i = 0; i < direction.size(); i++) {
                short move = direction.getShort(i);
                byte state = getSquareState(move, board, isWhite);

                if (state == TargetSquare.FRIENDLY) break;

                out.add(move);
                if (state == TargetSquare.ENEMY) break;
            }
        }
    }
}
