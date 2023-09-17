import com.github.zerkath.rosemary.BoardRepresentation.BoardState;
import com.github.zerkath.rosemary.DataTypes.Pieces;
import com.github.zerkath.rosemary.Evaluation.EvaluationCalculations;
import com.github.zerkath.rosemary.Main.UCI_Controller;
import java.util.Map;
import org.junit.jupiter.api.*;

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
    boardState.updatePieceCount();
    for (Map.Entry<Byte, Integer> entry : boardState.pieceMap.entrySet()) {
      System.out.println(Pieces.getChar(entry.getKey()) + " count: " + entry.getValue());
    }
    UCI_Controller uci = new UCI_Controller();
    uci.boardState = boardState;
    uci.runPerft(4, false);
    boardState.updatePieceCount();
    Map<Byte, Integer> map = uci.boardState.pieceMap;
    System.out.println("\nAfter perft");
    for (Map.Entry<Byte, Integer> entry : map.entrySet()) {
      System.out.println(Pieces.getChar(entry.getKey()) + " count: " + entry.getValue());
    }
    Assertions.assertEquals(8, map.get((byte) (Pieces.PAWN | Pieces.WHITE)));
    Assertions.assertEquals(8, map.get((byte) (Pieces.PAWN | Pieces.BLACK)));
    Assertions.assertEquals(2, map.get((byte) (Pieces.KNIGHT | Pieces.WHITE)));
    Assertions.assertEquals(2, map.get((byte) (Pieces.KNIGHT | Pieces.BLACK)));
    Assertions.assertEquals(2, map.get((byte) (Pieces.ROOK | Pieces.BLACK)));
    Assertions.assertEquals(2, map.get((byte) (Pieces.ROOK | Pieces.WHITE)));
    Assertions.assertEquals(1, map.get((byte) (Pieces.KING | Pieces.BLACK)));
    Assertions.assertEquals(1, map.get((byte) (Pieces.KING | Pieces.WHITE)));
  }
}
