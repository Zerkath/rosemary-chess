
import com.github.zerkath.rosemary.DataTypes.Coordinate;
import com.github.zerkath.rosemary.DataTypes.Move;
import com.github.zerkath.rosemary.DataTypes.Pieces;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UtilsTest {

    @Test
    void coordinate() {
        Coordinate expected = new Coordinate(4, 4);
        Coordinate result = new Coordinate("e4");
        Assertions.assertEquals(expected.column, result.column);
        Assertions.assertEquals(expected.row, result.row);
    }

    @Test
    void move() {
        Move expected = new Move(new Coordinate(4, 4), new Coordinate(3, 4));
        Move result = new Move("e4e5");
        Assertions.assertEquals(expected.destination.column, result.destination.column);
        Assertions.assertEquals(expected.destination.row, result.destination.row);
        Assertions.assertEquals(expected.origin.column, result.origin.column);
        Assertions.assertEquals(expected.origin.row, result.origin.row);
    }

    @Test
    void promotionCommand() {
        Move expected = new Move(new Move(new Coordinate(1, 7), new Coordinate(0, 7)), (Pieces.KNIGHT | Pieces.WHITE));
        Assertions.assertEquals(expected.promotion, new Move("h7h8N").promotion);
    }
}
