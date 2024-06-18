package rosemary;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import rosemary.board.BoardState;
import rosemary.generation.*;
import rosemary.types.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MoveTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/moves.csv", numLinesToSkip = 1)
    void testMoveGeneration(String input, int expected) {
        BoardState boardState = new BoardState(input);

        Moves moves = MoveGenerator.getLegalMoves(boardState);
        Assertions.assertEquals(expected, moves.size(), moves.toString());
    }
}
