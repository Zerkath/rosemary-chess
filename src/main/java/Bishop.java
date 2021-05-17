import java.util.LinkedList;

public class Bishop extends Queen {
    public Bishop(int row, int col, boolean isWhite) {
        super(row, col, isWhite);
        if(this.isWhite) {
            this.fenSymbol = 'B';
        } else {
            this.fenSymbol = 'b';
        }
    }

    public LinkedList<int[]> getPossibleMoves() {
        return bishopMoves();
    }
}
