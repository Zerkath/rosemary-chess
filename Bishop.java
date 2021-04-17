import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(int row, int col, boolean isWhite) {
        super(row, col, isWhite);
        if(this.isWhite) {
            this.fenSymbol = 'B';
        } else {
            this.fenSymbol = 'b';
        }
    }

    public ArrayList<int[]> getPossibleMoves() {

        ArrayList<int[]> moves = new ArrayList<>();
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



        /*
        int counter = 1; // The counter of traveled squares on a diagonal
        //checks possible moves for down and right diagonal
        while(true) {
            //Checks if destination isn't out of bounds
            if(isDestinationSquareOnBoard(row + counter, col + counter)) {
                //Checks if square is empty or has a piece of opposing color
                if(isDestinationPieceValid(row + counter, col + counter)) {
                    moves.add(new int[]{row + counter, col + counter});

                    //if destination square has enemy piece that results in capture, breaks loop
                    if(game.board[row+counter][col + counter] != null) {
                        break;
                    }
                } else { //breaks loop if encounters a piece of the same color
                    break;
                }

            } else { //breaks while loop if destination square is out of bounds
                break;
            }
            counter ++;
        }

        counter = 1;
        //checks possible moves for down and left diagonal
        while(true) {
            if(isDestinationSquareOnBoard(row + counter, col - counter)) {

                if(isDestinationPieceValid(row + counter, col - counter)) {
                    moves.add(new int[]{row + counter, col - counter});

                    if(game.board[row+counter][col - counter] != null) {
                        break;
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
            counter ++;
        }

        counter = 1;
        //checks possible moves for up and right diagonal
        while(true) {
            if(isDestinationSquareOnBoard(row - counter, col + counter)) {
                if(isDestinationPieceValid(row - counter, col + counter)) {
                    moves.add(new int[]{row - counter, col + counter});

                    if(game.board[row-counter][col + counter] != null) {
                        break;
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
            counter ++;
        }


        counter = 1;
        //checks possible moves for up and left diagonal
        while(true) {
            if(isDestinationSquareOnBoard(row - counter, col - counter)) {
                if(isDestinationPieceValid(row - counter, col - counter)) {
                    moves.add(new int[]{row - counter, col - counter});

                    if(game.board[row-counter][col - counter] != null) {
                        break;
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
            counter ++;
        }

         */

        return moves;
    }




}
