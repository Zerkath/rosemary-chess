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
}
