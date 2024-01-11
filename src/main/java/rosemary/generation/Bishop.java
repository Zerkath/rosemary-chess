package rosemary.generation;

import rosemary.board.BoardState;
import rosemary.types.*;

class BishopMoves {
    Moves downRight = new Moves();
    Moves downLeft = new Moves();
    Moves upRight = new Moves();
    Moves upLeft = new Moves();

    Moves[] allMoves = new Moves[] {downRight, downLeft, upRight, upLeft};

    BishopMoves() {}
}

public class Bishop extends SlidingPiece {

    // Moves should be generated from origin, outwards and be ordered
    static BishopMoves[] bishopMoves = new BishopMoves[64];

    static {
        for (short origin = 0; origin < 64; origin++) {
            BishopMoves moves = new BishopMoves();

            // Bishop moves down and right
            for (int i = 1;
                    MoveUtil.getRow(origin) + i <= 7 && MoveUtil.getColumn(origin) + i <= 7;
                    i++) {
                moves.downRight.add(getMove(i, i, origin));
            }

            // Bishop moves down and left
            for (int i = 1;
                    MoveUtil.getRow(origin) + i <= 7 && MoveUtil.getColumn(origin) - i >= 0;
                    i++) {
                moves.downLeft.add(getMove(i, -i, origin));
            }

            // Bishop moves up and right
            for (int i = 1;
                    MoveUtil.getRow(origin) - i >= 0 && MoveUtil.getColumn(origin) + i <= 7;
                    i++) {
                moves.upRight.add(getMove(-i, i, origin));
            }

            // Bishop moves up and left
            for (int i = 1;
                    MoveUtil.getRow(origin) - i >= 0 && MoveUtil.getColumn(origin) - i >= 0;
                    i++) {
                moves.upLeft.add(getMove(-i, -i, origin));
            }
            bishopMoves[origin] = moves;
        }
    }

    public static void getMoves(short origin, BoardState boardState, Moves moves) {

        boolean isWhite = Pieces.isWhite(boardState.board[origin]);

        Moves[] allMoves = bishopMoves[origin].allMoves;

        for (Moves direction : allMoves) {
            for (short move : direction) {
                TargetSquare state = getSquareState(move, boardState.board, isWhite);
                addMove(state, move, moves);
                if (state != TargetSquare.EMPTY) break;
            }
        }
    }
}
