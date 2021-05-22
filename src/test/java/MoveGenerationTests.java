import org.junit.jupiter.api.*;
public class MoveGenerationTests {
    Game game = new Game();
    Utils utils = new Utils();
    String d_fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    @Test
    void whiteStartMoves() {
        game.parseFen(d_fen);
        game.getPossibleMovesForTurn();
        game.printBoard();
        Assertions.assertEquals(20, game.moves.size());
    }

    @Test
    void blackStartMoves() {
        game.parseFen(d_fen);
        int [][] move = utils.parseCommand("d2d4");
        game.movePiece(move);
        game.getPossibleMovesForTurn();
        game.printBoard();
        Assertions.assertEquals(20, game.moves.size());
    }

    @Test
    void whiteMovesAfter_d2d4_e7e5() {
        game.parseFen(d_fen);
        int [][] move = utils.parseCommand("d2d4");
        game.movePiece(move[0], move[1]);
        move = utils.parseCommand("e7e5");
        game.movePiece(move[0], move[1]);
        game.getPossibleMovesForTurn();
        game.printBoard();
        Assertions.assertEquals(29, game.moves.size());
    }
}
