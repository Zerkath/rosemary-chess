import BoardRepresentation.BoardState;
import DataTypes.CastlingRights;
import CommonTools.Utils;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FenParseTest {
    @Test
    void startingBoard() {
        BoardState state = new BoardState(Utils.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"));
        Assertions.assertEquals(CastlingRights.BOTH, state.board.getBlackCastling());
        Assertions.assertEquals(CastlingRights.BOTH, state.board.getWhiteCastling());
    }

    @Test
    void noWhiteQueenSide() {
        BoardState state = Utils.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w Kkq - 0 1");
        Utils.printBoard(state);
        Assertions.assertEquals(CastlingRights.BOTH, state.board.getBlackCastling());
        Assertions.assertEquals(CastlingRights.KING, state.board.getWhiteCastling());
    }

    @Test
    void noBlackQueenSide() {
        BoardState state = Utils.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQk - 0 1");
        Utils.printBoard(state);
        Assertions.assertEquals(CastlingRights.KING, state.board.getBlackCastling());
        Assertions.assertEquals(CastlingRights.BOTH, state.board.getWhiteCastling());
    }

    @Test
    void noCastlingRights() {
        BoardState state = Utils.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1");
        Utils.printBoard(state);
        Assertions.assertEquals(CastlingRights.NONE, state.board.getBlackCastling());
        Assertions.assertEquals(CastlingRights.NONE, state.board.getWhiteCastling());
    }

    @Test
    void correctAmountOfPieces() {

        BoardState state = Utils.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        int [] countedPieces = state.countPieces();
        Utils.printBoard(state);
        Assertions.assertEquals(16, countedPieces[0]);
        Assertions.assertEquals(16, countedPieces[1]);
        Assertions.assertEquals(32, countedPieces[2]);
    }
    @Test
    void noBlackPieces() {

        BoardState state = Utils.parseFen("8/8/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        int [] countedPieces = state.countPieces();
        Utils.printBoard(state);
        Assertions.assertEquals(16, countedPieces[0]);
        Assertions.assertEquals(0, countedPieces[1]);
    }

    @Test
    void noWhitePieces() {

        BoardState state = Utils.parseFen("rnbqkbnr/pppppppp/8/8/8/8/8/8 w KQkq - 0 1");
        int [] countedPieces = state.countPieces();
        Utils.printBoard(state);
        Assertions.assertEquals(0, countedPieces[0]);
        Assertions.assertEquals(16, countedPieces[1]);
    }

    @Test
    void weirdBoardPosition() {
        //https://lichess.org/editor/q3k3/2P3P1/6q1/3P1P2/1P1q4/3B1N2/P2B2N1/1R2K2R_w_K_-_0_1
        BoardState state = Utils.parseFen("q3k3/2P3P1/6q1/3P1P2/1P1q4/3B1N2/P2B2N1/1R2K2R w K - 0 1");
        int [] countedPieces = state.countPieces();
        Utils.printBoard(state);
        Assertions.assertEquals(3, countedPieces[12]); //black queens
        Assertions.assertEquals(6, countedPieces[3]); //white pawns
        Assertions.assertEquals(2, countedPieces[9]); //white knights
        Assertions.assertEquals(2, countedPieces[5]); //white bishops

        Assertions.assertEquals(1, countedPieces[13]); //kings
        Assertions.assertEquals(1, countedPieces[14]);

    }

    @Test
    void printFen() {
        String fenString = "q3k3/2P3P1/6q1/3P1P2/1P1q4/3B1N2/P2B2N1/1R2K2R w K - 0 1";
        BoardState state = Utils.parseFen(fenString);
        Assertions.assertEquals(fenString, Utils.toFenString(state));

        fenString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQk - 0 1";
        state = Utils.parseFen(fenString);
        Assertions.assertEquals(fenString, Utils.toFenString(state));
    }

    @Test
    void enPassant() {
        String fenString = "7k/2pppppp/p7/Pp6/8/8/1PPPPPPP/7K w - b6 0 3";
        BoardState state = Utils.parseFen(fenString);
        Assertions.assertNotNull(state.enPassant);
        Assertions.assertEquals(2, state.enPassant.row); //row
        Assertions.assertEquals(1, state.enPassant.column); //col
        Assertions.assertEquals(fenString, Utils.toFenString(state));
    }
    @Test
    void enPassantTwo() {
        String fenString = "7k/p1pppppp/8/P7/1pP5/8/1P1PPPPP/7K b - c3 0 3";
        BoardState boardState = Utils.parseFen(fenString);
        Assertions.assertNotNull(boardState.enPassant);
        Assertions.assertEquals(5, boardState.enPassant.row); //row
        Assertions.assertEquals(2, boardState.enPassant.column); //col
        Assertions.assertEquals(fenString, Utils.toFenString(boardState));
    }
}
