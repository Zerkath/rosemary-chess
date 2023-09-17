
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
        Assertions.assertEquals(expected.getColumn(), result.getColumn());
        Assertions.assertEquals(expected.getRow(), result.getRow());
    }

    @Test
    void move() {
        Move expected = new Move(new Coordinate(4, 4).coord, new Coordinate(3, 4).coord);
        Move result = new Move("e4e5");
        Assertions.assertEquals(new Coordinate(expected.getDestination()).getColumn(), new Coordinate(result.getDestination()).getColumn());
        Assertions.assertEquals(new Coordinate(expected.getDestination()).getRow(), new Coordinate(result.getDestination()).getRow());
        Assertions.assertEquals(new Coordinate(expected.getOrigin()).getColumn(), new Coordinate(result.getOrigin()).getColumn());
        Assertions.assertEquals(new Coordinate(expected.getOrigin()).getRow(), new Coordinate(result.getOrigin()).getRow());
    }

    @Test
    void promotionCommand() {
        Move expected = new Move(new Move(new Coordinate(1, 7).coord, new Coordinate(0, 7).coord), (Pieces.KNIGHT | Pieces.WHITE));
        Assertions.assertEquals(expected.promotion, new Move("h7h8N").promotion);
    }
}
