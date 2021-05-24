import java.util.Arrays;
import java.util.Scanner;
public class TestProgram {
    public static void main(String[] args) {
        String pos = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        BoardState boardState = new BoardState(Utils.parseFen(pos));
        Moves moves = MoveGenerator.getLegalMoves(boardState);

    }
}
