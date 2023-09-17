package com.github.zerkath.rosemary.MoveGeneration;

import com.github.zerkath.rosemary.BoardRepresentation.BoardState;
import com.github.zerkath.rosemary.DataTypes.*;
import java.util.HashMap;

class BishopMoves {
    Moves downRight = new Moves();
    Moves downLeft = new Moves();
    Moves upRight = new Moves();
    Moves upLeft = new Moves();

    Moves[] allMoves = new Moves[] { downRight, downLeft, upRight, upLeft };

    BishopMoves() {
    }
}

public class Bishop extends SlidingPiece {

    // Moves should be generated from origin, outwards and be ordered
    static HashMap<Coordinate, BishopMoves> bishopMoves = new HashMap<>();

    static {
        for (short originX = 0; originX < 64; originX++) {
            Coordinate origin = new Coordinate(originX);
            BishopMoves moves = new BishopMoves();

            // Bishop moves down and right
            for (int i = 1; origin.getRow() + i <= 7 && origin.getColumn() + i <= 7; i++) {
                moves.downRight.add(getMove(i, i, origin));
            }

            // Bishop moves down and left
            for (int i = 1; origin.getRow() + i <= 7 && origin.getColumn() - i >= 0; i++) {
                moves.downLeft.add(getMove(i, -i, origin));
            }

            // Bishop moves up and right
            for (int i = 1; origin.getRow() - i >= 0 && origin.getColumn() + i <= 7; i++) {
                moves.upRight.add(getMove(-i, i, origin));
            }

            // Bishop moves up and left
            for (int i = 1; origin.getRow() - i >= 0 && origin.getColumn() - i >= 0; i++) {
                moves.upLeft.add(getMove(-i, -i, origin));
            }
            bishopMoves.put(origin, moves);
        }
    }

    public static void getMoves(Coordinate origin, BoardState boardState, Moves moves) {

        Board board = boardState.board;

        boolean isWhite = Pieces.isWhite(board.getCoordinate(origin.coord));

        Moves[] allMoves = bishopMoves.get(origin).allMoves;

        for (Moves direction : allMoves) {
            for (Move move : direction) {
                TargetSquare state = getSquareState(move, board, isWhite);
                addMove(state, move, moves);
                if (state != TargetSquare.EMPTY)
                    break;
            }
        }
    }
}
