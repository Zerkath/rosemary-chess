import java.util.ArrayList;

public class Queen extends Piece{
    public Queen(int row, int col, boolean isWhite) {
        super(row, col, isWhite);
        if(this.isWhite) {
            this.fenSymbol = 'Q';
        } else {
            this.fenSymbol = 'q';
        }
    }

    public ArrayList<int[]> getPossibleMoves() {

        ArrayList<int[]> moves = new ArrayList<>();
        int destRow, destCol;

        //QUEEN MOVES UPWARDS
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

        //QUEEN MOVES UP AND RIGHT DIAGONAL
        for(int i = 1; row - i >= 0 && col + i <= 7; i++) {
            destRow = row - i;
            destCol = col + i;
            if(isMovePossible(destRow, destCol)) {
                moves.add(new int[]{destRow, destCol});

                if(game.board[destRow][destCol] != null) {
                    break;
                }
            } else {
                break;
            }
        }

        //QUEEN MOVES RIGHT
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

        //QUEEN MOVES DOWN AND RIGHT DIAGONAL
        for(int i = 1; row + i <= 7 && col + i <= 7; i++) { //Runs as long as destination is within board limits
            destRow = row + i;
            destCol = col + i;
            if(isMovePossible(destRow, destCol)) { //Checks if within board limits and
                moves.add(new int[]{destRow, destCol});

                //if destination square has enemy piece that results in capture, breaks loop
                if(game.board[destRow][destCol] != null) {
                    break;
                }
            } else {
                break;
            }
        }

        //QUEEN MOVES DOWNWARDS
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

        //QUEEN MOVES DOWN AND LEFT DIAGONAL
        for(int i = 1; row + i <= 7 && col - i >= 0; i++) {
            destRow = row + i;
            destCol = col - i;
            if(isMovePossible(destRow, destCol)) {
                moves.add(new int[]{destRow, destCol});

                if(game.board[destRow][destCol] != null) {
                    break;
                }
            } else {
                break;
            }
        }

        //QUEEN MOVES LEFT
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

        //QUEEN MOVES UP AND LEFT DIAGONAL
        for(int i = 1; row - i >= 0 && col - i >= 0; i++) {
            destRow = row - i;
            destCol = col - i;
            if(isMovePossible(destRow, destCol)) {
                moves.add(new int[]{destRow, destCol});

                if(game.board[destRow][destCol] != null) {
                    break;
                }
            } else {
                break;
            }
        }

        return moves;
    }


}
