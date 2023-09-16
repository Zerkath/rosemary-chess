package com.github.zerkath.rosemary.MoveGeneration;

import com.github.zerkath.rosemary.BoardRepresentation.BoardState;
import com.github.zerkath.rosemary.DataTypes.*;
import java.util.HashMap;

class RookMoves {
    Moves up = new Moves();
    Moves down = new Moves();
    Moves left = new Moves();
    Moves right = new Moves();

    Moves[] allMoves = new Moves[] { up, down, left, right };

    RookMoves() {
    }
}

public class Rook extends SlidingPiece {

    // Moves should be generated from origin, outwards and be ordered
    static HashMap<Coordinate, RookMoves> rookMoves = new HashMap<>();

    static {

        for (Coordinate origin : Utils.allCoordinates) {

            RookMoves moves = new RookMoves();

            // Runs as long as destination is within board limits
            for (int i = 1; origin.column - i >= 0; i++) {
                moves.left.add(getMove(0, -i, origin));
            }

            for (int i = 1; origin.row - i >= 0; i++) {
                moves.up.add(getMove(-i, 0, origin));
            }

            for (int i = 1; origin.column + i <= 7; i++) {
                moves.right.add(getMove(0, i, origin));
            }

            for (int i = 1; origin.row + i <= 7; i++) {
                moves.down.add(getMove(i, 0, origin));
            }

            rookMoves.put(origin, moves);
        }
    }

    public static void getMoves(Coordinate origin, BoardState boardState, Moves moves) {

        Board board = boardState.board;

        boolean isWhite = Pieces.isWhite(board.getCoordinate(origin));

        Moves[] allMoves = rookMoves.get(origin).allMoves;

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
