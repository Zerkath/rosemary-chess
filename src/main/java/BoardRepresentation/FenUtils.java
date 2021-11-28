package BoardRepresentation;

import DataTypes.Board;
import DataTypes.Coordinate;
import DataTypes.Pieces;

public class FenUtils {
    private final static String default_fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public static BoardState parseFen(String fen) {

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


        boardState.isWhiteTurn = split[1].equals("w");

        boardState.setCastling(split[2].toCharArray()); //set castling rights
        boardState.turnNumber = Integer.parseInt(split[5]);

        boardState.enPassant = split[3].length() == 2 ? new Coordinate(split[3]) : null;

        for (int row = 0; row < rows.length; row++) {
            boardState.addRow(rows[row], row);
        }

        return boardState;
    }

    public static String getFenString(BoardState boardState) {
        StringBuilder strBuilder = new StringBuilder();
        Board board = boardState.board;

        for (int row = 0; row < 8; row++) {
            int empty = 0;
            for (int column = 0; column < 8; column++) {
                int piece = board.getCoordinate(row, column);
                if(piece == 0) {
                    empty++;
                } else if(empty != 0) {
                    strBuilder.append(empty);
                    empty = 0;
                    strBuilder.append(Pieces.getChar(piece));
                } else strBuilder.append(Pieces.getChar(piece));
            }
            if(empty != 0) strBuilder.append(empty);
            if(row != 7) strBuilder.append("/");
        }

        strBuilder.append(boardState.isWhiteTurn ? " w" : " b");

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
            strBuilder.append(" - ");
        } else {
            strBuilder.append(" ")
                    .append(WhiteCastlingString)
                    .append(BlackCastlingString)
                    .append(" ");
        }

        strBuilder.append(boardState.enPassant != null ? boardState.enPassant : "-");

        strBuilder.append(" ")
                .append(boardState.halfMove)
                .append(" ")
                .append(boardState.turnNumber);

        return strBuilder.toString();
    }
}
