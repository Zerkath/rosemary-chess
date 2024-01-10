import java.util.Map;
import org.junit.jupiter.api.*;
import rosemary.UciController;
import rosemary.board.BoardState;
import rosemary.eval.*;
import rosemary.types.Pieces;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EvalTest {

    private final EvaluationCalculations evaluationCalculations = new EvaluationCalculations();

    @Test
    void equalStart() {
        BoardState boardState =
                new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        int eval = evaluationCalculations.calculateMaterial(boardState);
        Assertions.assertEquals(0, eval);
    }

    @Test
    void noPawns() {
        BoardState boardState = new BoardState("rnbqkbnr/8/8/8/8/8/8/RNBQKBNR w KQkq - 0 1");
        int eval = evaluationCalculations.calculateMaterial(boardState);
        Assertions.assertEquals(0, eval);
    }

    @Test
    void noPawnsBlack() {
        BoardState boardState = new BoardState("rnbqkbnr/8/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        int eval = evaluationCalculations.calculateMaterial(boardState);
        Assertions.assertEquals(800, eval);
    }

    @Test
    void noPawnsWhite() {
        BoardState boardState = new BoardState("rnbqkbnr/pppppppp/8/8/8/8/8/RNBQKBNR w KQkq - 0 1");
        int eval = evaluationCalculations.calculateMaterial(boardState);
        Assertions.assertEquals(-800, eval);
    }

    @Test
    void whiteMoreMiddleControl() {
        BoardState boardState = new BoardState("rnbqkbnr/3pp3/8/8/8/3PP3/8/RNBQKBNR w KQkq - 0 1");
        int eval = evaluationCalculations.calculateMaterial(boardState);
        Assertions.assertTrue(eval > 0);
    }

    @Test
    void blackMoreMiddleControl() {
        BoardState boardState = new BoardState("rnbqkbnr/8/3pp3/8/8/8/3PP3/RNBQKBNR w KQkq - 0 1");
        int eval = evaluationCalculations.calculateMaterial(boardState);
        Assertions.assertTrue(eval < 0);
    }

    @Test
    void pieceCountsMatch() {
        BoardState boardState =
                new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        System.out.println("Start");
        for (Map.Entry<Byte, Byte> entry : boardState.getPieceMap().entrySet()) {
            System.out.println(Pieces.getChar(entry.getKey()) + " count: " + entry.getValue());
        }
        UciController uci = new UciController();
        uci.boardState = boardState;
        uci.runPerft(4, false);
        Map<Byte, Byte> map = uci.boardState.getPieceMap();
        System.out.println("\nAfter perft");
        for (Map.Entry<Byte, Byte> entry : map.entrySet()) {
            System.out.println(Pieces.getChar(entry.getKey()) + " count: " + entry.getValue());
        }
        Assertions.assertEquals(8, (int) map.get((byte) (Pieces.PAWN | Pieces.WHITE)));
        Assertions.assertEquals(8, (int) map.get((byte) (Pieces.PAWN | Pieces.BLACK)));
        Assertions.assertEquals(2, (int) map.get((byte) (Pieces.KNIGHT | Pieces.WHITE)));
        Assertions.assertEquals(2, (int) map.get((byte) (Pieces.KNIGHT | Pieces.BLACK)));
        Assertions.assertEquals(2, (int) map.get((byte) (Pieces.ROOK | Pieces.BLACK)));
        Assertions.assertEquals(2, (int) map.get((byte) (Pieces.ROOK | Pieces.WHITE)));
        Assertions.assertEquals(1, (int) map.get((byte) (Pieces.KING | Pieces.BLACK)));
        Assertions.assertEquals(1, (int) map.get((byte) (Pieces.KING | Pieces.WHITE)));
    }
}
