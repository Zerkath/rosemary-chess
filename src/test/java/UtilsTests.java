import org.junit.jupiter.api.*;
public class UtilsTests {
    @Test
    void characters() {
        for (int i = 0; i < 8; i++) {
            char c = (char)(i+'a');
            Assertions.assertEquals(c, Utils.toColumnCharacter(Utils.toColumnNumber(c)));
        }
    }
}
