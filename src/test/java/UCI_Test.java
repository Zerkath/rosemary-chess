import org.junit.jupiter.api.*;
public class UCI_Test {
    UCI_Controller uci = new UCI_Controller();

    @Test
    void castling() {
        String pgn = "position startpos moves d2d4 e7e5 d4e5 d7d6 e5d6 c7d6 d1d6 f8d6 h2h3 d6e5 b1c3 e5c3 b2c3 d8a5 c1d2 a5a3 e2e3 c8f5 f1d3 f5d3 c2d3 g8f6 d3d4 f6e4 g1e2 e8g8 e1g1 e4d2 f1e1";
        uci.handleMessage(pgn);
        Assertions.assertEquals("rn3rk1/pp3ppp/8/8/3P4/q1P1P2P/P2nNPP1/R3R1K1 b - - 1 15", uci.game.toFenString());
    }

    @Test
    void noCastling() {
        String pgn = "position startpos moves d2d4 e7e5 d4e5 d7d6 e5d6 c7d6 d1d6 f8d6 h2h3 d6e5 b1c3 e5c3 b2c3 d8a5 c1d2 a5a3 e2e3 c8f5 f1d3 f5d3 c2d3 g8f6 d3d4 f6e4 g1e2";
        uci.handleMessage(pgn);
        Assertions.assertEquals("rn2k2r/pp3ppp/8/8/3Pn3/q1P1P2P/P2BNPP1/R3K2R b KQkq - 2 13", uci.game.toFenString());
    }
}
