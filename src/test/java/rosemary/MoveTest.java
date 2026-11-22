package rosemary;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import rosemary.board.BoardState;
import rosemary.generation.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MoveTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/moves.csv", numLinesToSkip = 1)
    void testMoveGeneration(String input, int expected) {
        BoardState boardState = new BoardState(input);

        short[] moves = MoveGenerator.getLegalMoves(boardState).toShortArray();
        Assertions.assertEquals(expected, moves.length, moves.toString());
    }
}
