package rosemary.eval;

import rosemary.board.BoardState;
import rosemary.types.*;

public class EvaluationCalculations {

    public static int calculateMaterial(BoardState state) {
        return piecesInMiddle(state) + materialValue(state.getPieceMap()) + development(state);
    }

    private static int piecesInMiddle(BoardState state) {
        int value = 0;
        for (int row = 2; row < 6; row++) {
            for (int column = 2; column < 6; column++) {
                int piece = state.getBoard()[Utils.getCoordinate(row, column)];
                if (piece != 0) {
                    value += Pieces.isWhite(piece) ? 25 : -20;
                }
            }
        }
        return value;
    }

    private static int development(BoardState state) {
        int value = 0;
        int knight = Pieces.KNIGHT | Pieces.WHITE;
        int bishop = Pieces.BISHOP | Pieces.WHITE;
        int whiteRow = 7;
        int blackRow = 0;
        // developing knight is good for black etc
        value -= getDevelopmentValue(state, knight, bishop, whiteRow);
        knight = Pieces.KNIGHT | Pieces.BLACK;
        bishop = Pieces.BISHOP | Pieces.BLACK;
        value += getDevelopmentValue(state, knight, bishop, blackRow);

        return value;
    }

    private static int getDevelopmentValue(BoardState state, int knight, int bishop, int row) {
        int value = 0;
        if (isPieceAtSquare(row, 1, knight, state.getBoard())) value += 35;
        if (isPieceAtSquare(row, 2, bishop, state.getBoard())) value += 35;
        if (isPieceAtSquare(row, 5, bishop, state.getBoard())) value += 35;
        if (isPieceAtSquare(row, 6, knight, state.getBoard())) value += 35;
        return value;
    }

    private static boolean isPieceAtSquare(int row, int column, int piece, byte[] board) {
        int comparison = board[Utils.getCoordinate(row, column)];
        if (comparison == 0) return false;
        return comparison == piece;
    }

    public static int pieceToValue(int piece) {
        if (piece == 0) return 0;
        int result = 0;
        switch (Pieces.getType(piece)) {
            case Pieces.PAWN:
                result = EvaluationValues.ePawn;
                break;
            case Pieces.ROOK:
                result = EvaluationValues.eRook;
                break;
            case Pieces.BISHOP:
                result = EvaluationValues.eBishop;
                break;
            case Pieces.QUEEN:
                result = EvaluationValues.eQueen;
                break;
            case Pieces.KNIGHT:
                result = EvaluationValues.eKnight;
                break;
        }
        return Pieces.isWhite(piece) ? result : -result;
    }

    private static int materialValue(byte[] pieceMap) {
        int result = 0;

        for (int i = 0; i < 23; i++) {
            if (pieceMap[i] == 0) continue;
            result += pieceToValue(i) * pieceMap[i];
        }
        return result;
    }
}
