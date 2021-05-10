import org.junit.jupiter.api.*;
public class FenParseTests {
    Game game = new Game();

    @Test
    void startingBoard() {
        game.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Assertions.assertEquals(Game.CastlingRights.BOTH, game.blackCastling);
        Assertions.assertEquals(Game.CastlingRights.BOTH, game.whiteCastling);
    }

    @Test
    void noWhiteQueenSide() {
        game.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w Kkq - 0 1");
        Assertions.assertEquals(Game.CastlingRights.BOTH, game.blackCastling);
        Assertions.assertEquals(Game.CastlingRights.KINGSIDE, game.whiteCastling);
    }

    @Test
    void noBlackQueenSide() {
        game.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQk - 0 1");
        Assertions.assertEquals(Game.CastlingRights.KINGSIDE, game.blackCastling);
        Assertions.assertEquals(Game.CastlingRights.BOTH, game.whiteCastling);
    }

    @Test
    void noCastlingRights() {
        game.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1");
        Assertions.assertEquals(Game.CastlingRights.NONE, game.blackCastling);
        Assertions.assertEquals(Game.CastlingRights.NONE, game.whiteCastling);
    }
}
