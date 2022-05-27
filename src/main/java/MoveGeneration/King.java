package MoveGeneration;

import BoardRepresentation.BoardState;
import DataTypes.*;
import java.util.LinkedList;
public class King {

    public static void getMoves(Coordinate origin, BoardState boardState, Moves moves) {

        Board board = boardState.board;
        boolean isWhiteTurn = boardState.isWhiteTurn;
        CastlingRights whiteCastling = board.getWhiteCastling();
        CastlingRights blackCastling = board.getBlackCastling();

        int originalPiece = board.getCoordinate(origin);
        boolean isWhite = Pieces.isWhite(originalPiece);
        int row = origin.row;
        int col = origin.column;
        
        //generating moves around king //todo update
        for (int row_i = row-1; row_i <= row+1; row_i++) {
            for (int column_i = col-1; column_i <= col+1; column_i++) {
                if(row_i != row || column_i != col) {
                    Coordinate destination = new Coordinate(row_i, column_i);
                    if(board.isOpposingColourOrEmpty(destination, originalPiece)) moves.add(new Move(origin, destination));
                }
            }
        }
        // castling
        if((isWhite && whiteCastling != CastlingRights.NONE) || (!isWhite && blackCastling != CastlingRights.NONE)) {
            CastlingData data = new CastlingData(isWhite, row, board);
            if(col == 4 &&
                    ((isWhite && row == 7) || (!isWhite && row == 0)) &&
                    !castlingStoppedByKnightOrPawn(data) &&
                    !inCheckVertically(data) &&
                    !inCheckDiagonally(data) &&
                    !backRankThreat(data)
            )  { //only check if the king is in the original position and hasn't moved

                int piece = isWhite ?
                        Pieces.ROOK | Pieces.WHITE :
                        Pieces.ROOK | Pieces.BLACK;

                boolean qSide = queenSidePossible(data);

                boolean kSide = kingSidePossible(data);

                CastlingRights current = null;

                if(isWhite && isWhiteTurn) {
                    current = whiteCastling;
                }

                if(!isWhite && !isWhiteTurn) {
                    current = blackCastling;
                }

                if(current != null) {
                    switch (current) {
                        case BOTH -> {
                            if (qSide) moves.add(new Move(origin, new Coordinate(row, 2)));
                            if (kSide) moves.add(new Move(origin, new Coordinate(row, 6)));
                        }
                        case KING -> { if (kSide) moves.add(new Move(origin, new Coordinate(row, 6))); }
                        case QUEEN -> { if (qSide) moves.add(new Move(origin, new Coordinate(row, 2))); }
                    }
                }
            }
        }
    }

    private static class CastlingData {

        boolean isWhite;
        int opponentColour;
        int ownRook;
        int pawn, knight, rook, bishop, queen;
        int row;
        int [] backRow, seventh, sixth;
        Board board;

        public CastlingData(boolean isWhite, int row, Board board) {
            this.isWhite = isWhite;
            this.board = board;
            this.row = row;
            opponentColour = isWhite ? Pieces.BLACK : Pieces.WHITE;
            ownRook = Pieces.ROOK | (isWhite ? Pieces.WHITE: Pieces.BLACK);
            pawn = Pieces.PAWN | opponentColour;
            knight = Pieces.KNIGHT | opponentColour;
            rook = Pieces.ROOK | opponentColour;
            bishop = Pieces.BISHOP | opponentColour;
            queen = Pieces.QUEEN | opponentColour;

            backRow = board.getRow(row);
            seventh = board.getRow(row == 7 ? 6 : 1);
            sixth = board.getRow(row == 7 ? 5 : 2);
        }
    }

    private static boolean castlingStoppedByKnightOrPawn(CastlingData data) {
        for (int i = 3; i <= 5; i++) if(data.sixth[i] == data.knight || data.seventh[i] == data.pawn) return true;
        return data.seventh[2] == data.knight || data.seventh[6] == data.knight;
    }

    private static boolean queenSideCastlingStoppedByKnight(CastlingData data) {
        int knight = data.knight;
        int pawn = data.pawn;

        if(data.seventh[0] == knight || data.seventh[1] == pawn || data.seventh[2] == pawn || data.seventh[4] == knight || data.seventh[5] == knight) {
            return true;
        }

        return data.sixth[1] == knight || data.sixth[2] == knight;
    }

    private static boolean kingSideCastlingStoppedByKnight(CastlingData data) {
        int knight = data.knight;
        int pawn = data.pawn;

        if(data.seventh[7] == knight || data.seventh[6] == pawn || data.seventh[3] == knight) {
            return true;
        }

        return data.sixth[7] == knight || data.sixth[6] == knight;
    }

    private static boolean queenSidePossible(CastlingData data) {
        if(data.ownRook != data.backRow[0] || data.backRow[1] != 0 || data.backRow[2] != 0 || data.backRow[3] != 0) return false;
        if(queenSideCastlingStoppedByKnight(data)) return false;
        if(queenSideCastlingStoppedVertically(data)) return false;
        return !queenSideCastlingStoppedDiagonally(data);
    }

    private static boolean kingSidePossible(CastlingData data) {
        if(data.ownRook != data.backRow[7] || data.backRow[6] != 0 || data.backRow[5] != 0) return false;
        if(kingSideCastlingStoppedByKnight(data)) return false;
        if(kingSideCastlingStoppedVertically(data)) return false;
        return !kingSideCastlingStoppedDiagonally(data);
    }

    private static boolean backRankThreat(CastlingData data) {

        for (int i = 3; i >= 0; i--) {
            int piece = data.backRow[i];
            if (data.queen == piece || data.rook == piece) return true;
            if(piece != 0) break;
        }
        for (int i = 5; i <= 7; i++) {
            int piece = data.backRow[i];
            if (data.queen == piece || data.rook == piece) return true;
            if(piece != 0) break;
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

        if(data.isWhite) {
            backRank = 6;
            iteration = -1;
            oppBackRank = -1;
        } else {
            backRank = 1;
            iteration = 1;
            oppBackRank = 8;
        }

        for (int j = startIndex; j <= endIndex; j++) {
            for(int i = backRank; i != oppBackRank; i += iteration) {
                int piece = data.board.getCoordinate(new Coordinate(i, j));
                if(data.rook == piece || data.queen == piece) return true;
                if(piece != 0) break;
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
        if(data.seventh[3] == data.pawn || data.seventh[5] == data.pawn) return true;
        return isThreatenedDiagonally(data, 4, 4);
    }

    private static boolean isThreatenedDiagonally(CastlingData data, int startIndex, int endIndex) {

        int horIndex, verStart, verIteration, verIndex;

        if(data.isWhite) {
            verIteration = -1;
            verStart = 6;
        } else {
            verIteration = 1;
            verStart = 1;
        }

        for(int i = startIndex; i <= endIndex; i++) {
            horIndex = i - 1;
            verIndex = verStart;
            while(horIndex <= 7 && horIndex >= 0 && verIndex <= 7 && verIndex >= 0) {
                int piece = data.board.getCoordinate(verIndex, horIndex);

                if(data.bishop == piece || data.queen == piece) return true;
                if(piece != 0) break;

                horIndex--;
                verIndex += verIteration;
            }
            horIndex = i + 1;
            verIndex = verStart;
            while(horIndex <= 7 && horIndex >= 0 && verIndex <= 7 && verIndex >= 0) {
                int piece = data.board.getCoordinate(verIndex, horIndex);

                if(data.bishop == piece || data.queen == piece) return true;
                if(piece != 0) break;

                horIndex++;
                verIndex += verIteration;
            }
        }
        return false;
    }

    /**
     * returns true if the king isn't in check
     * @return true if the state is legal
     */
    public static boolean kingInCheck(BoardState boardState) {
        //its inverse turn
        boolean white = !boardState.isWhiteTurn;
        Board board = boardState.board;
        //let's try the most likely moves to cause check (bishop and rook)
        Coordinate origin = white ? board.getWhiteKing() : board.getBlackKing();

        if(distanceWithinBoundary(board.getWhiteKing(), board.getBlackKing(), 1)) return true;

        if(origin == null) return false;
        int opponentColor = white ? Pieces.BLACK : Pieces.WHITE;
        int opponentKnight = Pieces.KNIGHT | opponentColor;
        int opponentPawn = Pieces.PAWN | opponentColor;

        if (pieceHasCheck(boardState, white, Pieces.ROOK, origin) || pieceHasCheck(boardState, white, Pieces.BISHOP, origin)) return true;

        Moves knightMoves = new Moves();
        Knight.getMoves(origin, boardState, knightMoves);
        for (Move move: knightMoves) {
            int piece = board.getCoordinate(move.destination);
            if(piece == 0) continue;
            if(opponentKnight == piece) {
                return true;
            }
        }
        Moves pawnMoves = new Moves();
        Pawn.getMoves(origin, boardState, pawnMoves);
        for (Move move: pawnMoves) {
            if(boardState.board.getCoordinate(move.destination) == opponentPawn) return true;
        }
        return false; // no checks return false
    }


    private static boolean distanceWithinBoundary(Coordinate a, Coordinate b, int boundary) {
        if(a == null || a == null) return false; 
        int absCol = Math.abs(a.column - b.column);
        int absRow = Math.abs(a.row - b.row);

        return absCol <= boundary && absRow <= boundary;
    }

    private static boolean pieceHasCheck(BoardState boardState, boolean colour, int type, Coordinate origin) {
        Moves moves = new Moves();
        switch (type) {
            case Pieces.BISHOP -> Bishop.getMoves(origin, boardState, moves);
            case Pieces.ROOK -> Rook.getMoves(origin, boardState, moves);
        }
        for (Move move: moves) {
            int piece = boardState.board.getCoordinate(move.destination);
            if(piece == 0) continue;
            if(Pieces.isWhite(piece) != colour) {
                int destType = Pieces.getType(piece);
                if(destType == type || destType == Pieces.QUEEN) return true;
            }
        }
        return false;
    }
}
