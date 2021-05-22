import org.junit.jupiter.api.*;
public class MoveGenerationTests {
    BoardState game = new BoardState();
    String d_fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    BoardState boardState = new BoardState(Utils.parseFen(d_fen));

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

    BoardState getTestBoard() {
        return new BoardState(Utils.parseFen(d_fen));
    }

    @Test
    void movesToDepths() {

        int [] depth = new int[5];
        for (int i = 0; i < depth.length; i++) {
            long start = System.currentTimeMillis();
            depth[i] = recursion(i+1, getTestBoard());
            long end = System.currentTimeMillis();
            System.out.println("Depth: " + (i+1) +  " Nodes: " + depth[i] + " Time: " + (end-start) + "ms");

        }


        Assertions.assertEquals(20, depth[0]);
        Assertions.assertEquals(400, depth[1]);
        Assertions.assertEquals(8902, depth[2]);
        Assertions.assertEquals(197281, depth[3]);
        Assertions.assertEquals(4865609, depth[4]);
//        Assertions.assertEquals(119060324, depth[5]); //to slow to reach
    }

    private int recursion(int depth, BoardState boardState) {
        if(depth <= 0) {
            return 1;
        }
        Moves moves = MoveGenerator.getAllMoves(boardState);
        int numPositions = 0;

        for (Move move: moves) {
            boardState.movePiece(move);
            numPositions += recursion(depth-1, boardState);
            boardState.unMakeMove();
        }
        return numPositions;
    }
}
