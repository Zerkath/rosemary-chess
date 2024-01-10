package rosemary.generation;

import rosemary.board.BoardState;
import rosemary.types.*;

class RookMoves {
    Moves up = new Moves();
    Moves down = new Moves();
    Moves left = new Moves();
    Moves right = new Moves();

    Moves[] allMoves = new Moves[] {up, down, left, right};

    RookMoves() {}
}

public class Rook extends SlidingPiece {

    // Moves should be generated from origin, outwards and be ordered
    static RookMoves[] rookMoves = new RookMoves[64];

    static {
        for (short origin = 0; origin < 64; origin++) {
            RookMoves moves = new RookMoves();

            // Runs as long as destination is within board limits
            for (int i = 1; MoveUtil.getColumn(origin) - i >= 0; i++) {
                moves.left.add(getMove(0, -i, origin));
            }

            for (int i = 1; MoveUtil.getRow(origin) - i >= 0; i++) {
                moves.up.add(getMove(-i, 0, origin));
            }

            for (int i = 1; MoveUtil.getColumn(origin) + i <= 7; i++) {
                moves.right.add(getMove(0, i, origin));
            }

            for (int i = 1; MoveUtil.getRow(origin) + i <= 7; i++) {
                moves.down.add(getMove(i, 0, origin));
            }

            rookMoves[origin] = moves;
        }
    }

    public static void getMoves(short origin, BoardState boardState, Moves moves) {

        Board board = boardState.board;

        boolean isWhite = Pieces.isWhite(board.getCoordinate(origin));

        Moves[] allMoves = rookMoves[origin].allMoves;

        for (Moves direction : allMoves) {
            for (short move : direction) {
                TargetSquare state = getSquareState(move, board, isWhite);
                addMove(state, move, moves);
                if (state != TargetSquare.EMPTY) break;
            }
        }
    }
}
