package rosemary.board;

import rosemary.types.CastlingRights;
import rosemary.types.Pieces;
import rosemary.types.Utils;

public class FenUtils {

    public static final String default_fen =
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public static BoardState parseFen(String fen) {

        /*
         * split data indexes
         * 0 = fen board data
         * 1 = white or black turn
         * 2 = castling rights
         * 3 = en passant move
         * 4 = half-move clock (how many turns since last capture or pawn move 50 move
         * rule)
         * 5 = full-move number starts at 1 incremented after blacks move or at the
         * start of white move
         */
        String[] split = fen.split(" ");

        BoardState boardState = new BoardState();

        if (split.length != 6) return new BoardState(parseFen(default_fen)); // todo give out errors
        String[] rows = split[0].split("/");
        if (rows.length != 8) return new BoardState(parseFen(default_fen));

        boardState.setWhiteTurn(split[1].equals("w"));

        boardState.setCastling(split[2].toCharArray()); // set castling rights
        boardState.setTurnNumber((byte) Integer.parseInt(split[5]));

        boardState.setEnPassant(split[3].length() == 2 ? Utils.getCoordinate(split[3]) : (byte) -1);

        for (int row = 0; row < rows.length; row++) {
            boardState.addRow(rows[row], row);
        }

        return boardState;
    }

    private static StringBuilder strBuilder = new StringBuilder();

    public static String getFenString(BoardState boardState) {
        strBuilder.setLength(0);
        byte[] board = boardState.getBoard();

        for (int row = 0; row < 8; row++) {
            int empty = 0;
            for (int column = 0; column < 8; column++) {
                int piece = board[Utils.getCoordinate(row, column)];
                if (piece == 0) {
                    empty++;
                } else if (empty != 0) {
                    strBuilder.append(empty);
                    empty = 0;
                    strBuilder.append(Pieces.getChar(piece));
                } else strBuilder.append(Pieces.getChar(piece));
            }
            if (empty != 0) strBuilder.append(empty);
            if (row != 7) strBuilder.append("/");
        }

        strBuilder.append(boardState.isWhiteTurn() ? " w" : " b");

        String WhiteCastlingString =
                switch (boardState.getWhiteCastling()) {
                    case CastlingRights.KING -> "K";
                    case CastlingRights.QUEEN -> "Q";
                    case CastlingRights.BOTH -> "KQ";
                    default -> "";
                };

        String BlackCastlingString =
                switch (boardState.getBlackCastling()) {
                    case CastlingRights.KING -> "k";
                    case CastlingRights.QUEEN -> "q";
                    case CastlingRights.BOTH -> "kq";
                    default -> "";
                };

        if (WhiteCastlingString.length() < 1 && BlackCastlingString.length() < 1) {
            strBuilder.append(" - ");
        } else {
            strBuilder
                    .append(" ")
                    .append(WhiteCastlingString)
                    .append(BlackCastlingString)
                    .append(" ");
        }

        strBuilder.append(
                boardState.getEnPassant() != -1
                        ? Utils.coordinateToString(boardState.getEnPassant())
                        : "-");

        strBuilder
                .append(" ")
                .append(boardState.getHalfMove())
                .append(" ")
                .append(boardState.getTurnNumber());

        return strBuilder.toString();
    }
}
