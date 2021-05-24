import java.util.Arrays;
import java.util.LinkedList;

public class Evaluation {

    static final int ePawn = 100;
    static final int eKnight = 300;
    static final int eBishop = 310;
    static final int eRook = 500;
    static final int eQueen = 900;

    int threadCount;

    ThreadGroup tg = new ThreadGroup("Evaluation_Threads");

    public Evaluation(int threadCount, int depth) {
        this.threadCount = threadCount;
    }


    static public int calculateEvaluation(BoardState board) {
        int negation = board.turn == PlayerTurn.BLACK ? -1 : 1;
        Moves moves = MoveGenerator.getLegalMoves(board);
        int options = (moves.size() * negation * 2);
        int centralControl = calculatePiecesInMiddle(board);
        int materialAdvantage = calculateMaterial(board);
        return options+materialAdvantage+centralControl;
    }

    static private int calculatePiecesInMiddle(BoardState board) {
        char [][] data = board.board;
        int value = 0;
        for (int i = 2; i < 6; i++) {
            for (int j = 2; j < 6; j++) {
                char piece = data[i][j];
                if(piece != '-') {
                    if(Character.isLowerCase(piece)) {
                        value -= 30;
                    } else {
                        value += 30;
                    }
                }
            }
        }
        return value;
    }

    static public int calculateMaterial(BoardState curr) { //+ if white, -if black
        int result = 0;
        int [] arr = curr.countPieces();
        result += arr[3] * ePawn;
        result -= arr[4] * ePawn;
        result += arr[5] * eBishop;
        result -= arr[6] * eBishop;
        result += arr[7] * eRook;
        result -= arr[8] * eRook;
        result += arr[9] * eKnight;
        result -= arr[10] * eKnight;
        result += arr[11] * eQueen;
        result -= arr[12] * eQueen;
        return result;
    }
}
