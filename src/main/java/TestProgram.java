import java.util.Arrays;
import java.util.Scanner;
public class TestProgram {
    public static void main(String[] args) {
        String pos = "8/r7/7p/8/8/7P/8/6PR b - - 0 1";
        BoardState boardState = new BoardState(Utils.parseFen(pos));
        Moves moves = MoveGenerator.getLegalMoves(boardState);
        boolean [][] attacked = MoveGenerator.getAttackedSquares(moves);

        System.out.println(Arrays.deepToString(attacked));
    }
}
