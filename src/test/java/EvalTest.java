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

        byte[] pieceMap = boardState.getPieceMap();
        for (int i = 0; i < 23; i++) {
            System.out.println(Pieces.getChar(i) + " count: " + pieceMap[i]);
        }
        UciController uci = new UciController();
        uci.boardState = boardState;
        uci.runPerft(2, false);
        byte[] map = uci.boardState.getPieceMap();
        System.out.println("\nAfter perft");
        for (int i = 0; i < 23; i++) {
            System.out.println(Pieces.getChar(i) + " count: " + map[i]);
        }
        Assertions.assertEquals(8, (int) map[(byte) (Pieces.PAWN | Pieces.WHITE)]);
        Assertions.assertEquals(8, (int) map[(byte) (Pieces.PAWN | Pieces.BLACK)]);
        Assertions.assertEquals(2, (int) map[(byte) (Pieces.KNIGHT | Pieces.WHITE)]);
        Assertions.assertEquals(2, (int) map[(byte) (Pieces.KNIGHT | Pieces.BLACK)]);
        Assertions.assertEquals(2, (int) map[(byte) (Pieces.ROOK | Pieces.BLACK)]);
        Assertions.assertEquals(2, (int) map[(byte) (Pieces.ROOK | Pieces.WHITE)]);
        Assertions.assertEquals(1, (int) map[(byte) (Pieces.KING | Pieces.BLACK)]);
        Assertions.assertEquals(1, (int) map[(byte) (Pieces.KING | Pieces.WHITE)]);
    }
}
