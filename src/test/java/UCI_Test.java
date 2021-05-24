import org.junit.jupiter.api.*;
public class UCI_Test {
    UCI_Controller uci = new UCI_Controller();

    @Test
    void kingSideCastling() {
        String pgn = "position startpos moves d2d4 e7e5 d4e5 d7d6 e5d6 c7d6 d1d6 f8d6 h2h3 d6e5 b1c3 e5c3 b2c3 d8a5 c1d2 a5a3 e2e3 c8f5 f1d3 f5d3 c2d3 g8f6 d3d4 f6e4 g1e2 e8g8 e1g1 e4d2 f1e1";
        uci.handleMessage(pgn);
        Assertions.assertEquals("rn3rk1/pp3ppp/8/8/3P4/q1P1P2P/P2nNPP1/R3R1K1 b - - 1 15", uci.getFen());
    }

    @Test
    void queenSideCastling() {
        String pgn = "position startpos moves b2b3 b7b6 c1b2 c8b7 b1c3 b8c6 e2e3 e7e6 d1e2 d8e7 e1c1 e8c8";
        uci.handleMessage(pgn);
        Assertions.assertEquals("2kr1bnr/pbppqppp/1pn1p3/8/8/1PN1P3/PBPPQPPP/2KR1BNR w - - 4 7", uci.getFen());
    }

    @Test
    void noCastling() {
        String pgn = "position startpos moves e2e4 e7e5 e1e2";
        uci.handleMessage(pgn);
        Assertions.assertEquals("rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPPKPPP/RNBQ1BNR b kq - 1 2", uci.getFen());
    }

    @Test
    void enPassantWhite() {
        String pgn = "position startpos moves d2d4 h7h6 d4d5 e7e5";
        uci.handleMessage(pgn);
        Assertions.assertEquals("rnbqkbnr/pppp1pp1/7p/3Pp3/8/8/PPP1PPPP/RNBQKBNR w KQkq e6 0 3", uci.getFen());

    }

    @Test
    void enPassantBlack() {
        String pgn = "position startpos moves h2h3 d7d5 g2g3 d5d4 e2e4";
        uci.handleMessage(pgn);
        Assertions.assertEquals("rnbqkbnr/ppp1pppp/8/8/3pP3/6PP/PPPP1P2/RNBQKBNR b KQkq e3 0 3", uci.getFen());
    }

    @Test
    void castlingRights() {
        String pgn = "position startpos moves a2a3 a7a6 h2h3 h7h6"; //all pawns in front of rooks moved up by 1
        uci.handleMessage(pgn);
        Assertions.assertEquals("rnbqkbnr/1pppppp1/p6p/8/8/P6P/1PPPPPP1/RNBQKBNR w KQkq - 0 3", uci.getFen());
        uci.handleMessage(pgn + " a1a2"); //white queenside
        Assertions.assertEquals("rnbqkbnr/1pppppp1/p6p/8/8/P6P/RPPPPPP1/1NBQKBNR b Kkq - 1 3", uci.getFen());
        uci.handleMessage(pgn + " h1h2"); //white kingside
        Assertions.assertEquals("rnbqkbnr/1pppppp1/p6p/8/8/P6P/1PPPPPPR/RNBQKBN1 b Qkq - 1 3", uci.getFen());
        uci.handleMessage(pgn + " a1a2 a8a7"); //white and black queenside
        Assertions.assertEquals("1nbqkbnr/rpppppp1/p6p/8/8/P6P/RPPPPPP1/1NBQKBNR w Kk - 2 4", uci.getFen());
        uci.handleMessage(pgn + " a1a2 a8a7 h1h2 h8h7"); //no castling
        Assertions.assertEquals("1nbqkbn1/rppppppr/p6p/8/8/P6P/RPPPPPPR/1NBQKBN1 w - - 4 5", uci.getFen());

    }

    @Test
    void blackQueensideRookCaptured() {
        String pgn = "position startpos moves g2g3 b7b6 f1g2 d7d6 g2a8";//bishop capture
        uci.handleMessage(pgn);
        Assertions.assertEquals("Bnbqkbnr/p1p1pppp/1p1p4/8/8/6P1/PPPPPP1P/RNBQK1NR b KQk - 0 3", uci.getFen());
    }

    @Test
    void leftEdgeEnPassant() {
        String pgn = "position startpos moves c2c4 b7b5 c4b5 a7a5";
        uci.handleMessage(pgn);
        Assertions.assertEquals("rnbqkbnr/2pppppp/8/pP6/8/8/PP1PPPPP/RNBQKBNR w KQkq a6 0 3", uci.getFen());
    }

    @Test
    void castlingRightsKingMoved() {
        String pgn = "position fen \"r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1\" moves f3h3 e8f8 h3h8";
        uci.handleMessage(pgn);
        Assertions.assertEquals("r4k1Q/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N5/PPPBBPPP/R3K2R b KQ - 0 2", uci.getFen());
    }
}