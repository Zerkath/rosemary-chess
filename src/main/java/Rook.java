import java.util.LinkedList;

public class Rook extends Queen {
    public Rook(int row, int col, boolean isWhite) {
        super(row, col, isWhite);
        if(this.isWhite) {
            this.fenSymbol = 'R';
        } else {
            this.fenSymbol = 'r';
        }
    }

    public LinkedList<int[]> getPossibleMoves() {
        return rookMoves();
    }
}
