import java.util.Scanner;
public class program {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
            Game game = new Game();

        game.parseFen("rnbqkbnr/pppppppp/2P5/1P6/8/8/PP2PPPP/RNBQKBNR w KQkq - 0 1");

        while(true) {
            System.out.println("Anna koordinatit (array numerot)");
            String move = scn.nextLine();
            String [] moves = move.split(" ");
            game.printPieceLegalMove(Integer.parseInt(moves[0]) , Integer.parseInt(moves[1]));
        }
    }
}
