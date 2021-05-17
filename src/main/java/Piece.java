import java.util.LinkedList;

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

    public abstract LinkedList<int[]> getPossibleMoves();

    /**
     * Checks if destination square is within bounds of the game board array
     * Since it's 8x8, it checks both row and col values if it's between indexes 0 and 7
     * @param row destination row
     * @param col destination column
     * @return list of moves int [] where index 0 is row and index 1 is column
     */
    boolean isDestinationSquareOnBoard(int row, int col) {
        return row >= 0 && row <= 7 && col >= 0 && col <= 7;
    }

    /**
     * Checks if destination square is empty or the piece is of opposing color
     * @param row destination row
     * @param col destination column
     * @return true if the square is empty or if the square is opposing color
     */
    boolean isDestinationPieceValid(int row, int col) {
        if(game.board[row][col] == null) {
            return true;
        } else return game.board[row][col].isWhite != this.isWhite;
    }

    boolean isDestinationEmpty(int row, int col) {
        if(isDestinationSquareOnBoard(row, col)) {
            return game.board[row][col] == null;
        } else return false;
    }

    boolean isDestOpposing(int row, int col) {
        if(isDestinationSquareOnBoard(row, col)) {
            if(isDestinationEmpty(row, col)) return false;
            return game.board[row][col].isWhite != this.isWhite;
        } else return false;
    }
    /**
     * First part of the if checks whether destination square is on board
     * Second part returns true if destination square is empty or it's piece is of opposing color
     * @param row destination row
     * @param col destination column
     * @return true if move is coords are on the board and the square is empty or the opposing color
     */
    boolean isMovePossible(int row, int col) {
        return isDestinationSquareOnBoard(row, col) && isDestinationPieceValid(row, col);
    }

    public void updatePosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public void setGameState(Game game) {
        this.game = game;
    }

    public char getFenSymbol() {
        return fenSymbol;
    }
}
