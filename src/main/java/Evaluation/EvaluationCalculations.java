package Evaluation;

import BoardRepresentation.BoardState;
import DataTypes.PlayerTurn;

public class EvaluationCalculations {

    BoardState state;
    EvaluationValues values = new EvaluationValues();

    public int calculateMaterial(BoardState state) {
        state.updatePieceCount();
        this.state = state;
        int negation = state.turn == PlayerTurn.BLACK ? -1 : 1;
        int centralControl = piecesInMiddle();
        int materialAdvantage = calculateMaterial();
        int kingSafety = kingSafety();
        int development = development();
        return materialAdvantage + centralControl + kingSafety + development + (int)(5 * (1 + Math.random())); //slight random, doesnt play the same move always?
    }

    private int piecesInMiddle() {
        char[][] data = state.board;
        int value = 0;
        for (int i = 2; i < 6; i++) {
            for (int j = 2; j < 6; j++) {
                char piece = data[i][j];
                if (piece != '-') {
                    if (Character.isLowerCase(piece)) {
                        value -= 20;
                    } else {
                        value += 20;
                    }
                }
            }
        }
        return value;
    }

    private int kingSafety() {
        char[][] data = state.board;
        int value = 0;
        if (state.turnNumber < 20) {
            if (data[0][2] == 'k') { //black king queenSide
                for (int i = 0; i < 3; i++) {
                    if (data[1][i] != 'p') break;
                    if (i == 2) value -= 50;
                }
            }
            if (data[7][2] == 'K') { //white king queenSide
                for (int i = 0; i < 3; i++) {
                    if (data[6][i] != 'P') break;
                    if (i == 2) value += 50;
                }
            }
            if (data[0][6] == 'k') { //black king kingSide
                for (int i = 5; i < 8; i++) {
                    if (data[1][i] != 'p') break;
                    if (i == 7) value -= 80;
                }
            }
            if (data[7][6] == 'k') { //white king kingSide
                for (int i = 5; i < 8; i++) {
                    if (data[6][i] != 'p') break;
                    if (i == 7) value += 80;
                }
            }
        }
        return value;
    }

    private int development() {
        char[][] data = state.board;
        int value = 0;
        char knight = 'n';
        char bishop = 'b';
        int whiteRow = 7;
        int blackRow = 0;
        if(data[blackRow][1] == knight) value += 15; //developing knight is good for black etc
        if(data[blackRow][2] == bishop) value += 17;
        if(data[blackRow][5] == bishop) value += 20;
        if(data[blackRow][6] == knight) value += 16;

        knight = 'N';
        bishop = 'B';
        if(data[whiteRow][1] == knight) value -= 15; //developing knight is good for white etc
        if(data[whiteRow][2] == bishop) value -= 17;
        if(data[whiteRow][5] == bishop) value -= 20;
        if(data[whiteRow][6] == knight) value -= 16;

        return value;
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
