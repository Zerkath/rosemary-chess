import BoardRepresentation.BoardState;

import DataTypes.Pieces;
import Evaluation.EvaluationCalculations;
import Main.UCI_Controller;
import org.junit.jupiter.api.*;

import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EvalTest {

    private final EvaluationCalculations evaluationCalculations = new EvaluationCalculations();

    @Test
    void equalStart() {
        BoardState boardState = new BoardRepresentation.BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        int eval = evaluationCalculations.calculateMaterial(boardState);
        Assertions.assertEquals(0, eval);
    }
    @Test
    void noPawns() {
        BoardState boardState = new BoardRepresentation.BoardState("rnbqkbnr/8/8/8/8/8/8/RNBQKBNR w KQkq - 0 1");
        int eval = evaluationCalculations.calculateMaterial(boardState);
        Assertions.assertEquals(0, eval);
    }

    @Test
    void noPawnsBlack() {
        BoardState boardState = new BoardRepresentation.BoardState("rnbqkbnr/8/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        int eval = evaluationCalculations.calculateMaterial(boardState);
        Assertions.assertEquals(800, eval);
    }

    @Test
    void noPawnsWhite() {
        BoardState boardState = new BoardRepresentation.BoardState("rnbqkbnr/pppppppp/8/8/8/8/8/RNBQKBNR w KQkq - 0 1");
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
        BoardState boardState = new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        System.out.println("Start");
        boardState.updatePieceCount();
        for (Map.Entry<Integer, Integer> entry: boardState.pieceMap.entrySet()) {
            System.out.println(Pieces.getChar(entry.getKey()) + " count: " + entry.getValue());
        }
        UCI_Controller uci = new UCI_Controller();
        uci.boardState = boardState;
        uci.runPerft(4, false);
        boardState.updatePieceCount();
        Map<Integer, Integer> map = uci.boardState.pieceMap;
        System.out.println("\nAfter perft");
        for (Map.Entry<Integer, Integer> entry: map.entrySet()) {
            System.out.println(Pieces.getChar(entry.getKey()) + " count: " + entry.getValue());
        }
        Assertions.assertEquals(8, map.get(Pieces.PAWN | Pieces.WHITE));
        Assertions.assertEquals(8, map.get(Pieces.PAWN | Pieces.BLACK));
        Assertions.assertEquals(2, map.get(Pieces.KNIGHT | Pieces.WHITE));
        Assertions.assertEquals(2, map.get(Pieces.KNIGHT | Pieces.BLACK));
        Assertions.assertEquals(2, map.get(Pieces.ROOK | Pieces.BLACK));
        Assertions.assertEquals(2, map.get(Pieces.ROOK | Pieces.WHITE));
        Assertions.assertEquals(1, map.get(Pieces.KING | Pieces.BLACK));
        Assertions.assertEquals(1, map.get(Pieces.KING | Pieces.WHITE));
    }
}
