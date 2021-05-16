import java.util.LinkedList;

public class Rook extends Piece{
    public Rook(int row, int col, boolean isWhite) {
        super(row, col, isWhite);
        if(this.isWhite) {
            this.fenSymbol = 'R';
        } else {
            this.fenSymbol = 'r';
        }
    }

    public LinkedList<int[]> getPossibleMoves() {

        LinkedList<int[]> moves = new LinkedList<>();
        int destRow, destCol;

        //Rook moves to left
        for(int i = 1; col - i >= 0; i++) { //Runs as long as destination is within board limits
            destCol = col - i;
            if(isMovePossible(row, destCol)) {
                moves.add(new int[]{row, destCol});

                if(game.board[row][destCol] != null) { //Checks if move ends in capture to end the loop
                    break;
                }
            } else {
                break;
            }
        }


        //Rook moves upwards
        for(int i = 1; row - i >= 0; i++) {
            destRow = row - i;
            if(isMovePossible(destRow, col)) {
                moves.add(new int[]{destRow, col});

                if(game.board[destRow][col] != null) {
                    break;
                }
            } else {
                break;
            }
        }

        //Rook moves to right
        for(int i = 1; col + i <= 7; i++) {
            destCol = col + i;
            if(isMovePossible(row, destCol)) {
                moves.add(new int[]{row, destCol});

                if(game.board[row][destCol] != null) {
                    break;
                }
            } else {
                break;
            }
        }

        //Rook moves downwards
        for(int i = 1; row + i <= 7; i++) {
            destRow = row + i;
            if(isMovePossible(destRow, col)) {
                moves.add(new int[]{destRow, col});

                if(game.board[destRow][col] != null) {
                    break;
                }
            } else {
                break;
            }
        }

        return moves;
    }
}
