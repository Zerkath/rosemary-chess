import java.util.LinkedList;

public class BoardState {
    char[][] board = new char[8][8];
    PlayerTurn turn;


    CastlingRights whiteCastling = CastlingRights.NONE;
    CastlingRights blackCastling = CastlingRights.NONE;

    int turnNumber = 1;
    int halfMove = 0;

    public BoardState() {

    }
    public BoardState(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, this.board[i], 0, board.length);
        }
    }

    public LinkedList<Move> pieceToMoves(char ch, Coordinate coord) {
        LinkedList<Move> result = new LinkedList<>();

        return result;
    }
}
