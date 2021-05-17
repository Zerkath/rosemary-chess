import java.util.LinkedList;

public class Queen extends Piece {
    public Queen(int row, int col, boolean isWhite) {
        super(row, col, isWhite);
        if(this.isWhite) {
            this.fenSymbol = 'Q';
        } else {
            this.fenSymbol = 'q';
        }
    }


    public LinkedList<int[]> bishopMoves() {
        LinkedList<int[]> moves = new LinkedList<>();
        int destRow, destCol;

        //Bishop moves down and right
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

        //Bishop moves down and left
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

        //Bishop moves up and right
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

        //Bishop moves up and left
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

    public LinkedList<int[]> rookMoves() {
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

    public LinkedList<int[]> getPossibleMoves() {

        LinkedList<int[]> moves = new LinkedList<>();

        moves.addAll(rookMoves());
        moves.addAll(bishopMoves());

        return moves;
    }
}
