import java.util.ArrayList;

public class Knight extends Piece{

    public Knight(int row, int col, boolean isWhite) {
        super(row, col, isWhite);
        if(this.isWhite) {
            this.fenSymbol = 'N';
        } else {
            this.fenSymbol = 'n';
        }
    }

    public ArrayList<int[]> getPossibleMoves() {

        ArrayList<int[]> moves = new ArrayList<>();
        int destRow, destCol;


        //2 up and 1 to sides
        destRow = row - 2;
        destCol = col - 1;
        if(isMovePossible(destRow, destCol)) { //Checks if 2 up and 1 left is on board
            moves.add(new int[]{destRow, destCol});
        }
        destCol = col + 1;
        if(isMovePossible(destRow, destCol)) { //Checks if 2 up and 1 right is on board
            moves.add(new int[]{destRow, destCol});

        }

        //2 down and 1 to sides
        destRow = row + 2;
        destCol = col - 1;
        if(isMovePossible(destRow, destCol)) {
            moves.add(new int[]{destRow, destCol});
        }
        destCol = col + 1;
        if(isMovePossible(destRow, destCol)) {
            moves.add(new int[]{destRow, destCol});
        }

        //2 to the left and 1 up or down
        destRow = row + 1;
        destCol = col - 2;
        if(isMovePossible(destRow, destCol)) {
            moves.add(new int[]{destRow, destCol});
        }
        destRow = row - 1;
        if(isMovePossible(destRow, destCol)) {
            moves.add(new int[]{destRow, destCol});
        }

        //2 to the right and 1 up or down
        destRow = row + 1;
        destCol = col + 2;
        if(isMovePossible(destRow, destCol)) {
            moves.add(new int[]{destRow, destCol});
        }
        destRow = row - 1;
        if(isMovePossible(destRow, destCol)) {
            moves.add(new int[]{destRow, destCol});
        }

        return moves;
    }

}
