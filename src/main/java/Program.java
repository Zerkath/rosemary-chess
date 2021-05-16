import java.util.Scanner;
public class Program {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        Game game = new Game();
        Evaluation eval = new Evaluation(1);

        game.parseFen("8/p6/8/8/8/8/1P6/8 w - - 0 1");
        System.out.println(game.toFenString());
        game.printBoard();
        eval.assignNewTask(game);
        eval.startEvaluation();
    }
}
