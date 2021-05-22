import java.util.LinkedList;

public class Pawn extends Piece {

    public Pawn(int row, int col, boolean isWhite) {
        super(row, col, isWhite);

        if(this.isWhite) {
            this.fenSymbol = 'P';
        } else {
            this.fenSymbol = 'p';
        }
    }

    boolean promotable = false;

    //White Starting square row is 6, and black is 1 
    
    public LinkedList<int[]> getPossibleMoves() {
        LinkedList<int[]> moves = new LinkedList<>();
        int nextRow = row;
        int doubleJump = row;
        int enPassantRow;

        if(this.isWhite) {
            nextRow -= 1;
            doubleJump -=2;
            enPassantRow = 3;
            if(nextRow == 0) {
                promotable = true;
            }
        } else {
            nextRow += 1;
            doubleJump +=2;
            enPassantRow = 4;
            if(nextRow == 7) {
                promotable = true;
            }
        }

        boolean leftEdge = col == 0;
        boolean rightEdge = col == 7;


        //forward
        if(isDestinationEmpty(nextRow, col)) {
            moves.add(new int[]{nextRow, col});
            if((isWhite && row == 6) || (!isWhite && row == 1) && isDestinationEmpty(doubleJump, col)) { //if at starting square and nothing in front
                moves.add(new int[]{doubleJump, col}); //can jump 2 squares
            }
        }

        if(!leftEdge && isDestOpposing(nextRow, col-1)) {
            moves.add(new int[]{nextRow, col-1});
        }

        if(!rightEdge && isDestOpposing(nextRow, col+1)) {
            moves.add(new int[]{nextRow, col+1});
        }


        if(row == enPassantRow) { //en passant behavior
            if(!leftEdge && isDestOpposing(row, col-1) && (game.board[row][col-1] instanceof Pawn)) {
//                    System.out.println("to the left is a pawn"); //TODO check if last move from opponent was 2 squares
            }
            if(!rightEdge && isDestOpposing(row, col+1) && (game.board[row][col+1] instanceof Pawn)) {
//                    System.out.println("to the right is a pawn");
            }
        }
        return moves;
    }
}
