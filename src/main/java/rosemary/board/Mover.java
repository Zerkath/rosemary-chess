package rosemary.board;

import rosemary.types.*;
import rosemary.types.CastlingRights;

public class Mover {

    public static BoardState makeMoves(BoardState bs, String[] moves) {
        short[] m = new short[moves.length];
        for (int i = 0; i < moves.length; i++) {
            m[i] = MoveUtil.getMove(moves[i]);
        }
        return makeMoves(bs, m);
    }

    public static BoardState makeMoves(BoardState bs, short[] moves) {
        BoardState boardState = bs;
        for (short move : moves) {
            boardState = makeMove(boardState, move);
        }
        return boardState;
    }

    public static BoardState makeMove(BoardState boardState, short moveWithPromotionData) {
        BoardState bs = new BoardState(boardState);

        short move = MoveUtil.clearPromotion(moveWithPromotionData);
        byte temp_destination = MoveUtil.getDestination(move);
        byte temp_origin = MoveUtil.getOrigin(move);
        int dRow = MoveUtil.getRow(temp_destination);
        int dCol = MoveUtil.getColumn(temp_destination);

        byte selected = bs.getCoordinate(temp_origin);

        boolean isBeingPromoted = MoveUtil.getPromotion(moveWithPromotionData) != 0;
        boolean isWhite = Pieces.isWhite(selected);

        checkForCastlingRights(bs, moveWithPromotionData);

        if (Pieces.getType(selected) == Pieces.PAWN || bs.getCoordinate(MoveUtil.getDestination(move)) != 0) {
            bs.setHalfMove((byte) 0);
        } else {
            bs.setHalfMove((byte) (bs.getHalfMove() + 1));
        }

        if (Pieces.getType(selected) == Pieces.PAWN
                && bs.getEnPassant() != -1
                && MoveUtil.getRow(bs.getEnPassant()) == MoveUtil.getRow(temp_destination)
                && MoveUtil.getColumn(bs.getEnPassant()) == MoveUtil.getColumn(temp_destination)) {
            int offSet = isWhite ? 1 : -1;
            short row = (short) (MoveUtil.getRow(bs.getEnPassant()) + offSet);
            short col = MoveUtil.getColumn(bs.getEnPassant());
            bs.setCoordinate(Utils.getCoordinate(row, col), (byte) 0);
        }

        bs.setEnPassant((byte) -1);
        // add En passant
        if (Pieces.getType(selected) == Pieces.PAWN
                && ((MoveUtil.getRow(temp_origin) == 6 && MoveUtil.getRow(temp_destination) == 4)
                        || (MoveUtil.getRow(temp_origin) == 1 && MoveUtil.getRow(temp_destination) == 3))) {

            int right = 0;
            int left = 0;
            if (dCol == 0) right = bs.getCoordinate(Utils.getCoordinate(dRow, dCol + 1));
            if (dCol == 7) left = bs.getCoordinate(Utils.getCoordinate(dRow, dCol - 1));
            if (dCol > 0 && dCol < 7) {
                right = bs.getCoordinate(Utils.getCoordinate(dRow, dCol + 1));
                left = bs.getCoordinate(Utils.getCoordinate(dRow, dCol - 1));
            }

            if (Pieces.getType(right) == Pieces.PAWN) {
                addEnPassantMove(bs, isWhite, right, dCol, dRow);
            }

            if (Pieces.getType(left) == Pieces.PAWN) {
                addEnPassantMove(bs, isWhite, left, dCol, dRow);
            }
        }

        // Castling
        if (Pieces.getType(selected) == Pieces.KING
                && MoveUtil.getColumn(temp_origin) == 4
                && ((MoveUtil.getRow(temp_origin) == 0 && dRow == 0) || MoveUtil.getRow(temp_origin) == 7 && dRow == 7)
                && (dCol == 2 || dCol == 6)) {

            bs.setCastling(CastlingRights.NONE, isWhite);

            byte rook;
            byte destination;
            byte origin;
            if (dCol == 2) {
                destination = Utils.getCoordinate(dRow, 3);
                origin = Utils.getCoordinate(dRow, 0);
            } else {
                destination = Utils.getCoordinate(dRow, 5);
                origin = Utils.getCoordinate(dRow, 7);
            }
            rook = bs.getCoordinate(origin);
            bs.replaceCoordinate(destination, rook);
            bs.setCoordinate(origin, (byte) 0);
        }

        if (Pieces.getType(selected) == Pieces.KING) {
            bs.setCastling(CastlingRights.NONE, isWhite);
        }

        bs.setCoordinate(temp_origin, (byte) 0);

        byte piece = selected;
        if (isBeingPromoted) {
            piece = Pieces.getPromotionNum(MoveUtil.getPromotion(moveWithPromotionData));
        }

        bs.replaceCoordinate(temp_destination, piece);

        if (!bs.isWhiteTurn()) bs.setTurnNumber((byte) (bs.getTurnNumber() + 1));

        bs.setWhiteTurn(!bs.isWhiteTurn());
        return bs;
    }

    private static void checkForCastlingRights(BoardState bs, short move) {
        short destination = MoveUtil.getDestination(move);
        short origin = MoveUtil.getOrigin(move);

        short oRow = MoveUtil.getRow(origin);
        short oCol = MoveUtil.getColumn(origin);
        short dRow = MoveUtil.getRow(destination);
        short dCol = MoveUtil.getColumn(destination);

        byte blackCastling = bs.getBlackCastling();
        byte whiteCastling = bs.getWhiteCastling();

        if (oRow == 0 && oCol == 0 || dRow == 0 && dCol == 0) { // black queen side rook
            if (blackCastling == CastlingRights.BOTH) {
                bs.setBlackCastling(CastlingRights.KING);
            } else if (blackCastling == CastlingRights.QUEEN) {
                bs.setBlackCastling(CastlingRights.NONE);
            }
        } else if (oRow == 0 && oCol == 7 || dRow == 0 && dCol == 7) { // black king side rook
            if (blackCastling == CastlingRights.BOTH) {
                bs.setBlackCastling(CastlingRights.QUEEN);
            } else if (blackCastling == CastlingRights.KING) {
                bs.setBlackCastling(CastlingRights.NONE);
            }
        } else if (oRow == 7 && oCol == 0 || dRow == 7 && dCol == 0) { // white queen side rook
            if (whiteCastling == CastlingRights.BOTH) {
                bs.setWhiteCastling(CastlingRights.KING);
            } else if (whiteCastling == CastlingRights.QUEEN) {
                bs.setWhiteCastling(CastlingRights.NONE);
            }
        } else if (oRow == 7 && oCol == 7 || dRow == 7 && dCol == 7) { // white king side rook
            if (whiteCastling == CastlingRights.BOTH) {
                bs.setWhiteCastling(CastlingRights.QUEEN);
            } else if (whiteCastling == CastlingRights.KING) {
                bs.setWhiteCastling(CastlingRights.NONE);
            }
        }
    }

    private static void addEnPassantMove(BoardState bs, boolean white, int piece, int dCol, int dRow) {
        boolean pieceIsWhite = Pieces.isWhite(piece);
        if (white && !pieceIsWhite) {
            bs.setEnPassant(Utils.getCoordinate(dRow + 1, dCol));
        }
        if (!white && pieceIsWhite) {
            bs.setEnPassant(Utils.getCoordinate(dRow - 1, dCol));
        }
    }
}
