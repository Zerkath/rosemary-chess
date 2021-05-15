import java.util.ArrayList;

public class King extends Piece {
    public King(int row, int col, boolean isWhite) {
        super(row, col, isWhite);
        if(this.isWhite) {
            this.fenSymbol = 'K';
        } else {
            this.fenSymbol = 'k';
        }
    }

    @Override
    public ArrayList<int[]> getPossibleMoves() {

        ArrayList<int[]> moves = new ArrayList<>();


        //KING MOVES UP
        if(isMovePossible(row - 1, col)) {
            moves.add(new int[]{row - 1, col});
        }

        //KING MOVES UP AND RIGHT DIAGONAL
        if(isMovePossible(row - 1, col + 1)) {
            moves.add(new int[]{row - 1, col + 1});
        }

        //KING MOVES RIGHT
        if(isMovePossible(row, col + 1)) {
            moves.add(new int[]{row, col + 1});
        }

        //KING MOVES DOWN AND RIGHT DIAGONAL
        if(isMovePossible(row + 1, col + 1)) {
            moves.add(new int[]{row + 1, col + 1});
        }

        //KING MOVES DOWNWARDS
        if(isMovePossible(row + 1, col)) {
            moves.add(new int[]{row + 1, col});
        }

        //KING MOVES DOWN AND RIGHT DIAGONAL
        if(isMovePossible(row + 1, col - 1)) {
            moves.add(new int[]{row + 1, col - 1});
        }

        //KING MOVES LEFT
        if(isMovePossible(row, col - 1)) {
            moves.add(new int[]{row, col - 1});
        }

        //KING MOVES UP AND LEFT DIAGONAL
        if(isMovePossible(row - 1, col - 1)) {
            moves.add(new int[]{row - 1, col - 1});
        }

        return moves;
    }
}
