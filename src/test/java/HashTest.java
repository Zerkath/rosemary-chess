import org.junit.jupiter.api.*;
import rosemary.board.*;
import rosemary.types.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HashTest {

    private final String start = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    @Test
    void hashingSameBoardShouldProduceSameValue() {
        BoardState state = new BoardState(start);
        BoardState state2 = new BoardState(start);
        Assertions.assertEquals(BoardHasher.hashBoard(state), BoardHasher.hashBoard(state2));
    }

    @Test
    void hashingDifferentBoardsShouldProduceDifferentValues() {
        BoardState state1 = new BoardState(start);
        BoardState state2 =
                new BoardState("rnbqkbnr/pppp1p1p/6p1/3Pp3/8/8/PPP1PPPP/RNBQKBNR w KQkq e6 0 3");
        Assertions.assertNotEquals(BoardHasher.hashBoard(state1), BoardHasher.hashBoard(state2));
    }

    @Test
    void hashingAfterEnPassantShouldProduceDifferentValue() {
        BoardState state1 =
                new BoardState("rnbqkbnr/pppp1p1p/6p1/3Pp3/8/8/PPP1PPPP/RNBQKBNR w KQkq e6 0 3");
        BoardState state2 = Mover.makeMove(state1, MoveUtil.getMove("d5e6"));
        Assertions.assertNotEquals(BoardHasher.hashBoard(state1), BoardHasher.hashBoard(state2));
    }

    @Test
    void hashingAfterPieceMoveShouldProduceDifferentValue() {
        BoardState state1 = new BoardState(start);
        BoardState state2 = Mover.makeMove(state1, MoveUtil.getMove("d2d4"));
        Assertions.assertNotEquals(BoardHasher.hashBoard(state1), BoardHasher.hashBoard(state2));
    }

    @Test
    void hashingAfterCastlingShouldProduceDifferentValue() {
        BoardState state1 =
                new BoardState("rnb1kbnr/ppp1qpp1/3p3p/8/3P4/5N2/PPP1BPPP/RNBQK2R w KQkq - 0 6");
        BoardState state2 = Mover.makeMove(state1, MoveUtil.getMove("e1g1")); // Perform castling
        Assertions.assertNotEquals(BoardHasher.hashBoard(state1), BoardHasher.hashBoard(state2));
    }

    @Test
    void hashingAfterPromotionShouldProduceDifferentValue() {
        BoardState state1 =
                new BoardState("rnbqkbnr/pppp2P1/4p3/7p/8/8/PPPPPP1P/RNBQKBNR w KQkq - 0 5");
        BoardState state2 = Mover.makeMove(state1, MoveUtil.getMove("g7h8Q"));
        Assertions.assertNotEquals(BoardHasher.hashBoard(state1), BoardHasher.hashBoard(state2));
    }
}
