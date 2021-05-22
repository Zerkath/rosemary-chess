import org.junit.jupiter.api.*;
public class MoveGenerationTests {
    BoardState game = new BoardState();
    String d_fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    @Test
    void whiteStartMoves() {
        game = new BoardState(Utils.parseFen(d_fen));
        Moves moves = MoveGenerator.getAllMoves(game);
        Assertions.assertEquals(20, moves.size());
    }

    @Test
    void blackStartMoves() {
        game = new BoardState(Utils.parseFen(d_fen));
        game.movePiece(Utils.parseCommand("d2d4"));
        Moves moves = MoveGenerator.getAllMoves(game);
        Assertions.assertEquals(20, moves.size());
    }

    @Test
    void whiteMovesAfter_d2d4_e7e5() {
        game = new BoardState(Utils.parseFen(d_fen));
        game.movePiece(Utils.parseCommand("d2d4"));
        game.movePiece(Utils.parseCommand("e7e5"));
        Moves moves = MoveGenerator.getAllMoves(game);
        Assertions.assertEquals(29, moves.size());
    }
}
