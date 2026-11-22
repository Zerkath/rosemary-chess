package rosemary.generation;

import rosemary.board.BoardState;
import rosemary.types.Moves;

final class Queen {

    private Queen() {}

    static void getMoves(short origin, BoardState boardState, Moves moves) {
        Rook.getMoves(origin, boardState, moves);
        Bishop.getMoves(origin, boardState, moves);
    }
}
