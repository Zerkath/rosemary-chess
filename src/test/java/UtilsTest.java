import com.github.zerkath.rosemary.types.MoveUtil;
import com.github.zerkath.rosemary.types.Pieces;
import com.github.zerkath.rosemary.types.Utils;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UtilsTest {

  @Test
  void coordinate() {
    short expected = Utils.getCoordinate(4, 4);
    short result = Utils.getCoordinate("e4");
    Assertions.assertEquals(MoveUtil.getColumn(expected), MoveUtil.getColumn(result));
    Assertions.assertEquals(MoveUtil.getRow(expected), MoveUtil.getRow(result));
  }

  @Test
  void move() {
    // Move expected = new Move(new Coordinate(4, 4).coord, new Coordinate(3, 4).coord);
    // Move result = MoveUtil.getMove("e4e5");
    // Assertions.assertEquals(
    //     new Coordinate(MoveUtil.getDestination(expected)).getColumn(),
    //     new Coordinate(MoveUtil.getDestination(result)).getColumn());
    // Assertions.assertEquals(
    //     new Coordinate(MoveUtil.getDestination(expected)).getRow(),
    //     new Coordinate(MoveUtil.getDestination(result)).getRow());
    // Assertions.assertEquals(
    //     MoveUtil.getOrigin(expected)).getColumn(),
    //     MoveUtil.getOrigin(result)).getColumn());
    // Assertions.assertEquals(
    //     MoveUtil.getOrigin(expected)).getRow(), MoveUtil.getOrigin(result)).getRow());
  }

  @Test
  void promotionCommand() {
    short expected =
        MoveUtil.getMove(
            MoveUtil.getMove(Utils.getCoordinate(1, 7), Utils.getCoordinate(0, 7)),
            Pieces.KNIGHT,
            true);
    Assertions.assertEquals(expected, MoveUtil.getMove("h7h8N"));
  }
}
