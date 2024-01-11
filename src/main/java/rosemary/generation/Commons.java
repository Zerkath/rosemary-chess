package rosemary.generation;

import rosemary.types.MoveUtil;
import rosemary.types.Moves;
import rosemary.types.Pieces;
import rosemary.types.Utils;

public class Commons {

    static boolean pieceMoveNotPossible(
            int rowOffset,
            int colOffset,
            byte[] board,
            Moves moves,
            short origin,
            boolean isWhite) {
        short destination =
                Utils.getCoordinate(
                        MoveUtil.getRow(origin) + rowOffset,
                        MoveUtil.getColumn(origin) + colOffset);
        int target = board[destination];

        if (target == 0) {
            moves.add(origin, destination);
            return false;
        }
        if (isWhite != Pieces.isWhite(target)) moves.add(origin, destination);
        return true;
    }
}
