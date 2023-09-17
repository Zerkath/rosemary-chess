import com.github.zerkath.rosemary.BoardRepresentation.BoardState;
import com.github.zerkath.rosemary.DataTypes.CastlingRights;
import com.github.zerkath.rosemary.DataTypes.*;

import com.github.zerkath.rosemary.DataTypes.Pieces;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FenParseTest {

    private final String start = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    @Test
    void startingBoard() {
        BoardState state = new BoardState(start);
        Assertions.assertEquals(CastlingRights.BOTH, state.board.getBlackCastling());
        Assertions.assertEquals(CastlingRights.BOTH, state.board.getWhiteCastling());
    }

    @Test
    void startingBoardNotModifiedWhenStoredInternally() {
        BoardState state = new BoardState(start);
        Assertions.assertEquals(start, state.toFenString());
    }

    @Test
    void getRow() {
        BoardState state = new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Assertions.assertEquals(8, state.board.getRow(7).length);
    }

    @Test
    void noWhiteQueenSide() {
        BoardState state = new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w Kkq - 0 1");
        state.printBoard();
        Assertions.assertEquals(CastlingRights.BOTH, state.board.getBlackCastling());
        Assertions.assertEquals(CastlingRights.KING, state.board.getWhiteCastling());
    }

    @Test
    void noBlackQueenSide() {
        BoardState state = new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQk - 0 1");
        state.printBoard();
        Assertions.assertEquals(CastlingRights.KING, state.board.getBlackCastling());
        Assertions.assertEquals(CastlingRights.BOTH, state.board.getWhiteCastling());
    }

    @Test
    void noCastlingRights() {
        BoardState state = new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1");
        state.printBoard();
        Assertions.assertEquals(CastlingRights.NONE, state.board.getBlackCastling());
        Assertions.assertEquals(CastlingRights.NONE, state.board.getWhiteCastling());
    }

    @Test
    void correctAmountOfPieces() {

        BoardState state = new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        state.printBoard();
        state.updatePieceCount();
        Assertions.assertEquals(8, state.pieceMap.get(Pieces.getNum('p')));
        Assertions.assertEquals(8, state.pieceMap.get(Pieces.getNum('P')));
        Assertions.assertEquals(2, state.pieceMap.get(Pieces.getNum('r')));
        Assertions.assertEquals(2, state.pieceMap.get(Pieces.getNum('R')));
        Assertions.assertEquals(1, state.pieceMap.get(Pieces.getNum('q')));
        Assertions.assertEquals(1, state.pieceMap.get(Pieces.getNum('Q')));
        Assertions.assertEquals(1, state.pieceMap.get(Pieces.getNum('k')));
    }
//    @Test
//    void noBlackPieces() {
//
//        BoardState state = new BoardState("8/8/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
//        int [] countedPieces = state.countPieces();
//        state.printBoard();
//        Assertions.assertEquals(16, countedPieces[0]);
//        Assertions.assertEquals(0, countedPieces[1]);
//    }
//
//    @Test
//    void noWhitePieces() {
//
//        BoardState state = new BoardState("rnbqkbnr/pppppppp/8/8/8/8/8/8 w KQkq - 0 1");
//        int [] countedPieces = state.countPieces();
//        state.printBoard();
//        Assertions.assertEquals(0, countedPieces[0]);
//        Assertions.assertEquals(16, countedPieces[1]);
//    }
//
//    @Test
//    void weirdBoardPosition() {
//        //https://lichess.org/editor/q3k3/2P3P1/6q1/3P1P2/1P1q4/3B1N2/P2B2N1/1R2K2R_w_K_-_0_1
//        BoardState state = new BoardState("q3k3/2P3P1/6q1/3P1P2/1P1q4/3B1N2/P2B2N1/1R2K2R w K - 0 1");
//        int [] countedPieces = state.countPieces();
//        state.printBoard();
//        Assertions.assertEquals(3, countedPieces[12]); //black queens
//        Assertions.assertEquals(6, countedPieces[3]); //white pawns
//        Assertions.assertEquals(2, countedPieces[9]); //white knights
//        Assertions.assertEquals(2, countedPieces[5]); //white bishops
//
//        Assertions.assertEquals(1, countedPieces[13]); //kings
//        Assertions.assertEquals(1, countedPieces[14]);
//
//    }

    @Test
    void printFen() {
        String fenString = "q3k3/2P3P1/6q1/3P1P2/1P1q4/3B1N2/P2B2N1/1R2K2R w K - 0 1";
        BoardState state = new BoardState(fenString);
        Assertions.assertEquals(fenString, state.toFenString());

        fenString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQk - 0 1";
        state = new BoardState(fenString);
        Assertions.assertEquals(fenString, state.toFenString());
    }

    @Test
    void enPassant() {
        String fenString = "7k/2pppppp/p7/Pp6/8/8/1PPPPPPP/7K w - b6 0 3";
        BoardState state = new BoardState(fenString);
        Assertions.assertNotNull(state.enPassant);
        Assertions.assertEquals(2, new Coordinate(state.enPassant).getRow()); //row
        Assertions.assertEquals(1, new Coordinate(state.enPassant).getColumn()); //col
        Assertions.assertEquals(fenString, state.toFenString());
    }
    @Test
    void enPassantTwo() {
        String fenString = "7k/p1pppppp/8/P7/1pP5/8/1P1PPPPP/7K b - c3 0 3";
        BoardState boardState = new BoardState(fenString);
        Assertions.assertNotNull(boardState.enPassant);
        Assertions.assertEquals(5, new Coordinate(boardState.enPassant).getRow()); //row
        Assertions.assertEquals(2, new Coordinate(boardState.enPassant).getColumn()); //col
        Assertions.assertEquals(fenString, boardState.toFenString());
    }
}
