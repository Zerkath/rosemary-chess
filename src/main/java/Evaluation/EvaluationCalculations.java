package Evaluation;

import BoardRepresentation.BoardState;
import DataTypes.*;

public class EvaluationCalculations {

    BoardState state;
    EvaluationValues values = new EvaluationValues();

    public int calculateMaterial(BoardState state) {
        state.updatePieceCount();
        this.state = state;
        int negation = state.turn == PlayerTurn.BLACK ? -1 : 1;
        int centralControl = piecesInMiddle();
        int materialAdvantage = calculateMaterial();
//        int kingSafety = kingSafety();
        int development = development();
        return materialAdvantage + centralControl + development + (int)(5 * (1 + Math.random())); //slight random, doesnt play the same move always?
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
        Piece knight = new Piece('n');
        Piece bishop = new Piece('b');
        int whiteRow = 7;
        int blackRow = 0;
        //developing knight is good for black etc
        value = getDevelopmentValue(board, value, knight, bishop, whiteRow);

        knight.setColour(Colour.WHITE);
        bishop.setColour(Colour.WHITE);

        value = getDevelopmentValue(board, value, knight, bishop, blackRow);

        return value;
    }

    private int getDevelopmentValue(Board board, int value, Piece knight, Piece bishop, int blackRow) {
        if(isPieceAtSquare(blackRow, 1, knight, board)) value += 15;
        if(isPieceAtSquare(blackRow, 2, bishop, board)) value += 15;
        if(isPieceAtSquare(blackRow, 5, bishop, board)) value += 15;
        if(isPieceAtSquare(blackRow, 6, knight, board)) value += 15;
        return value;
    }

    private boolean isPieceAtSquare(int row, int column, Piece piece, Board board) {
        Piece comparison = board.getCoordinate(row, column);
        if(comparison == null) return false;
        return comparison.equals(piece);
    }

    public int calculateMaterial() { //+ if white, -if black
        int result = 0;
        int[] arr = state.pieces;
        result += arr[3] * values.ePawn;
        result -= arr[4] * values.ePawn;
        result += arr[5] * values.eBishop;
        result -= arr[6] * values.eBishop;
        result += arr[7] * values.eRook;
        result -= arr[8] * values.eRook;
        result += arr[9] * values.eKnight;
        result -= arr[10] * values.eKnight;
        result += arr[11] * values.eQueen;
        result -= arr[12] * values.eQueen;
        return result;
    }
}
