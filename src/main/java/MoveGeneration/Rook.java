package MoveGeneration;

import BoardRepresentation.BoardState;
import DataTypes.*;
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

public class Rook {

    // Moves should be generated from origin, outwards and be ordered
    static HashMap<Coordinate, RookMoves> rookMoves = new HashMap<>();

    private static Move getMove(int rowOffset, int colOffset, Coordinate origin) {
        return new Move(origin, Utils.getCoordinate(origin.row + rowOffset, origin.column + colOffset));
    }

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

    private static TargetSquare getSquareState(Move move, Board board, boolean isWhite) {

        int target = board.getCoordinate(move.destination);
        if (target == 0)
            return TargetSquare.EMPTY;

        return isWhite == Pieces.isWhite(target) ? TargetSquare.FRIENDLY : TargetSquare.ENEMY;
    }

    private static void addMove(TargetSquare state, Move move, Moves moves) {
        switch (state) {
            case EMPTY -> moves.add(move);
            case ENEMY -> moves.add(move);
            case FRIENDLY -> {
            }
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
