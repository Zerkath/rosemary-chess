package rosemary.generation;

import rosemary.board.BoardState;
import rosemary.types.BoardUtils;
import rosemary.types.CastlingRights;
import rosemary.types.MoveUtil;
import rosemary.types.Moves;
import rosemary.types.Pieces;
import rosemary.types.Utils;

final class King {

    private static final Moves[] UNFILTERED_MOVES = new Moves[64];

    static void getMoves(short origin, BoardState boardState, Moves moves) {
        byte[] board = boardState.getBoard();
        boolean isWhiteTurn = boardState.isWhiteTurn();
        byte whiteCastling = boardState.getWhiteCastling();
        byte blackCastling = boardState.getBlackCastling();

        int originalPiece = board[origin];
        boolean isWhite = Pieces.isWhite(originalPiece);
        int kingHome = isWhite ? Utils.getCoordinate(7, 4) : Utils.getCoordinate(0, 4);

        Moves pseudoMoves = UNFILTERED_MOVES[origin];
        for (int i = 0; i < pseudoMoves.size(); i++) {
            short move = pseudoMoves.getShort(i);
            if (BoardUtils.isOpposingColourOrEmpty(
                    MoveUtil.getDestination(move), originalPiece, board)) {
                moves.add(move);
            }
        }

        // castling
        if ((isWhite && whiteCastling != CastlingRights.NONE)
                || (!isWhite && blackCastling != CastlingRights.NONE)) {
            CastlingData data = new CastlingData(isWhite, board);
            if (origin == kingHome // only check if the king is in the original position and
                    // hasn't moved
                    && !castlingStoppedByKnightOrPawn(data)
                    && !inCheckVertically(data)
                    && !inCheckDiagonally(data)
                    && !backRankThreat(data)) {

                boolean qSide = queenSidePossible(data);
                boolean kSide = kingSidePossible(data);

                byte current = 0;

                if (isWhite && isWhiteTurn) {
                    current = whiteCastling;
                }

                if (!isWhite && !isWhiteTurn) {
                    current = blackCastling;
                }

                // the king is on the e-file, so the castling targets are two files away
                short queenSideMove = MoveUtil.getMove(origin, (short) (origin - 2));
                short kingSideMove = MoveUtil.getMove(origin, (short) (origin + 2));

                switch (current) {
                    case CastlingRights.BOTH -> {
                        if (qSide) moves.add(queenSideMove);
                        if (kSide) moves.add(kingSideMove);
                    }
                    case CastlingRights.KING -> {
                        if (kSide) moves.add(kingSideMove);
                    }
                    case CastlingRights.QUEEN -> {
                        if (qSide) moves.add(queenSideMove);
                    }
                    default -> {}
                }
            }
        }
    }

    static {
        for (short origin = 0; origin < 64; origin++) {
            int row = MoveUtil.getRow(origin);
            int col = MoveUtil.getColumn(origin);
            // generating moves around king //todo update
            Moves moves = new Moves(8);
            for (int row_i = row - 1; row_i <= row + 1; row_i++) {
                for (int column_i = col - 1; column_i <= col + 1; column_i++) {
                    if (row_i != row || column_i != col) {
                        Utils.addToCollection(row_i, column_i, row, col, moves);
                    }
                }
            }
            UNFILTERED_MOVES[origin] = moves;
        }
    }

    // TODO: Should not construct this
    // even though this might reduce complexity and improve readabilty
    // currently its taking about 0.5% of allocations during perft
    // which is not a lot in comparison, but fairly heavy for little use
    private static class CastlingData {

        boolean isWhite;
        int opponentColour;
        int ownRook;
        int pawn, knight, rook, bishop, queen;
        int backRow, seventh, sixth;
        byte[] board;

        public CastlingData(boolean isWhite, byte[] board) {
            this.isWhite = isWhite;
            this.board = board;
            opponentColour = isWhite ? Pieces.BLACK : Pieces.WHITE;
            ownRook = Pieces.ROOK | (isWhite ? Pieces.WHITE : Pieces.BLACK);
            pawn = Pieces.PAWN | opponentColour;
            knight = Pieces.KNIGHT | opponentColour;
            rook = Pieces.ROOK | opponentColour;
            bishop = Pieces.BISHOP | opponentColour;
            queen = Pieces.QUEEN | opponentColour;

            backRow = isWhite ? 7 : 0;
            seventh = isWhite ? 6 : 1;
            sixth = isWhite ? 5 : 2;
        }
    }

    private static boolean castlingStoppedByKnightOrPawn(CastlingData data) {
        byte[] board = data.board;
        for (int i = 3; i <= 5; i++)
            if (board[Utils.getCoordinate(data.sixth, i)] == data.knight
                    || board[Utils.getCoordinate(data.seventh, i)] == data.pawn) return true;
        return board[Utils.getCoordinate(data.seventh, 2)] == data.knight
                || board[Utils.getCoordinate(data.seventh, 6)] == data.knight;
    }

    private static boolean queenSideCastlingStoppedByKnight(CastlingData data) {
        int knight = data.knight;
        int pawn = data.pawn;
        byte[] board = data.board;

        if (board[Utils.getCoordinate(data.seventh, 0)] == knight
                || board[Utils.getCoordinate(data.seventh, 1)] == pawn
                || board[Utils.getCoordinate(data.seventh, 2)] == pawn
                || board[Utils.getCoordinate(data.seventh, 4)] == knight
                || board[Utils.getCoordinate(data.seventh, 5)] == knight) {
            return true;
        }

        return board[Utils.getCoordinate(data.sixth, 1)] == knight
                || board[Utils.getCoordinate(data.sixth, 2)] == knight;
    }

    private static boolean kingSideCastlingStoppedByKnight(CastlingData data) {
        int knight = data.knight;
        int pawn = data.pawn;
        byte[] board = data.board;

        if (board[Utils.getCoordinate(data.seventh, 7)] == knight
                || board[Utils.getCoordinate(data.seventh, 6)] == pawn
                || board[Utils.getCoordinate(data.seventh, 3)] == knight) {
            return true;
        }

        return board[Utils.getCoordinate(data.sixth, 7)] == knight
                || board[Utils.getCoordinate(data.sixth, 6)] == knight;
    }

    private static boolean queenSidePossible(CastlingData data) {
        if (data.ownRook != data.board[Utils.getCoordinate(data.backRow, 0)]
                || data.board[Utils.getCoordinate(data.backRow, 1)] != 0
                || data.board[Utils.getCoordinate(data.backRow, 2)] != 0
                || data.board[Utils.getCoordinate(data.backRow, 3)] != 0) return false;
        if (queenSideCastlingStoppedByKnight(data)) return false;
        if (queenSideCastlingStoppedVertically(data)) return false;
        return !queenSideCastlingStoppedDiagonally(data);
    }

    private static boolean kingSidePossible(CastlingData data) {
        if (data.ownRook != data.board[Utils.getCoordinate(data.backRow, 7)]
                || data.board[Utils.getCoordinate(data.backRow, 6)] != 0
                || data.board[Utils.getCoordinate(data.backRow, 5)] != 0) return false;
        if (kingSideCastlingStoppedByKnight(data)) return false;
        if (kingSideCastlingStoppedVertically(data)) return false;
        return !kingSideCastlingStoppedDiagonally(data);
    }

    private static boolean backRankThreat(CastlingData data) {

        for (int i = 3; i >= 0; i--) {
            int piece = data.board[Utils.getCoordinate(data.backRow, i)];
            if (data.queen == piece || data.rook == piece) return true;
            if (piece != 0) break;
        }
        for (int i = 5; i <= 7; i++) {
            int piece = data.board[Utils.getCoordinate(data.backRow, i)];
            if (data.queen == piece || data.rook == piece) return true;
            if (piece != 0) break;
        }
        return false;
    }

    private static boolean queenSideCastlingStoppedVertically(CastlingData data) {
        return isThreatenedVertically(data, 2, 3);
    }

    private static boolean kingSideCastlingStoppedVertically(CastlingData data) {
        return isThreatenedVertically(data, 5, 6);
    }

    private static boolean inCheckVertically(CastlingData data) {
        return isThreatenedVertically(data, 4, 4);
    }

    private static boolean isThreatenedVertically(CastlingData data, int startIndex, int endIndex) {
        int backRank, iteration;
        int oppBackRank;

        if (data.isWhite) {
            backRank = 6;
            iteration = -1;
            oppBackRank = -1;
        } else {
            backRank = 1;
            iteration = 1;
            oppBackRank = 8;
        }

        for (int j = startIndex; j <= endIndex; j++) {
            for (int i = backRank; i != oppBackRank; i += iteration) {
                int piece = data.board[Utils.getCoordinate(i, j)];
                if (data.rook == piece || data.queen == piece) return true;
                if (piece != 0) break;
            }
        }

        return false;
    }

    private static boolean queenSideCastlingStoppedDiagonally(CastlingData data) {
        return isThreatenedDiagonally(data, 2, 3);
    }

    private static boolean kingSideCastlingStoppedDiagonally(CastlingData data) {
        return isThreatenedDiagonally(data, 5, 6);
    }

    private static boolean inCheckDiagonally(CastlingData data) {
        byte[] board = data.board;
        if (board[Utils.getCoordinate(data.seventh, 3)] == data.pawn
                || board[Utils.getCoordinate(data.seventh, 5)] == data.pawn) return true;
        return isThreatenedDiagonally(data, 4, 4);
    }

    private static boolean isThreatenedDiagonally(CastlingData data, int startIndex, int endIndex) {

        int horIndex, verStart, verIteration, verIndex;

        if (data.isWhite) {
            verIteration = -1;
            verStart = 6;
        } else {
            verIteration = 1;
            verStart = 1;
        }

        for (int i = startIndex; i <= endIndex; i++) {
            horIndex = i - 1;
            verIndex = verStart;
            while (horIndex <= 7 && horIndex >= 0 && verIndex <= 7 && verIndex >= 0) {
                int piece = data.board[Utils.getCoordinate(verIndex, horIndex)];

                if (data.bishop == piece || data.queen == piece) return true;
                if (piece != 0) break;

                horIndex--;
                verIndex += verIteration;
            }
            horIndex = i + 1;
            verIndex = verStart;
            while (horIndex <= 7 && horIndex >= 0 && verIndex <= 7 && verIndex >= 0) {
                int piece = data.board[Utils.getCoordinate(verIndex, horIndex)];

                if (data.bishop == piece || data.queen == piece) return true;
                if (piece != 0) break;

                horIndex++;
                verIndex += verIteration;
            }
        }
        return false;
    }

    private King() {}
}
