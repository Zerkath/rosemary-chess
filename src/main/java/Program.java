import java.util.Scanner;
public class Program {
    public static void main(String[] args) {
//        Scanner scn = new Scanner(System.in);
        Game game = new Game();
        Evaluation eval = new Evaluation(16, 10);

        game.parseFen("PPPPP1k1/P1PPP3/P1PPP3/P1PPP3/P2PP3/PN1PP3/P2PP3/P2P4 w - - 0 1");
        System.out.println(game.toFenString());
        game.printBoard();
        eval.assignNewTask(game);
        eval.startEvaluation();
    }
}
