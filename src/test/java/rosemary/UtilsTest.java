package rosemary;

import org.junit.jupiter.api.*;
import rosemary.types.MoveUtil;
import rosemary.types.Pieces;
import rosemary.types.Utils;

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
    void promotionCommand() {
        short expected = MoveUtil.getMove(
                MoveUtil.getMove(Utils.getCoordinate(1, 7), Utils.getCoordinate(0, 7)), Pieces.KNIGHT, true);
        Assertions.assertEquals(expected, MoveUtil.getMove("h7h8N"));
    }
}
