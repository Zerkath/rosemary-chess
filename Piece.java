import java.util.ArrayList;
public abstract class Piece {
    boolean isWhite;
    // boolean pinned;
    int row;
    int col;
    Game game;
    public Piece(int row, int col, boolean isWhite) {
        this.row = row;
        this.col = col;
        this.isWhite = isWhite;
    }

    public abstract ArrayList<int[]> getPossibleMoves();

    public void gameBegins(Game game) {
        this.game = game;
    }
}
