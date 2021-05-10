import java.util.ArrayList;
public class Game {
    Piece[][] board = new Piece[8][8];
    enum CastlingRights {
        QUEENSIDE,
        KIGNSIDE,
        BOTH,
        NONE
    }

    CastlingRights whiteCastling = CastlingRights.NONE;
    CastlingRights blackCastling = CastlingRights.NONE;

    public void parseFen(String fen) {

        /*
        split data indexes
        0 = fen board data
        1 = white or black turn
        2 = castling rights
        3 = en passant move
        4 = half-move clock (how many turns since last capture or pawn move 50 move rule)
        5 = fullmove number starts at 1 incremented after blacks move or at the start of white move
        */
        String [] split = fen.split(" ");

        if(split.length != 6) return; //check if fen is somewhat valid
        String [] rows = split[0].split("/");
        if(rows.length != 8) return; //another validity check

        setCastling(split[2].toCharArray()); //set castling rights

        
        // Add pieces to the board
        for (int i = 0; i < rows.length; i++) {
            addRow(rows[i], i);
        }
        for (Piece[] row : board) {
            for (Piece piece : row) {
                if(piece != null) piece.gameBegins(this);
            }
        }
    }

    public void setCastling(char [] castling) {
        for (char c : castling) {
            boolean black = Character.isLowerCase(c);
            boolean queen = Character.toLowerCase(c) == 'q';
            if(black) {
                if(blackCastling == CastlingRights.NONE) {
                    blackCastling = queen ? CastlingRights.QUEENSIDE : CastlingRights.KIGNSIDE;
                } else if(blackCastling == CastlingRights.BOTH) {
                    break;
                } else {
                    if(blackCastling == CastlingRights.QUEENSIDE) {
                        if(!queen) blackCastling = CastlingRights.BOTH;
                    } else {
                        if(queen) blackCastling = CastlingRights.BOTH;
                    }
                }
            } else {
                if(whiteCastling == CastlingRights.NONE) {
                    whiteCastling = queen ? CastlingRights.QUEENSIDE : CastlingRights.KIGNSIDE;
                } else if(whiteCastling == CastlingRights.BOTH) {
                    break;
                } else {
                    if(whiteCastling == CastlingRights.QUEENSIDE) {
                        if(!queen) whiteCastling = CastlingRights.BOTH;
                    } else {
                        if(queen) whiteCastling = CastlingRights.BOTH;
                    }
                }
            }
        }
    }

    public void printPieceLegalMove(int row, int col) {
        if(this.board[row][col] == null) {
            System.out.println("No Piece");
            return;
        }
        System.out.print(this.board[row][col].getClass() + "\n");
        ArrayList<int[]> moves = this.board[row][col].getPossibleMoves();
        int c = 1;
        for (int[] move : moves) {
            System.out.println("Move " + c + "\t" + move[0] + " " + move[1]);
            c++;
        }
    }

    public void printBoard() {

        System.out.println("  0 1 2 3 4 5 6 7");
        for(int i = 0; i < board.length; i++) {
            System.out.print(i + " ");
            for(int j = 0; j < board[i].length; j++) {
                Piece piece = board[i][j];
                if(piece != null) {
                    System.out.print(piece.getFenSymbol() + " ");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }

    /**
     * used to add rows of FEN data to the board state
     * @param row a row of FEN
     * @param index which row to place the fen
     */
    public void addRow(String row, int index) {
        int c = 0;
        for (Character ch : row.toCharArray()) {
            if(Character.isDigit(ch)) {
                int numOfEmpty = Character.digit(ch, 10);
                for (int j = 0; j < numOfEmpty; j++) {
                    board[index][c] = null;
                    c++;
                }
            } else {
                board[index][c] = characterToPiece(ch, index, c);
                c++;
            }
        }
    }


    public Piece characterToPiece(char ch, int row, int col) {
        boolean isWhite = Character.isUpperCase(ch);

        switch(Character.toLowerCase(ch)) {
            case 'p': return new Pawn(row, col, isWhite);
            case 'b': return new Bishop(row, col, isWhite);
            case 'n': return new Knight(row, col, isWhite);
            case 'r': return new Rook(row, col, isWhite);
            case 'q': return new Queen(row, col, isWhite);
            case 'k': return new King(row, col, isWhite);
            default: return null;
        }
    }
}
