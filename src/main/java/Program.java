import java.util.Scanner;
public class Program {
    public static void main(String[] args) {
//        Scanner scn = new Scanner(System.in);
        Game game = new Game();
        Evaluation eval = new Evaluation(16, 10);

        game.parseFen("4b3/8/8/8/8/8/8/7B w - - 0 1");
        System.out.println(game.toFenString());
        game.printBoard();
        eval.assignNewTask(game);
        eval.startEvaluation();
    }
}
