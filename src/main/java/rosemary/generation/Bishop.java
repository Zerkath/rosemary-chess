package rosemary.generation;

import it.unimi.dsi.fastutil.shorts.ShortIterator;
import rosemary.board.BoardState;
import rosemary.types.*;

class BishopMoves {
    Moves downRight = new Moves(8);
    Moves downLeft = new Moves(8);
    Moves upRight = new Moves(8);
    Moves upLeft = new Moves(8);

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
            for (int i = 1; MoveUtil.getRow(origin) + i <= 7 && MoveUtil.getColumn(origin) + i <= 7; i++) {
                moves.downRight.add(getMove(i, i, origin));
            }

            // Bishop moves down and left
            for (int i = 1; MoveUtil.getRow(origin) + i <= 7 && MoveUtil.getColumn(origin) - i >= 0; i++) {
                moves.downLeft.add(getMove(i, -i, origin));
            }

            // Bishop moves up and right
            for (int i = 1; MoveUtil.getRow(origin) - i >= 0 && MoveUtil.getColumn(origin) + i <= 7; i++) {
                moves.upRight.add(getMove(-i, i, origin));
            }

            // Bishop moves up and left
            for (int i = 1; MoveUtil.getRow(origin) - i >= 0 && MoveUtil.getColumn(origin) - i >= 0; i++) {
                moves.upLeft.add(getMove(-i, -i, origin));
            }
            bishopMoves[origin] = moves;
        }
    }

    public static void getMoves(short origin, BoardState boardState, Moves moves) {

        boolean isWhite = Pieces.isWhite(boardState.getBoard()[origin]);

        Moves[] allMoves = bishopMoves[origin].allMoves;

        for (Moves direction : allMoves) {
            ShortIterator iter = direction.iterator();
            while (iter.hasNext()) {
                short move = iter.nextShort();
                byte state = getSquareState(move, boardState.getBoard(), isWhite);
                addMove(state, move, moves);
                if (state != TargetSquare.EMPTY) break;
            }
        }
    }
}
