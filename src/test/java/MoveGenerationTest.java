import java.util.HashSet;
import org.junit.jupiter.api.*;
import rosemary.PerftRunner;
import rosemary.board.*;
import rosemary.generation.MoveGenerator;
import rosemary.types.MoveUtil;
import rosemary.types.Moves;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MoveGenerationTest {
    String d_fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    MoveGenerator moveGenerator = new MoveGenerator();
    PerftRunner perftRunner = new PerftRunner(moveGenerator);

    @Test
    @Order(1)
    void whiteStartMoves() {
        BoardState boardState = new BoardState(d_fen);
        boardState.printBoard();

        Moves moves = moveGenerator.getLegalMoves(boardState);
        Assertions.assertEquals(20, moves.size(), moves.toString());
    }

    @Test
    @Order(2)
    void blackStartMoves() {
        BoardState boardState = new BoardState(d_fen);
        Mover.makeMove(boardState, MoveUtil.getMove("d2d4"));
        Moves moves = moveGenerator.getLegalMoves(boardState);
        Assertions.assertEquals(20, moves.size(), moves.toString());
    }

    @Test
    @Order(3)
    void whiteMovesAfter_d2d4_e7e5() {
        BoardState boardState = new BoardState(d_fen);
        boardState = Mover.makeMove(boardState, MoveUtil.getMove("d2d4"));
        boardState = Mover.makeMove(boardState, MoveUtil.getMove("e7e5"));
        Moves moves = moveGenerator.getLegalMoves(boardState);
        Assertions.assertEquals(29, moves.size());
    }

    @Test
    @Order(4)
    void leftSideEnPassant() {
        String position = "rnbqkbnr/2pppppp/8/pP6/8/8/PP1PPPPP/RNBQKBNR w KQkq a6 0 3";
        Moves moves = moveGenerator.getLegalMoves(new BoardState(position));
        Assertions.assertEquals(23, moves.size());
    }

    @Test
    @Order(5)
    void kingUnderAttack() {
        String position = "rnbq1bnr/pppkpppp/3pQ3/8/8/2P5/PP1PPPPP/RNB1KBNR b KQ - 3 3";
        Moves moves = moveGenerator.getLegalMoves(new BoardState(position));
        Assertions.assertEquals(4, moves.size(), moves.toString());
    }

    @Test
    @Order(5)
    void whiteKingUnderAttack() {
        String position = "rnb1kbnr/pp1ppppp/2p5/8/8/3Pq3/PPPKPPPP/RNBQ1BNR w - - 0 1";
        Moves moves = moveGenerator.getLegalMoves(new BoardState(position));
        Assertions.assertEquals(4, moves.size(), moves.toString());
    }

    BoardState getTestBoard() {
        return getTestBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    // String test = "6pr/8/7p/8/8/7P/8/6PR w - - 0 1";

    BoardState getTestBoard(String position) {
        return new BoardState(new BoardState(position));
    }

    @Test
    @Order(6)
    void PawnPromotion() {
        BoardState boardState = new BoardState("5q1q/6P1/8/8/8/8/8/8 w - - 0 1");
        boardState.printBoard();
        Moves moves = moveGenerator.getLegalMoves(boardState);
        Assertions.assertEquals(12, moves.size());
        for (short move : moves) {
            System.out.println(MoveUtil.moveToString(move));
        }
    }

    @Test
    @Order(7)
    void busyBoard() {
        BoardState boardState =
                new BoardState(
                        "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
        Mover.makeMoves(boardState, "h1g1 e7c5".split(" "));
        boardState.printBoard();
        System.out.println(Character.toLowerCase('q') == 'p');
    }

    @Test
    @Order(8)
    void weirdPawnBehavior() {
        BoardState boardState =
                new BoardState(
                        "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
        Mover.makeMoves(boardState, "e1f1 h3g2 f1g2".split(" "));
        boardState.printBoard();
    }

    @Test
    @Order(10)
    void speedStartPos10000Legal() {
        BoardState boardState =
                new BoardState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        int iterations = 10000;
        for (int i = 0; i < iterations; i++) {
            moveGenerator.getLegalMoves(boardState);
        }
    }

    @Test
    @Order(10)
    void speedMiddleGame10000Legal() {
        BoardState boardState =
                new BoardState(
                        "r3k2r/pbpnnpbp/1p1pp1p1/7Q/7q/1P1PP1P1/PBPNNPBP/R3K2R w KQkq - 6 10");
        int iterations = 10000;
        for (int i = 0; i < iterations; i++) {
            moveGenerator.getLegalMoves(boardState);
        }
    }

    @Test
    @Order(12)
    void speedRooksLegal() {
        BoardState boardState = new BoardState("3k4/8/3rR3/8/8/3Rr3/8/3K4 w - - 0 1");
        int iterations = 10000;
        for (int i = 0; i < iterations; i++) {
            moveGenerator.getLegalMoves(boardState);
        }
    }

    @Test
    @Order(14)
    void speedBishopsLegal() {
        BoardState boardState = new BoardState("K7/8/2B5/2B5/8/4bb2/8/7k w - - 0 1");
        int iterations = 10000;
        for (int i = 0; i < iterations; i++) {
            moveGenerator.getLegalMoves(boardState);
        }
    }

    @Test
    @Order(15)
    void blackKingMoves() {
        BoardState boardState = new BoardState("4k3/8/8/8/8/8/3PPP2/4K3 w - - 0 1");
        boardState = Mover.makeMove(boardState, MoveUtil.getMove("d2d4"));
        Moves moves = moveGenerator.getLegalMoves(boardState);
        Assertions.assertEquals(5, moves.size(), moves.toString());
    }

    @Test
    @Order(200)
    void movesToDepth() {

        long[] depth = new long[4];
        long[] expected = {20, 400, 8902, 197281, 4865609, 119060324};
        for (int i = 0; i < depth.length; i++) {
            long[] result = perftRunner.getPerftScore(i + 1, true, getTestBoard());
            depth[i] = result[0];
            System.out.println(
                    "Depth: " + (i + 1) + " Nodes: " + depth[i] + " Time: " + result[1] + "ms\n");
        }

        for (int i = 0; i < depth.length; i++) {
            Assertions.assertEquals(expected[i], depth[i]);
        }

        // run only this test
        // ./gradlew test --tests '*MoveGenerationTest.movesToDepth'

        // depth '6' too slow to reach took 278 seconds to complete on 23/05 e55b433
        // took 155s~ after 5/9/2021
        // took 136s~ after 13/9/2021, wsl2 99s??
        // took 73s~ after 15/9/2021
        // took 51s~ on 18/12/2022 ! new cpu
        // took 20s on 16/9/2023
        // took 14s~ after 17/9/2023
        //
        // this test is broken with async,
        // but with manual run in built app this takes 23s while old solution took 43s
    }

    @Test
    @Order(101)
    void movesFromPosition1() {
        long[] depth = new long[3];
        for (int i = 0; i < depth.length; i++) {
            long[] result =
                    perftRunner.getPerftScore(
                            i + 1,
                            true,
                            getTestBoard(
                                    "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1"));
            depth[i] = result[0];
            System.out.println(
                    "Depth: "
                            + (i + 1)
                            + " Nodes: "
                            + depth[i]
                            + " Time: "
                            + result[1]
                            + "ms movesFromPosition1 \n");
        }

        Assertions.assertEquals(48, depth[0]);
        Assertions.assertEquals(2039, depth[1]);
        Assertions.assertEquals(97862, depth[2]);
        // Assertions.assertEquals(4085603, depth[3]);
        // Assertions.assertEquals(193690690, depth[4]);
    }

    @Test
    @Order(102)
    void movesFromPosition2() {
        long[] depth = new long[4];
        for (int i = 0; i < depth.length; i++) {
            long[] result =
                    perftRunner.getPerftScore(
                            i + 1, true, getTestBoard("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1"));
            depth[i] = result[0];
            System.out.println(
                    "Depth: "
                            + (i + 1)
                            + " Nodes: "
                            + depth[i]
                            + " Time: "
                            + result[1]
                            + "ms movesFromPosition2\n");
        }

        Assertions.assertEquals(14, depth[0]);
        Assertions.assertEquals(191, depth[1]);
        Assertions.assertEquals(2812, depth[2]);
        Assertions.assertEquals(43238, depth[3]);
        // Assertions.assertEquals(674624, depth[4]);
        // Assertions.assertEquals(11030083, depth[5]);
    }

    @Test
    @Order(103)
    void movesFromPosition3() {
        long[] depth = new long[3];
        for (int i = 0; i < depth.length; i++) {
            long[] result =
                    perftRunner.getPerftScore(
                            i + 1,
                            true,
                            getTestBoard(
                                    "r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1"));
            depth[i] = result[0];
            System.out.println(
                    "Depth: "
                            + (i + 1)
                            + " Nodes: "
                            + depth[i]
                            + " Time: "
                            + result[1]
                            + "ms movesFromPosition3\n");
        }

        Assertions.assertEquals(6, depth[0]);
        Assertions.assertEquals(264, depth[1]);
        Assertions.assertEquals(9467, depth[2]);
        // Assertions.assertEquals(422333, depth[3]);
        // Assertions.assertEquals(15833292, depth[4]);
    }

    @Test
    @Order(103)
    void movesFromPosition3_1() {
        long[] result =
                perftRunner.getPerftScore(
                        1,
                        true,
                        getTestBoard(
                                "r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1R1K b kq - 1 1"));

        System.out.println(
                "Depth: 1"
                        + " Nodes: "
                        + result[0]
                        + " Time: "
                        + result[1]
                        + "ms movesFromPosition3\n");
        Assertions.assertEquals(46, result[0]);
    }

    @Test
    @Order(104)
    void movesFromPosition3mirrored() {
        long[] depth = new long[3];
        for (int i = 0; i < depth.length; i++) {
            long[] result =
                    perftRunner.getPerftScore(
                            i + 1,
                            true,
                            getTestBoard(
                                    "r2q1rk1/pP1p2pp/Q4n2/bbp1p3/Np6/1B3NBn/pPPP1PPP/R3K2R b KQ - 0 1"));
            depth[i] = result[0];
            System.out.println(
                    "Depth: "
                            + (i + 1)
                            + " Nodes: "
                            + depth[i]
                            + " Time: "
                            + result[1]
                            + "ms movesFromPosition3 mirrored\n");
        }

        Assertions.assertEquals(6, depth[0]);
        Assertions.assertEquals(264, depth[1]);
        Assertions.assertEquals(9467, depth[2]);
        // Assertions.assertEquals(422333, depth[3]);
        // Assertions.assertEquals(15833292, depth[4]);
    }

    @Test
    @Order(105)
    void movesFromPosition4() {
        long[] depth = new long[3];
        for (int i = 0; i < depth.length; i++) {
            long[] result =
                    perftRunner.getPerftScore(
                            i + 1,
                            true,
                            getTestBoard(
                                    "rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8"));
            depth[i] = result[0];
            System.out.println(
                    "Depth: "
                            + (i + 1)
                            + " Nodes: "
                            + depth[i]
                            + " Time: "
                            + result[1]
                            + "ms movesFromPosition4\n");
        }

        Assertions.assertEquals(44, depth[0]);
        Assertions.assertEquals(1486, depth[1]);
        Assertions.assertEquals(62379, depth[2]);
        // Assertions.assertEquals(2103487, depth[3]);
        // Assertions.assertEquals(89941194, depth[4]);
    }

    @Test
    void queenMystery() {
        BoardState boardState = new BoardState("4k3/p1ppqp2/4p3/3PN3/1p2P3/5Q2/7P/4K3 w - - 0 1");
        boardState = Mover.makeMove(boardState, MoveUtil.getMove("h2h3"));
        boardState = Mover.makeMove(boardState, MoveUtil.getMove("e7c5"));
        Moves moves = moveGenerator.getLegalMoves(boardState);
        Assertions.assertEquals(32, moves.size(), moves.toString());
    }

    @Test
    void randomKnight() {
        String position = "5k2/8/8/8/8/8/4Nn1P/R2QK2R w KQ - 1 8";
        Assertions.assertEquals(35, moveGenerator.getLegalMoves(new BoardState(position)).size());
    }

    @Test
    void kingShouldNotTeleport() {
        String position = "8/2p5/3p4/KP5r/5R1k/8/4P1P1/8 b - - 0 1";
        Assertions.assertEquals(2, moveGenerator.getLegalMoves(new BoardState(position)).size());
    }

    @Test
    void kingRestrictedByOtherKing() {
        String fenString = "8/8/8/p2N4/n1k1K3/8/5P2/8 b - - 3 52";
        BoardState boardState = new BoardState(fenString);
        MoveGenerator mv = new MoveGenerator();
        Moves expectedMoves = new Moves("c4c5 c4b5 c4b3 a4b2 a4b6 a4c3 a4c5");

        Moves moves = mv.getLegalMoves(boardState);

        Assertions.assertEquals(
                expectedMoves.size(),
                moves.size(),
                "\n" + getNonExpectedMoves(expectedMoves, moves).toString() + "\n");
    }

    private Moves getNonExpectedMoves(Moves expected, Moves actual) {
        Moves result = new Moves();
        HashSet<Short> expectedSet = new HashSet<>();
        expectedSet.addAll(expected);
        for (short move : actual) {
            System.out.println("Was not in set " + move);

            if (!expectedSet.contains(move)) {
                result.add(move);
            } else {
                System.out.println(MoveUtil.moveToString(move) + "expected");
            }
        }
        return result;
    }
}
