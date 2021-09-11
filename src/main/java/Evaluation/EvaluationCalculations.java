package Evaluation;

import BoardRepresentation.BoardState;
import DataTypes.*;

import java.util.Map;

public class EvaluationCalculations {

    BoardState state;
    EvaluationValues values = new EvaluationValues();

    public int calculateMaterial(BoardState state) {
        this.state = state;
        int centralControl = piecesInMiddle();
        int development = development();
        return centralControl + development + materialValue();
    }

    private int piecesInMiddle() {
        Board board = state.board;
        int value = 0;
        for (int row = 2; row < 6; row++) {
            for (int column = 2; column < 6; column++) {
                Piece piece = board.getCoordinate(row, column);
                if (piece != null) {
                    if (piece.getColour() == Colour.BLACK) {
                        value -= 20;
                    } else {
                        value += 20;
                    }
                }
            }
        }
        return value;
    }

//    todo rework
//    private int kingSafety() {
//        Board board = state.board;
//        int value = 0;
//        if (state.turnNumber < 20) {
//            if (data[0][2] == 'k') { //black king queenSide
//                for (int i = 0; i < 3; i++) {
//                    if (data[1][i] != 'p') break;
//                    if (i == 2) value -= 50;
//                }
//            }
//            if (data[7][2] == 'K') { //white king queenSide
//                for (int i = 0; i < 3; i++) {
//                    if (data[6][i] != 'P') break;
//                    if (i == 2) value += 50;
//                }
//            }
//            if (data[0][6] == 'k') { //black king kingSide
//                for (int i = 5; i < 8; i++) {
//                    if (data[1][i] != 'p') break;
//                    if (i == 7) value -= 80;
//                }
//            }
//            if (data[7][6] == 'k') { //white king kingSide
//                for (int i = 5; i < 8; i++) {
//                    if (data[6][i] != 'p') break;
//                    if (i == 7) value += 80;
//                }
//            }
//        }
//        return value;
//    }

    private int development() {
        Board board = state.board;
        int value = 0;
        Piece knight = new Piece('N');
        Piece bishop = new Piece('B');
        int whiteRow = 7;
        int blackRow = 0;
        //developing knight is good for black etc
        value += getDevelopmentValue(board, knight, bishop, whiteRow);

        knight.setColour(Colour.BLACK);
        bishop.setColour(Colour.BLACK);

        value += -getDevelopmentValue(board, knight, bishop, blackRow);

        return value;
    }

    private int getDevelopmentValue(Board board, Piece knight, Piece bishop, int row) {
        int value = 0;
        if(isPieceAtSquare(row, 1, knight, board)) value += 15;
        if(isPieceAtSquare(row, 2, bishop, board)) value += 15;
        if(isPieceAtSquare(row, 5, bishop, board)) value += 15;
        if(isPieceAtSquare(row, 6, knight, board)) value += 15;
        return value;
    }

    private boolean isPieceAtSquare(int row, int column, Piece piece, Board board) {
        Piece comparison = board.getCoordinate(row, column);
        if(comparison == null) return false;
        return comparison.equals(piece);
    }

    public int pieceToValue(Piece piece) {
        if(piece == null) return 0;
        int result = 0;
        switch (piece.getType()) {
            case PAWN: result = values.ePawn; break;
            case ROOK: result = values.eRook; break;
            case BISHOP: result = values.eBishop; break;
            case QUEEN: result = values.eQueen; break;
            case KNIGHT: result = values.eKnight; break;
        }
        if(!piece.isWhite()) {
            result = -result; // negate value for black pieces
        }
        return result;
    }

    private int materialValue() {
        int result = 0;
        for (Map.Entry<Piece, Integer> entry: state.pieceMap.entrySet()) {
            result += pieceToValue(entry.getKey()) * entry.getValue();
        }
        return result;
    }
}
