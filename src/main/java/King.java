import java.util.LinkedList;

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
    public LinkedList<int[]> getPossibleMoves() {

        LinkedList<int[]> moves = new LinkedList<>();


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

        //castling
        if(col == 4) { //only check if the king is in the original position and hasn't moved
            Piece [] backRow = game.board[row];

            boolean qSide = backRow[0] instanceof Rook &&
                    ((this.isWhite && backRow[0].isWhite) || (!this.isWhite && !backRow[0].isWhite)) &&
                    backRow[1] == null &&
                    backRow[2] == null &&
                    backRow[3] == null;

            boolean kSide = backRow[7] instanceof Rook && ((this.isWhite && backRow[7].isWhite) || (!this.isWhite && !backRow[7].isWhite)) &&
                    backRow[6] == null &&
                    backRow[5] == null;

            if(isWhite && row == 7 && game.turn == Game.PlayerTurn.WHITE) {
                switch (game.whiteCastling) {
                    case BOTH: {
                        if(qSide) moves.add(new int[]{row, 2 }); //queen side
                        if(kSide) moves.add(new int[]{row, 6 }); //king side
                        break;
                    }
                    case KINGSIDE: {
                        if(kSide) moves.add(new int[]{row, 6});
                        break;
                    }
                    case QUEENSIDE: {
                        if(qSide) moves.add(new int[]{row, 2});
                        break;
                    }
                }
            }
            if(!isWhite && row == 0 && game.turn == Game.PlayerTurn.BLACK) {
                switch (game.blackCastling) {
                    case BOTH: {
                        if(qSide) moves.add(new int[]{row, 2 }); //queen side
                        if(kSide) moves.add(new int[]{row, 6 }); //king side
                        break;
                    }
                    case KINGSIDE: {
                        if(kSide) moves.add(new int[]{row, 6});
                        break;
                    }
                    case QUEENSIDE: {
                        if(qSide) moves.add(new int[]{row, 2});
                        break;
                    }
                }
            }
        }
        return moves;
    }
}
