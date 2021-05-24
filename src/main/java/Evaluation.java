import java.util.Arrays;
import java.util.LinkedList;

public class Evaluation {

    //eval values TODO optimize
    static final int ePawn = 100;
    static final int eKnight = 180;
    static final int eBishop = 220;
    static final int eRook = 400;
    static final int eQueen = 780;

    int threadCount;

    ThreadGroup tg = new ThreadGroup("Evaluation_Threads");

    public Evaluation(int threadCount, int depth) {
        this.threadCount = threadCount;
    }


    static public int calculateEvaluation(BoardState board) {
        return calculateMaterial(board);
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
