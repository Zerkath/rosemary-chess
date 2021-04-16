import java.util.ArrayList;
public class Pawn extends Piece {

    public Pawn(int row, int col, boolean isWhite) {
        super(row, col, isWhite);
    }

    public ArrayList<int[]> getPossibleMoves() {
        ArrayList<int[]> moves = new ArrayList<>();
        if(this.isWhite) { //if white the first index of the array goes down, left is towards 0 and right is towards 7
            int nextRow = row - 1;
            //move forward white
            if(game.board[nextRow][col] == null) {
                moves.add(new int[]{nextRow, col});
                if(this.row == 6 && game.board[row - 2][col] == null) { //if at starting square and nothing in front
                    moves.add(new int[]{row -2, col}); //can jump 2 squares TODO pin checks
                }
            }

            if(col > 0 && game.board[nextRow][col-1] != null && !game.board[nextRow][col-1].isWhite) {
                moves.add(new int[]{nextRow, col-1});
            }

            if(col < 7 && game.board[nextRow][col+1] != null && !game.board[nextRow][col+1].isWhite) {
                moves.add(new int[]{nextRow, col+1});
            }

            //TODO google en passant

        } else {
            //move forward black
            int nextRow = row + 1;

            if(game.board[nextRow][col] == null) {
                moves.add(new int[]{nextRow, col});
                if(this.row == 1 && game.board[row + 2][col] == null) {
                    moves.add(new int[]{row + 2, col}); // TODO pin checks 
                }
            }

            if(col > 0 && game.board[nextRow][col-1] != null && game.board[nextRow][col-1].isWhite) {
                moves.add(new int[]{nextRow, col-1});
            }

            if(col < 7 && game.board[nextRow][col+1] != null && game.board[nextRow][col+1].isWhite) {
                moves.add(new int[]{nextRow, col+1});
            }

        }
        return moves;
    }
}
