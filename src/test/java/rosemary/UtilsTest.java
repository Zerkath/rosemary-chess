package rosemary;

import java.io.IOException;
import java.util.Arrays;
import org.junit.jupiter.api.*;
import rosemary.board.FenUtils;
import rosemary.types.BoardUtils;
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

    @Test
    void emptyDebugBoard() {
        try {
            byte[] test = new byte[64];
            Arrays.fill(test, (byte) 0);
            var out = BoardUtils.getString(test);
            var expected = new String(getClass()
                    .getClassLoader()
                    .getResourceAsStream("empty_debug.txt")
                    .readAllBytes());
            Assertions.assertEquals(expected, out, "Expected:\n %s \ngot:\n%s".formatted(expected, out));
        } catch (IOException ex) {
            Assertions.fail(ex);
        }
    }

    @Test
    void startDebugBoard() {
        try {
            var out = BoardUtils.getString(FenUtils.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
                    .getBoard());
            var expected = new String(getClass()
                    .getClassLoader()
                    .getResourceAsStream("start_debug.txt")
                    .readAllBytes());
            Assertions.assertEquals(expected, out, "Expected:\n %s \ngot:\n%s".formatted(expected, out));
        } catch (IOException ex) {
            Assertions.fail(ex);
        }
    }
}
