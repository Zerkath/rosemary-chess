package rosemary;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import rosemary.board.BoardState;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PerftTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/perft.csv", numLinesToSkip = 1)
    void testQuickPerft(String input, int depth, int expected) {
        System.out.println("Depth: " + depth);
        BoardState boardState = new BoardState(input);

        long score = PerftRunner.perft(depth, true, boardState);
        Assertions.assertEquals(expected, score, score);
    }

    @ParameterizedTest
    @Tag("slow")
    @CsvFileSource(resources = "/slow_perft.csv", numLinesToSkip = 1)
    void testSlowPerft(String input, int depth, int expected) {
        System.out.println("Depth: " + depth);
        BoardState boardState = new BoardState(input);

        long score = PerftRunner.perft(depth, true, boardState);
        Assertions.assertEquals(expected, score, score);
    }
}
