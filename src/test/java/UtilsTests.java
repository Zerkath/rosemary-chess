import org.junit.jupiter.api.*;
public class UtilsTests {
    Utils utils = new Utils();

    @Test
    void characters() {
        for (int i = 0; i < 8; i++) {
            char c = (char)(i+'a');
            Assertions.assertEquals(c, utils.toColumnCharacter(utils.toColumnNumber(c)));
        }
    }
}
