import java.util.ArrayList;
public class Game {
    Piece[][] board = new Piece[8][8];
    enum whiteCastling {
        QUEENSIDE,
        KIGNSIDE,
        BOTH,
        NONE
    }
    enum blackCastling {
        QUEENSIDE,
        KIGNSIDE,
        BOTH,
        NONE
    }

    public void parseFen(String fen) {
        String [] split = fen.split(" ");
        String [] rows = split[0].split("/");
        for (int i = 0; i < rows.length; i++) {
            addRow(rows[i], i);
        }
        for (Piece[] row : board) {
            for (Piece piece : row) {
                if(piece != null) piece.gameBegins(this);
            }
        }
    }
    public void printPieceLegalMove(int row, int col) {
        if(this.board[row][col] == null) {
            System.out.println("No Piece");
            return;
        }
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
            default: return null;
        }
    }
}
