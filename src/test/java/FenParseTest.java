import org.junit.jupiter.api.*;
import rosemary.board.*;
import rosemary.types.*;
import rosemary.types.CastlingRights;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FenParseTest {

    private final String start = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    @Test
    void startingBoard() {
        BoardState state = new BoardState(start);
        Assertions.assertEquals(CastlingRights.BOTH, state.getBlackCastling());
        Assertions.assertEquals(CastlingRights.BOTH, state.getWhiteCastling());
    }

    @Test
    void startingBoardNotModifiedWhenStoredInternally() {
        BoardState state = new BoardState(start);
        Assertions.assertEquals(start, state.toFenString());
    }

    @Test
    void getRow() {
        BoardState state =
                new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Assertions.assertEquals(8, state.board.getRow(7).length);
    }

    @Test
    void noWhiteQueenSide() {
        BoardState state =
                new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w Kkq - 0 1");
        state.printBoard();
        Assertions.assertEquals(CastlingRights.BOTH, state.getBlackCastling());
        Assertions.assertEquals(CastlingRights.KING, state.getWhiteCastling());
    }

    @Test
    void noBlackQueenSide() {
        BoardState state =
                new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQk - 0 1");
        state.printBoard();
        Assertions.assertEquals(CastlingRights.KING, state.getBlackCastling());
        Assertions.assertEquals(CastlingRights.BOTH, state.getWhiteCastling());
    }

    @Test
    void noCastlingRights() {
        BoardState state = new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1");
        state.printBoard();
        Assertions.assertEquals(CastlingRights.NONE, state.getBlackCastling());
        Assertions.assertEquals(CastlingRights.NONE, state.getWhiteCastling());
    }

    @Test
    void correctAmountOfPieces() {

        BoardState state =
                new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        state.printBoard();
        byte[] m = state.getPieceMap();
        Assertions.assertEquals(8, (int) m[Pieces.getNum('p')]);
        Assertions.assertEquals(8, (int) m[Pieces.getNum('P')]);
        Assertions.assertEquals(2, (int) m[Pieces.getNum('r')]);
        Assertions.assertEquals(2, (int) m[Pieces.getNum('R')]);
        Assertions.assertEquals(1, (int) m[Pieces.getNum('q')]);
        Assertions.assertEquals(1, (int) m[Pieces.getNum('Q')]);
        Assertions.assertEquals(1, (int) m[Pieces.getNum('k')]);
    }

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
        Assertions.assertEquals(2, MoveUtil.getRow(state.enPassant)); // row
        Assertions.assertEquals(1, MoveUtil.getColumn(state.enPassant)); // col
        Assertions.assertEquals(fenString, state.toFenString());
    }

    @Test
    void enPassantTwo() {
        String fenString = "7k/p1pppppp/8/P7/1pP5/8/1P1PPPPP/7K b - c3 0 3";
        BoardState boardState = new BoardState(fenString);
        Assertions.assertNotNull(boardState.enPassant);
        Assertions.assertEquals(5, MoveUtil.getRow(boardState.enPassant)); // row
        Assertions.assertEquals(2, MoveUtil.getColumn(boardState.enPassant)); // col
        Assertions.assertEquals(fenString, boardState.toFenString());
    }

    @Test
    void playingMovesShouldNotAlterOriginal() {
        BoardState boardState =
                new BoardState("rnbqkbnr/pppp2pp/8/3Ppp2/8/8/PPP1PPPP/RNBQKBNR w KQkq e6 0 3");
        String original = boardState.toFenString();

        // should create a new board, original should be unaltered
        Mover.makeMoves(boardState, "d5e6 d7e6".split(" "));

        Assertions.assertEquals(original, boardState.toFenString());
    }
}
