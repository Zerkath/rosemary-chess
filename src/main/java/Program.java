import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
public class Program {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        Game game = new Game();
        Evaluation eval = new Evaluation(4);

        game.parseFen("q3k3/2P3P1/6q1/3P1P2/1P1q4/3B1N2/P2B2N1/1R2K2R b K - 0 3");
        System.out.println(game.toFenString());
        game.printBoard();
        eval.updateTask(game);
        System.out.println(eval.material);
//        while(!game.moves.empty()) {
//            LinkedList<int[]> pieceMoves = game.moves.pop();
//            System.out.print("Starting square: " + Arrays.toString(pieceMoves.pop()) + " : ");
//            while(!pieceMoves.isEmpty()) {
//                System.out.print(Arrays.toString(pieceMoves.pop()));
//                if(!pieceMoves.isEmpty()) System.out.print(", ");
//            }
//            System.out.println();
//        }
        eval.startEvaluation();
    }
}
