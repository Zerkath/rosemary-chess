import java.util.Scanner;
public class Program {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
            Game game = new Game();

        game.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        game.printBoard();

        while(true) {
            System.out.println("Anna koordinatit muodossa (row col)");
            String move = scn.nextLine();
            String [] moves = move.split(" ");
            game.printPieceLegalMove(Integer.parseInt(moves[0]) , Integer.parseInt(moves[1]));
        }
    }
}
