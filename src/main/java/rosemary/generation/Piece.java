package rosemary.generation;

import java.util.stream.IntStream;
import rosemary.board.BoardState;

interface Piece {
    IntStream moves(short origin, BoardState boardState);
}
