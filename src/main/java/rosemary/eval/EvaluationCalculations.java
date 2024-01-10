package rosemary.eval;

import java.util.Map;
import rosemary.board.BoardState;
import rosemary.types.*;

public class EvaluationCalculations {

    BoardState state;
    EvaluationValues values = new EvaluationValues();

    public int calculateMaterial(BoardState state) {
        this.state = state;
        return piecesInMiddle() + materialValue(state.getPieceMap()) + development();
    }

    private int piecesInMiddle() {
        Board board = state.board;
        int value = 0;
        for (int row = 2; row < 6; row++) {
            for (int column = 2; column < 6; column++) {
                int piece = board.getCoordinate(row, column);
                if (piece != 0) {
                    value += Pieces.isWhite(piece) ? 25 : -20;
                }
            }
        }
        return value;
    }

    private int development() {
        Board board = state.board;
        int value = 0;
        int knight = Pieces.KNIGHT | Pieces.WHITE;
        int bishop = Pieces.BISHOP | Pieces.WHITE;
        int whiteRow = 7;
        int blackRow = 0;
        // developing knight is good for black etc
        value -= getDevelopmentValue(board, knight, bishop, whiteRow);
        knight = Pieces.KNIGHT | Pieces.BLACK;
        bishop = Pieces.BISHOP | Pieces.BLACK;
        value += getDevelopmentValue(board, knight, bishop, blackRow);

        return value;
    }

    private int getDevelopmentValue(Board board, int knight, int bishop, int row) {
        int value = 0;
        if (isPieceAtSquare(row, 1, knight, board)) value += 35;
        if (isPieceAtSquare(row, 2, bishop, board)) value += 35;
        if (isPieceAtSquare(row, 5, bishop, board)) value += 35;
        if (isPieceAtSquare(row, 6, knight, board)) value += 35;
        return value;
    }

    private boolean isPieceAtSquare(int row, int column, int piece, Board board) {
        int comparison = board.getCoordinate(row, column);
        if (comparison == 0) return false;
        return comparison == piece;
    }

    public int pieceToValue(int piece) {
        if (piece == 0) return 0;
        int result = 0;
        switch (Pieces.getType(piece)) {
            case Pieces.PAWN:
                result = values.ePawn;
                break;
            case Pieces.ROOK:
                result = values.eRook;
                break;
            case Pieces.BISHOP:
                result = values.eBishop;
                break;
            case Pieces.QUEEN:
                result = values.eQueen;
                break;
            case Pieces.KNIGHT:
                result = values.eKnight;
                break;
        }
        return Pieces.isWhite(piece) ? result : -result;
    }

    private int materialValue(Map<Byte, Byte> pieceMap) {
        int result = 0;
        for (Map.Entry<Byte, Byte> entry : pieceMap.entrySet()) {
            result += pieceToValue(entry.getKey()) * entry.getValue();
        }
        return result;
    }
}
