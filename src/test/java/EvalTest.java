import BoardRepresentation.BoardState;

import Evaluation.EvaluationCalculations;
import org.junit.jupiter.api.*;
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
}
