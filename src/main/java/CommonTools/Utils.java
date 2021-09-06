package CommonTools;

import BoardRepresentation.BoardState;
import DataTypes.*;

public class Utils {

    private final static String default_fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    static public String toFenString(BoardState boardState) {
        Board board = boardState.board;
        StringBuilder result = new StringBuilder();

        for (int row = 0; row < 8; row++) {
            int empty = 0;
            for (int column = 0; column < 8; column++) {
                Piece piece = board.getCoordinate(row, column);
                if(piece == null) {
                    empty++;
                } else if(empty != 0) {
                    result.append(empty);
                    empty = 0;
                    result.append(piece.toChar());
                } else {
                    result.append(piece.toChar());
                }
            }
            if(empty != 0) {
                result.append(empty);
            }
            if(row != 7) result.append("/");
        }

        result.append(boardState.turn == PlayerTurn.BLACK ? " b" : " w");

        String WhiteCastlingString = "";
        switch (board.getWhiteCastling()) {
            case KING: WhiteCastlingString = "K"; break;
            case QUEEN: WhiteCastlingString = "Q"; break;
            case BOTH: WhiteCastlingString = "KQ"; break;
        }

        String BlackCastlingString = "";
        switch (board.getBlackCastling()) {
            case KING: BlackCastlingString = "k"; break;
            case QUEEN: BlackCastlingString = "q"; break;
            case BOTH: BlackCastlingString = "kq"; break;
        }

        if(WhiteCastlingString.length() < 1 && BlackCastlingString.length() < 1) {
            result.append(" - ");
        } else {
            result.append(" ");
            result.append(WhiteCastlingString);
            result.append(BlackCastlingString);
            result.append(" ");
        }

        if(boardState.enPassant != null) {
            result.append(boardState.enPassant);
        } else {
            result.append("-");
        }

        result.append(" ").append(boardState.halfMove).append(" ");
        result.append(boardState.turnNumber);
        return result.toString();
    }

    static public BoardState parseFen(String fen) {

        /*
        split data indexes
        0 = fen board data
        1 = white or black turn
        2 = castling rights
        3 = en passant move
        4 = half-move clock (how many turns since last capture or pawn move 50 move rule)
        5 = full-move number starts at 1 incremented after blacks move or at the start of white move
        */
        String [] split = fen.split(" ");

        BoardState boardState = new BoardState();

        if(split.length != 6) return new BoardState(parseFen(default_fen)); //todo give out errors
        String [] rows = split[0].split("/");
        if(rows.length != 8) return new BoardState(parseFen(default_fen));

        if(split[1].equals("w")) {
            boardState.turn = PlayerTurn.WHITE;
        } else if (split[1].equals("b")) {
            boardState.turn = PlayerTurn.BLACK;
        }

        boardState.setCastling(split[2].toCharArray()); //set castling rights
        boardState.turnNumber = Integer.parseInt(split[5]);

        boardState.enPassant = split[3].length() == 2 ? new Coordinate(split[3]) : null;

        // Add pieces to the board
        for (int row = 0; row < rows.length; row++) {
            boardState.addRow(rows[row], row);
        }

        return boardState;
    }

    static public void printBoard(BoardState board) {
        System.out.println(Utils.toFenString(board));
        System.out.println(getVisualBoardString(board.board));
    }

    static public String getVisualBoardString(Board board) {
        String divider = "=|-----|-----|-----|-----|-----|-----|-----|-----|=\n";
        StringBuilder str = new StringBuilder();
        str.append("    0     1     2     3     4     5     6     7\n");
        for(int row = 0; row < 8; row++) {
            str.append(divider).append(row);
            for(int column = 0; column < 8; column++) {
                Piece piece = board.getCoordinate(row, column);
                if(piece != null) {
                    str.append("|  ").append(piece.toChar());
                } else {
                    str.append("|   ");
                }
                if(column != 7) str.append("  ");
                else str.append("  |");
            }
            str.append(8-row);
            str.append("\n");
        }
        str.append(divider);
        str.append("    a     b     c     d     e     f     g     h\n");

        return str.toString();
    }
}