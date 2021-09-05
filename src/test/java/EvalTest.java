import BoardRepresentation.BoardState;
import CommonTools.Utils;
import org.junit.jupiter.api.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EvalTest {

//    private Evaluation
//
//    @Test
//    void equalStart() {
//        BoardRepresentation.BoardState boardState = new BoardRepresentation.BoardState(CommonTools.Utils.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"));
//        int eval = Evaluation.Evaluation.calculate(boardState);
//        Assertions.assertEquals(0, eval);
//    }
//    @Test
//    void noPawns() {
//        BoardRepresentation.BoardState boardState = new BoardRepresentation.BoardState(CommonTools.Utils.parseFen("rnbqkbnr/8/8/8/8/8/8/RNBQKBNR w KQkq - 0 1"));
//        int eval = Evaluation.Evaluation.calculate(boardState);
//        Assertions.assertEquals(0, eval);
//    }
//
//    @Test
//    void noPawnsBlack() {
//        BoardRepresentation.BoardState boardState = new BoardRepresentation.BoardState(CommonTools.Utils.parseFen("rnbqkbnr/8/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"));
//        int eval = Evaluation.Evaluation.calculate(boardState);
//        Assertions.assertEquals(800, eval);
//    }
//
//    @Test
//    void noPawnsWhite() {
//        BoardRepresentation.BoardState boardState = new BoardRepresentation.BoardState(CommonTools.Utils.parseFen("rnbqkbnr/pppppppp/8/8/8/8/8/RNBQKBNR w KQkq - 0 1"));
//        int eval = Evaluation.calculate(boardState);
//        Assertions.assertEquals(-800, eval);
//    }
//
//    @Test
//    void whiteMoreMiddleControl() {
//        BoardState boardState = new BoardState(Utils.parseFen("rnbqkbnr/3pp3/8/8/8/3PP3/8/RNBQKBNR w KQkq - 0 1"));
//        int eval = eval.calculate(boardState);
//        Assertions.assertTrue(eval > 0);
//    }
//
//    @Test
//    void blackMoreMiddleControl() {
//        BoardState boardState = new BoardState(Utils.parseFen("rnbqkbnr/8/3pp3/8/8/8/3PP3/RNBQKBNR w KQkq - 0 1"));
//        int eval = eval.calculate(boardState);
//        Assertions.assertTrue(eval < 0);
//    }
}
