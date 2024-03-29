package rosemary.generation;

import rosemary.types.*;

abstract class SlidingPiece {

    static short getMove(int rowOffset, int colOffset, short origin) {
        return MoveUtil.getMove(
                origin,
                Utils.getCoordinate(
                        MoveUtil.getRow(origin) + rowOffset,
                        MoveUtil.getColumn(origin) + colOffset));
    }

    static TargetSquare getSquareState(short move, byte[] board, boolean isWhite) {

        int target = board[MoveUtil.getDestination(move)];
        if (target == 0) return TargetSquare.EMPTY;

        return isWhite == Pieces.isWhite(target) ? TargetSquare.FRIENDLY : TargetSquare.ENEMY;
    }

    static void addMove(TargetSquare state, short move, Moves moves) {
        switch (state) {
            case EMPTY -> moves.add(move);
            case ENEMY -> moves.add(move);
            case FRIENDLY -> {}
        }
    }
}
