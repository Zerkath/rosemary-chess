package rosemary.generation;

import it.unimi.dsi.fastutil.shorts.ShortIterator;
import rosemary.board.*;
import rosemary.types.*;
import rosemary.types.MoveUtil;

public class King {

    static Moves[] unfilteredMoves = new Moves[64];

    static {
        for (short origin = 0; origin < 64; origin++) {
            int row = MoveUtil.getRow(origin);
            int col = MoveUtil.getColumn(origin);
            // generating moves around king //todo update
            Moves moves = new Moves();
            for (int row_i = row - 1; row_i <= row + 1; row_i++) {
                for (int column_i = col - 1; column_i <= col + 1; column_i++) {
                    if (row_i != row || column_i != col) {
                        Utils.addToCollection(row_i, column_i, row, col, moves);
                    }
                }
            }
            unfilteredMoves[origin] = moves;
        }
    }

    public static void getMoves(short origin, BoardState boardState, Moves moves) {
        byte[] board = boardState.getBoard();
        boolean isWhiteTurn = boardState.isWhiteTurn();
        byte whiteCastling = boardState.getWhiteCastling();
        byte blackCastling = boardState.getBlackCastling();

        int originalPiece = board[origin];
        boolean isWhite = Pieces.isWhite(originalPiece);
        int row = MoveUtil.getRow(origin);
        int col = MoveUtil.getColumn(origin);

        ShortIterator pseudoIterator = unfilteredMoves[origin].iterator();
        // generating moves around king //todo update
        while (pseudoIterator.hasNext()) {
            short move = pseudoIterator.nextShort();
            if (BoardUtils.isOpposingColourOrEmpty(MoveUtil.getDestination(move), originalPiece, board))
                moves.add(move);
        }

        // castling
        if ((isWhite && whiteCastling != CastlingRights.NONE) || (!isWhite && blackCastling != CastlingRights.NONE)) {
            CastlingData data = new CastlingData(isWhite, row, board);
            if (col == 4
                    && ((isWhite && row == 7) || (!isWhite && row == 0))
                    && !castlingStoppedByKnightOrPawn(data)
                    && !inCheckVertically(data)
                    && !inCheckDiagonally(data)
                    && !backRankThreat(data)) { // only check if the king is in the original position and
                // hasn't moved

                boolean qSide = queenSidePossible(data);

                boolean kSide = kingSidePossible(data);

                byte current = 0;

                if (isWhite && isWhiteTurn) {
                    current = whiteCastling;
                }

                if (!isWhite && !isWhiteTurn) {
                    current = blackCastling;
                }

                if (current != 0) {
                    switch (current) {
                        case CastlingRights.BOTH -> {
                            if (qSide) moves.add(MoveUtil.getMove(origin, Utils.getCoordinate(row, 2)));
                            if (kSide) moves.add(MoveUtil.getMove(origin, Utils.getCoordinate(row, 6)));
                        }
                        case CastlingRights.KING -> {
                            if (kSide) moves.add(MoveUtil.getMove(origin, Utils.getCoordinate(row, 6)));
                        }
                        case CastlingRights.QUEEN -> {
                            if (qSide) moves.add(MoveUtil.getMove(origin, Utils.getCoordinate(row, 2)));
                        }
                        default -> {}
                    }
                }
            }
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

        public CastlingData(boolean isWhite, int row, byte[] board) {
            this.isWhite = isWhite;
            this.board = board;
            opponentColour = isWhite ? Pieces.BLACK : Pieces.WHITE;
            ownRook = Pieces.ROOK | (isWhite ? Pieces.WHITE : Pieces.BLACK);
            pawn = Pieces.PAWN | opponentColour;
            knight = Pieces.KNIGHT | opponentColour;
            rook = Pieces.ROOK | opponentColour;
            bishop = Pieces.BISHOP | opponentColour;
            queen = Pieces.QUEEN | opponentColour;

            backRow = row;
            seventh = row == 7 ? 6 : 1;
            sixth = row == 7 ? 5 : 2;
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

    /**
     * returns true if the king isn't in check
     *
     * @return true if the state is legal
     */
    public static boolean kingInCheck(BoardState boardState) {
        // its inverse turn
        boolean white = !boardState.isWhiteTurn();
        byte[] board = boardState.getBoard();

        // let's try the most likely moves to cause check (bishop and rook)
        short origin = white ? boardState.getWhiteKing() : boardState.getBlackKing();
        if (origin == -1) return false;
        if (pieceHasCheck(boardState, white, Pieces.ROOK, origin)
                || pieceHasCheck(boardState, white, Pieces.BISHOP, origin)) return true;

        if (distanceWithinBoundary(boardState.getWhiteKing(), boardState.getBlackKing(), 1)) return true;

        int opponentColor = white ? Pieces.BLACK : Pieces.WHITE;
        int opponentKnight = Pieces.KNIGHT | opponentColor;
        int opponentPawn = Pieces.PAWN | opponentColor;

        Moves knightMoves = new Moves();
        Knight.getMoves(origin, boardState, knightMoves);
        ShortIterator knightIter = knightMoves.iterator();
        while (knightIter.hasNext()) {
            short move = knightIter.nextShort();

            int piece = board[MoveUtil.getDestination(move)];
            if (piece == 0) continue;
            if (opponentKnight == piece) {
                return true;
            }
        }
        Moves pawnMoves = new Moves();
        Pawn.getMoves(origin, boardState, pawnMoves);
        ShortIterator pawnIter = pawnMoves.iterator();
        while (pawnIter.hasNext()) {
            short move = pawnIter.nextShort();
            if (boardState.getBoard()[MoveUtil.getDestination(move)] == opponentPawn) return true;
        }
        return false; // no checks return false
    }

    private static boolean distanceWithinBoundary(short a, short b, int boundary) {
        int absCol = Math.abs(MoveUtil.getColumn(a) - MoveUtil.getColumn(b));
        int absRow = Math.abs(MoveUtil.getRow(a) - MoveUtil.getRow(b));

        return absCol <= boundary && absRow <= boundary;
    }

    private static boolean pieceHasCheck(BoardState boardState, boolean colour, int type, short origin) {

        // generate moves from king position to outside
        Moves pieceHasCheckList = new Moves();

        switch (type) {
            case Pieces.BISHOP -> Bishop.getMoves(origin, boardState, pieceHasCheckList);
            case Pieces.ROOK -> Rook.getMoves(origin, boardState, pieceHasCheckList);
        }
        ShortIterator iter = pieceHasCheckList.iterator();
        while (iter.hasNext()) {
            short move = iter.nextShort();

            int piece = boardState.getBoard()[MoveUtil.getDestination(move)];

            if (piece == 0) continue;
            if (Pieces.isWhite(piece) != colour) {
                int destType = Pieces.getType(piece);
                if (destType == type || destType == Pieces.QUEEN) {
                    return true;
                }
            }
        }
        return false;
    }
}
