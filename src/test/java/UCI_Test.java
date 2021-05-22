import org.junit.jupiter.api.*;
public class UCI_Test {
    UCI_Controller uci = new UCI_Controller();

    @Test
    void kingSideCastling() {
        String pgn = "position startpos moves d2d4 e7e5 d4e5 d7d6 e5d6 c7d6 d1d6 f8d6 h2h3 d6e5 b1c3 e5c3 b2c3 d8a5 c1d2 a5a3 e2e3 c8f5 f1d3 f5d3 c2d3 g8f6 d3d4 f6e4 g1e2 e8g8 e1g1 e4d2 f1e1";
        uci.handleMessage(pgn);
        Assertions.assertEquals("rn3rk1/pp3ppp/8/8/3P4/q1P1P2P/P2nNPP1/R3R1K1 b - - 1 15", uci.game.toFenString());
    }

    @Test
    void queenSideCastling() {
        String pgn = "position startpos moves b2b3 b7b6 c1b2 c8b7 b1c3 b8c6 e2e3 e7e6 d1e2 d8e7 e1c1 e8c8";
        uci.handleMessage(pgn);
        Assertions.assertEquals("2kr1bnr/pbppqppp/1pn1p3/8/8/1PN1P3/PBPPQPPP/2KR1BNR w - - 4 7", uci.game.toFenString());
    }

    @Test
    void noCastling() {
        String pgn = "position startpos moves e2e4 e7e5 e1e2";
        uci.handleMessage(pgn);
        Assertions.assertEquals("rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPPKPPP/RNBQ1BNR b kq - 1 2", uci.game.toFenString());
    }
}
