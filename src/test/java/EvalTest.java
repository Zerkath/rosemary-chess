import org.junit.jupiter.api.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EvalTest {
//    @Test
//    void equalStart() {
//        BoardState boardState = new BoardState(Utils.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"));
//        int eval = Evaluation.calculate(boardState);
//        Assertions.assertEquals(0, eval);
//    }
//    @Test
//    void noPawns() {
//        BoardState boardState = new BoardState(Utils.parseFen("rnbqkbnr/8/8/8/8/8/8/RNBQKBNR w KQkq - 0 1"));
//        int eval = Evaluation.calculate(boardState);
//        Assertions.assertEquals(0, eval);
//    }
//
//    @Test
//    void noPawnsBlack() {
//        BoardState boardState = new BoardState(Utils.parseFen("rnbqkbnr/8/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"));
//        int eval = Evaluation.calculate(boardState);
//        Assertions.assertEquals(800, eval);
//    }
//
//    @Test
//    void noPawnsWhite() {
//        BoardState boardState = new BoardState(Utils.parseFen("rnbqkbnr/pppppppp/8/8/8/8/8/RNBQKBNR w KQkq - 0 1"));
//        int eval = Evaluation.calculate(boardState);
//        Assertions.assertEquals(-800, eval);
//    } todo fix

    @Test
    void whiteMoreMiddleControl() {
        BoardState boardState = new BoardState(Utils.parseFen("rnbqkbnr/3pp3/8/8/8/3PP3/8/RNBQKBNR w KQkq - 0 1"));
        int eval = Evaluation.calculate(boardState);
        Assertions.assertTrue(eval > 0);
    }

    @Test
    void blackMoreMiddleControl() {
        BoardState boardState = new BoardState(Utils.parseFen("rnbqkbnr/8/3pp3/8/8/8/3PP3/RNBQKBNR w KQkq - 0 1"));
        int eval = Evaluation.calculate(boardState);
        Assertions.assertTrue(eval < 0);
    }
}
