import java.util.ArrayList;
public abstract class Piece {
    boolean isWhite;
    // boolean pinned;
    int row;
    int col;
    Game game;
    char fenSymbol;

    public Piece(int row, int col, boolean isWhite) {
        this.row = row;
        this.col = col;
        this.isWhite = isWhite;
    }

    public abstract ArrayList<int[]> getPossibleMoves();

    /**
     * Checks if destination square is within bounds of the game board array
     * Since it's 8x8, it checks both row and col values if it's between indexes 0 and 7
     * @param row destination row
     * @param col destination column
     * @return
     */
    boolean isDestinationSquareOnBoard(int row, int col) {
        if(row >= 0 && row <= 7 && col >= 0 && col <= 7) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if destination square is empty or the piece is of opposing color
     * @param row destination row
     * @param col destination column
     * @return
     */
    boolean isDestinationPieceValid(int row, int col) {
        if(game.board[row][col] == null) {
            return true;
        } else if (game.board[row][col].isWhite != this.isWhite) {
            return true;
        } else {
            return false;
        }
    }

    public void gameBegins(Game game) {
        this.game = game;
    }

    public char getFenSymbol() {
        return fenSymbol;
    }
}
