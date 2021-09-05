import BoardRepresentation.BoardState;
import DataTypes.Move;
import DataTypes.Moves;
import CommonTools.Utils;
import Main.UCI_Controller;
import MoveGeneration.MoveGenerator;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MoveGenerationTest {
    BoardState game = new BoardState();
    String d_fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    BoardState boardState = new BoardState(Utils.parseFen(d_fen));
    UCI_Controller uci = new UCI_Controller();

    MoveGenerator moveGenerator = new MoveGenerator();

    @Test
    @Order(1)
    void whiteStartMoves() {
        game = new BoardState(Utils.parseFen(d_fen));
        Moves moves = moveGenerator.getLegalMoves(game);
        Assertions.assertEquals(20, moves.size(), moves.getString());
    }

    @Test
    @Order(2)
    void blackStartMoves() {
        game = new BoardState(Utils.parseFen(d_fen));
        game.makeMove(Utils.parseCommand("d2d4"));
        Moves moves = moveGenerator.getLegalMoves(game);
        Assertions.assertEquals(20, moves.size(), moves.getString());
    }

    @Test
    @Order(3)
    void whiteMovesAfter_d2d4_e7e5() {
        game = new BoardState(Utils.parseFen(d_fen));
        game.makeMove(Utils.parseCommand("d2d4"));
        game.makeMove(Utils.parseCommand("e7e5"));
        Moves moves = moveGenerator.getLegalMoves(game);
        Assertions.assertEquals(29, moves.size());
    }

    @Test
    @Order(4)
    void leftSideEnPassant() {
        String position = "rnbqkbnr/2pppppp/8/pP6/8/8/PP1PPPPP/RNBQKBNR w KQkq a6 0 3";
        Moves moves = moveGenerator.getLegalMoves(Utils.parseFen(position));
        Assertions.assertEquals(23, moves.size());
    }

    @Test
    @Order(5)
    void kingUnderAttack() {
        String position = "rnbq1bnr/pppkpppp/3pQ3/8/8/2P5/PP1PPPPP/RNB1KBNR b KQ - 3 3";
        Moves moves = moveGenerator.getLegalMoves(Utils.parseFen(position));
        Assertions.assertEquals(4, moves.size(), moves.getString());
    }

    @Test
    @Order(5)
    void whiteKingUnderAttack() {
        String position = "rnb1kbnr/pp1ppppp/2p5/8/8/3Pq3/PPPKPPPP/RNBQ1BNR w - - 0 1";
        Moves moves = moveGenerator.getLegalMoves(Utils.parseFen(position));
        Assertions.assertEquals(4, moves.size(), moves.getString());
    }

    BoardState getTestBoard() {
        return getTestBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    //        String test = "6pr/8/7p/8/8/7P/8/6PR w - - 0 1";

    BoardState getTestBoard(String position) {
        return new BoardState(Utils.parseFen(position));
    }

    @Test
    @Order(6)
    void PawnPromotion() {
        BoardState boardState = new BoardState(Utils.parseFen("5q1q/6P1/8/8/8/8/8/8 w - - 0 1"));
        Utils.printBoard(boardState);
        Moves moves = moveGenerator.getLegalMoves(boardState);
        Assertions.assertEquals(12, moves.size());
        for (Move move: moves) {
            System.out.println(Utils.parseCommand(move));
        }
    }

    @Test
    @Order(7)
    void busyBoard() {
        BoardState boardState = new BoardState(Utils.parseFen("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1"));
        boardState.playMoves("h1g1 e7c5".split(" "));
        Utils.printBoard(boardState);
        System.out.println(Character.toLowerCase('q') == 'p');
    }

    @Test
    @Order(8)
    void weirdPawnBehavior() {
        BoardState boardState = new BoardState(Utils.parseFen("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1"));
        boardState.playMoves("e1f1 h3g2 f1g2".split(" "));
        Utils.printBoard(boardState);
    }

    @Test
    @Order(9)
    void speedStartPos10000PseudoLegal() {
        BoardState boardState = new BoardState(Utils.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"));
        int iterations = 10000;
        for (int i = 0; i < iterations; i++) {
            moveGenerator.getLegalMoves(boardState);
        }
    }

    @Test
    @Order(10)
    void speedStartPos10000Legal() {
        BoardState boardState = new BoardState(Utils.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"));
        int iterations = 10000;
        for (int i = 0; i < iterations; i++) {
            moveGenerator.getLegalMoves(boardState);
        }
    }

    //r3k2r/pbpnnpbp/1p1pp1p1/7Q/7q/1P1PP1P1/PBPNNPBP/R3K2R w KQkq - 6 10

    @Test
    @Order(10)
    void speedMiddlegame10000PseudoLegal() {
        BoardState boardState = new BoardState(Utils.parseFen("r3k2r/pbpnnpbp/1p1pp1p1/7Q/7q/1P1PP1P1/PBPNNPBP/R3K2R w KQkq - 6 10"));
        int iterations = 10000;
        for (int i = 0; i < iterations; i++) {
            moveGenerator.getLegalMoves(boardState);
        }
    }

    @Test
    @Order(10)
    void speedMiddlegame10000Legal() {
        BoardState boardState = new BoardState(Utils.parseFen("r3k2r/pbpnnpbp/1p1pp1p1/7Q/7q/1P1PP1P1/PBPNNPBP/R3K2R w KQkq - 6 10"));
        int iterations = 10000;
        for (int i = 0; i < iterations; i++) {
            moveGenerator.getLegalMoves(boardState);
        }
    }

    @Test
    @Order(11)
    void speedRooksPseudo() {
        BoardState boardState = new BoardState(Utils.parseFen("3k4/8/3rR3/8/8/3Rr3/8/3K4 w - - 0 1"));
        int iterations = 10000;
        for (int i = 0; i < iterations; i++) {
            moveGenerator.getLegalMoves(boardState);
        }
    }

    @Test
    @Order(12)
    void speedRooksLegal() {
        BoardState boardState = new BoardState(Utils.parseFen("3k4/8/3rR3/8/8/3Rr3/8/3K4 w - - 0 1"));
        int iterations = 10000;
        for (int i = 0; i < iterations; i++) {
            moveGenerator.getLegalMoves(boardState);
        }
    }

    @Test
    @Order(13)
    void speedBishopsPseudo() {
        BoardState boardState = new BoardState(Utils.parseFen("K7/8/2B5/2B5/8/4bb2/8/7k w - - 0 1"));
        int iterations = 10000;
        for (int i = 0; i < iterations; i++) {
            moveGenerator.getLegalMoves(boardState);
        }
    }

    @Test
    @Order(14)
    void speedBishopsLegal() {
        BoardState boardState = new BoardState(Utils.parseFen("K7/8/2B5/2B5/8/4bb2/8/7k w - - 0 1"));
        int iterations = 10000;
        for (int i = 0; i < iterations; i++) {
            moveGenerator.getLegalMoves(boardState);
        }
    }

    @Test
    @Order(15)
    void blackKingMoves() {
        game = new BoardState(Utils.parseFen("4k3/8/8/8/8/8/3PPP2/4K3 w - - 0 1"));
        game.makeMove(Utils.parseCommand("d2d4"));
        Moves moves = moveGenerator.getLegalMoves(game);
        Assertions.assertEquals(5, moves.size(), moves.getString());
    }

    @Test
    @Order(200)
    void movesToDepth() {

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
        //took 206s~ after changes
        //took 155s~ after 5/9/2021
    }

    @Test
    @Order(101)
    void movesFromPosition1() {
        int [] depth = new int[3];
        for (int i = 0; i < depth.length; i++) {
            long start = System.currentTimeMillis();
            depth[i] = recursion(i+1, getTestBoard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1"), i+1);
            long end = System.currentTimeMillis();
            System.out.println("Depth: " + (i+1) +  " Nodes: " + depth[i] + " Time: " + (end-start) + "ms\n");

        }

        Assertions.assertEquals(48, depth[0]);
        Assertions.assertEquals(2039, depth[1]);
        Assertions.assertEquals(97862, depth[2]);
//        Assertions.assertEquals(4085603, depth[3]);
//        Assertions.assertEquals(193690690, depth[4]);
    }

    @Test
    @Order(102)
    void movesFromPosition2() {
        int [] depth = new int[5];
        for (int i = 0; i < depth.length; i++) {
            long start = System.currentTimeMillis();
            depth[i] = recursion(i+1, getTestBoard("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1"), i+1);
            long end = System.currentTimeMillis();
            System.out.println("Depth: " + (i+1) +  " Nodes: " + depth[i] + " Time: " + (end-start) + "ms\n");

        }

        Assertions.assertEquals(14, depth[0]);
        Assertions.assertEquals(191, depth[1]);
        Assertions.assertEquals(2812, depth[2]);
        Assertions.assertEquals(43238, depth[3]);
        Assertions.assertEquals(674624, depth[4]);
//        Assertions.assertEquals(11030083, depth[5]);
    }

    @Test
    @Order(103)
    void movesFromPosition3() {
        int [] depth = new int[3];
        for (int i = 0; i < depth.length; i++) {
            long start = System.currentTimeMillis();
            depth[i] = recursion(i+1, getTestBoard("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1"), i+1);
            long end = System.currentTimeMillis();
            System.out.println("Depth: " + (i+1) +  " Nodes: " + depth[i] + " Time: " + (end-start) + "ms\n");

        }

        Assertions.assertEquals(6, depth[0]);
        Assertions.assertEquals(264, depth[1]);
        Assertions.assertEquals(9467, depth[2]);
//        Assertions.assertEquals(422333, depth[3]);
//        Assertions.assertEquals(15833292, depth[4]);
    }

    @Test
    @Order(104)
    void movesFromPosition3mirrored() {
        int [] depth = new int[3];
        for (int i = 0; i < depth.length; i++) {
            long start = System.currentTimeMillis();
            depth[i] = recursion(i+1, getTestBoard("r2q1rk1/pP1p2pp/Q4n2/bbp1p3/Np6/1B3NBn/pPPP1PPP/R3K2R b KQ - 0 1"), i+1);
            long end = System.currentTimeMillis();
            System.out.println("Depth: " + (i+1) +  " Nodes: " + depth[i] + " Time: " + (end-start) + "ms\n");

        }

        Assertions.assertEquals(6, depth[0]);
        Assertions.assertEquals(264, depth[1]);
        Assertions.assertEquals(9467, depth[2]);
//        Assertions.assertEquals(422333, depth[3]);
//        Assertions.assertEquals(15833292, depth[4]);
    }

    @Test
    @Order(105)
    void movesFromPosition4() {
        int [] depth = new int[3];
        for (int i = 0; i < depth.length; i++) {
            long start = System.currentTimeMillis();
            depth[i] = recursion(i+1, getTestBoard("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8"), i+1);
            long end = System.currentTimeMillis();
            System.out.println("Depth: " + (i+1) +  " Nodes: " + depth[i] + " Time: " + (end-start) + "ms\n");

        }

        Assertions.assertEquals(44, depth[0]);
        Assertions.assertEquals(1486, depth[1]);
        Assertions.assertEquals(62379, depth[2]);
//        Assertions.assertEquals(2103487, depth[3]);
//        Assertions.assertEquals(89941194, depth[4]);
    }

    @Test
    void randomKnight() {
        String position = "5k2/8/8/8/8/8/4Nn1P/R2QK2R w KQ - 1 8";
        Assertions.assertEquals(35, moveGenerator.getLegalMoves(Utils.parseFen(position)).size());
    }

    private int recursion(int depth, BoardState boardState, int start) {

        if(depth <= 0) {
            return 1;
        }
        Moves moves = moveGenerator.getLegalMoves(boardState);
        int numPositions = 0;

        for (Move move: moves) {
            boardState.makeMove(move);
            int result = recursion(depth-1, boardState, start);
            if(depth == start) System.out.println(Utils.parseCommand(move) + ": " + result);
            numPositions += result;
            boardState.unMakeMove();
        }
        return numPositions;
    }
}
