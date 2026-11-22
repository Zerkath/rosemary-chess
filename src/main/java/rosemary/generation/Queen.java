package rosemary.generation;

import java.util.stream.IntStream;
import rosemary.board.BoardState;

final class Queen implements Piece {

    static final Queen INSTANCE = new Queen();

    private Queen() {}

    @Override
    public IntStream moves(short origin, BoardState boardState) {
        return IntStream.concat(
                Rook.INSTANCE.moves(origin, boardState), Bishop.INSTANCE.moves(origin, boardState));
    }
}
