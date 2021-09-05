import CommonTools.Utils;
import DataTypes.Coordinate;
import org.junit.jupiter.api.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UtilsTest {
    @Test
    void characters() {
        for (int i = 0; i < 8; i++) {
            char c = (char)(i+'a');
            Assertions.assertEquals(c, Utils.toColumnCharacter(Utils.toColumnNumber(c)));
        }
    }

    @Test
    void coordinate() {
        Coordinate expected = new Coordinate(4, 4);
        Coordinate result = Utils.parseCoordinate("e4");
        Assertions.assertEquals(expected.column, result.column);
        Assertions.assertEquals(expected.row, result.row);
    }
}
