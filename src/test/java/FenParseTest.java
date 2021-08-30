import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FenParseTest {
    BoardState game = new BoardState();
    @Test
    void startingBoard() {
        BoardState state = new BoardState(Utils.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"));
        Assertions.assertEquals(CastlingRights.BOTH, state.blackCastling);
        Assertions.assertEquals(CastlingRights.BOTH, state.whiteCastling);
    }
//
//    @Test
//    void noWhiteQueenSide() {
//        game.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w Kkq - 0 1");
//        game.printBoard();
//        Assertions.assertEquals(Game.CastlingRights.BOTH, game.blackCastling);
//        Assertions.assertEquals(Game.CastlingRights.KINGSIDE, game.whiteCastling);
//    }
//
//    @Test
//    void noBlackQueenSide() {
//        game.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQk - 0 1");
//        game.printBoard();
//        Assertions.assertEquals(Game.CastlingRights.KINGSIDE, game.blackCastling);
//        Assertions.assertEquals(Game.CastlingRights.BOTH, game.whiteCastling);
//    }
//
//    @Test
//    void noCastlingRights() {
//        game.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1");
//        game.printBoard();
//        Assertions.assertEquals(Game.CastlingRights.NONE, game.blackCastling);
//        Assertions.assertEquals(Game.CastlingRights.NONE, game.whiteCastling);
//    }
//
//    @Test
//    void correctAmountOfPieces() {
//
//        game.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
//        int [] countedPieces = game.countPieces();
//        game.printBoard();
//        Assertions.assertEquals(16, countedPieces[0]);
//        Assertions.assertEquals(16, countedPieces[1]);
//        Assertions.assertEquals(32, countedPieces[2]);
//    }
//    @Test
//    void noBlackPieces() {
//
//        game.parseFen("8/8/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
//        int [] countedPieces = game.countPieces();
//        game.printBoard();
//        Assertions.assertEquals(16, countedPieces[0]);
//        Assertions.assertEquals(0, countedPieces[1]);
//    }
//
//    @Test
//    void noWhitePieces() {
//
//        game.parseFen("rnbqkbnr/pppppppp/8/8/8/8/8/8 w KQkq - 0 1");
//        int [] countedPieces = game.countPieces();
//        game.printBoard();
//        Assertions.assertEquals(0, countedPieces[0]);
//        Assertions.assertEquals(16, countedPieces[1]);
//    }
//
//    @Test
//    void weirdBoardPosition() {
//        //https://lichess.org/editor/q3k3/2P3P1/6q1/3P1P2/1P1q4/3B1N2/P2B2N1/1R2K2R_w_K_-_0_1
//        game.parseFen("q3k3/2P3P1/6q1/3P1P2/1P1q4/3B1N2/P2B2N1/1R2K2R w K - 0 1");
//        int [] countedPieces = game.countPieces();
//        game.printBoard();
//        Assertions.assertEquals(3, countedPieces[12]); //black queens
//        Assertions.assertEquals(6, countedPieces[3]); //white pawns
//        Assertions.assertEquals(2, countedPieces[9]); //white knights
//        Assertions.assertEquals(2, countedPieces[5]); //white bishops
//
//        Assertions.assertEquals(1, countedPieces[13]); //kings
//        Assertions.assertEquals(1, countedPieces[14]);
//
//    }
//
//    @Test
//    void printFen() {
//        String fenString = "q3k3/2P3P1/6q1/3P1P2/1P1q4/3B1N2/P2B2N1/1R2K2R w K - 0 1";
//        game.parseFen(fenString);
//        Assertions.assertEquals(fenString, game.toFenString());
//
//        fenString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQk - 0 1";
//        game.parseFen(fenString);
//        Assertions.assertEquals(fenString, game.toFenString());
//    }
//
    @Test
    void enPassant() {
        String fenString = "7k/2pppppp/p7/Pp6/8/8/1PPPPPPP/7K w - b6 0 3";
        BoardState state = Utils.parseFen(fenString);
        Assertions.assertNotNull(state.enPassant);
        Assertions.assertEquals(2, state.enPassant.row); //row
        Assertions.assertEquals(1, state.enPassant.column); //col
        Assertions.assertEquals(fenString, Utils.toFenString(state));
    }
//    @Test
//    void enPassantTwo() {
//        String fenString = "7k/p1pppppp/8/P7/1pP5/8/1P1PPPPP/7K b - c3 0 3";
//        game.parseFen(fenString);
//        Assertions.assertNotNull(game.enPassant);
//        Assertions.assertEquals(5, game.enPassant[0]); //row
//        Assertions.assertEquals(2, game.enPassant[1]); //col
//        Assertions.assertEquals(fenString, game.toFenString());
//    }
}
