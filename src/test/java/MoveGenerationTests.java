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

    @Test
    void leftSideEnPassant() {
        String position = "rnbqkbnr/2pppppp/8/pP6/8/8/PP1PPPPP/RNBQKBNR w KQkq a6 0 3";
        Moves moves = MoveGenerator.getLegalMoves(Utils.parseFen(position));
        Assertions.assertEquals(23, moves.size());
    }

    @Test
    void kingUnderAttack() {
        String position = "rnbq1bnr/pppkpppp/3pQ3/8/8/2P5/PP1PPPPP/RNB1KBNR b KQ - 3 3";
        Moves moves = MoveGenerator.getLegalMoves(Utils.parseFen(position));
        Assertions.assertEquals(4, moves.size());
    }

    BoardState getTestBoard() {
        //startpos f2f3 h7h6 a2a3

        //rnbqkbnr/ppppppp1/7p/8/8/P4P2/1PPPP1PP/RNBQKBNR b KQkq - 0 2
        String test = "6pr/8/7p/8/8/7P/8/6PR w - - 0 1";
        return new BoardState(Utils.parseFen(d_fen));
    }

    @Test
    void movesToDepths() {

        int [] depth = new int[5];
        for (int i = 0; i < depth.length; i++) {
            long start = System.currentTimeMillis();
            depth[i] = recursion(i+1, getTestBoard(), i+1);
            long end = System.currentTimeMillis();
            System.out.println("Depth: " + (i+1) +  " Nodes: " + depth[i] + " Time: " + (end-start) + "ms\n");

        }

        Assertions.assertEquals(20, depth[0]);
        Assertions.assertEquals(400, depth[1]);
        Assertions.assertEquals(8902, depth[2]);
        Assertions.assertEquals(197281, depth[3]);
        Assertions.assertEquals(4865609, depth[4]);
//        Assertions.assertEquals(119060324, depth[5]); //too slow to reach took 278 seconds to complete on 23/05 e55b433
    }

    private int recursion(int depth, BoardState boardState, int start) {

        if(depth <= 0) {
            return 1;
        }
        Moves moves = MoveGenerator.getLegalMoves(boardState);
        int numPositions = 0;

        for (Move move: moves) {
            boardState.movePiece(move);
            int result = recursion(depth-1, boardState, start);
            if(depth == start) System.out.println(Utils.parseCommand(move) + ": " + result);
            numPositions += result;
            boardState.unMakeMove();
        }
        return numPositions;
    }
}
