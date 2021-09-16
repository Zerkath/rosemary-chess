package MoveGeneration;

import BoardRepresentation.BoardState;
import DataTypes.*;

public class King implements PieceGenerator {

    Rook rook = new Rook();
    Bishop bishop = new Bishop();
    Knight knight = new Knight();

    public Moves getMoves(Coordinate origin, BoardState boardState) {

        Board board = boardState.board;
        PlayerTurn turn = boardState.turn;
        CastlingRights whiteCastling = board.getWhiteCastling();
        CastlingRights blackCastling = board.getBlackCastling();

        Moves moves = new Moves();

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

        CastlingKnightData castlingKnightData = new CastlingKnightData(isWhite, board);

        //castling
        if((isWhite && whiteCastling != CastlingRights.NONE) || (!isWhite && blackCastling != CastlingRights.NONE)) {
            if(col == 4 && ((isWhite && row == 7) || (!isWhite && row == 0)) &&
                    !castlingStoppedByKnight(castlingKnightData) &&
                    !inCheckVertically(isWhite, board) &&
                    !inCheckDiagonally(isWhite, board)
            )  { //only check if the king is in the original position and hasn't moved
                int [] backRow = board.getRow(row);
                int piece = isWhite ?
                        Pieces.ROOK | Pieces.WHITE :
                        Pieces.ROOK | Pieces.BLACK;

                boolean qSide = queenSidePossible(backRow, piece, isWhite, board, castlingKnightData);

                boolean kSide = kingSidePossible(backRow, piece, isWhite, board, castlingKnightData);

                CastlingRights current = null;

                if(isWhite && turn == PlayerTurn.WHITE) {
                    current = whiteCastling;
                }

                if(!isWhite && turn == PlayerTurn.BLACK) {
                    current = blackCastling;
                }

                if(current != null) {
                    switch (current) {
                        case BOTH: {
                            if(qSide) moves.add(new Move(origin, new Coordinate(row, 2)));
                            if(kSide) moves.add(new Move(origin, new Coordinate(row, 6))); //king side
                            break;
                        }
                        case KING: {
                            if(kSide) moves.add(new Move(origin, new Coordinate(row, 6)));
                            break;
                        }
                        case QUEEN: {
                            if(qSide) moves.add(new Move(origin, new Coordinate(row, 2)));
                            break;
                        }
                    }
                }
            }
        }

        return moves;
    }

    private static boolean queenSidePossible(int [] backRow, int piece, boolean isWhite, Board board, CastlingKnightData castlingKnightData) {
        if(piece != backRow[0]) return false;
        if(backRow[1] != 0) return false;
        if(backRow[2] != 0) return false;
        if(backRow[3] != 0) return false;
        if(backRankThreat(isWhite, board)) return false;
        if(leftCastlingStoppedByKnight(castlingKnightData)) return false;
        if(leftCastlingStoppedVertically(isWhite, board)) return false;
        return !leftCastlingStoppedDiagonally(isWhite, board);
    }

    private static boolean kingSidePossible(int [] backRow, int piece, boolean isWhite, Board board, CastlingKnightData castlingKnightData) {
        if(piece != backRow[7]) return false;
        if(backRow[6] != 0) return false;
        if(backRow[5] != 0) return false;
        if(backRankThreat(isWhite, board)) return false;
        if(rightCastlingStoppedByKnight(castlingKnightData)) return false;
        if(rightCastlingStoppedVertically(isWhite, board)) return false;
        return !rightCastlingStoppedDiagonally(isWhite, board);
    }


    private static class CastlingKnightData {
        int [] sixth, seventh;
        int opponentKnight;
        int opponentPawn;
        public CastlingKnightData(boolean isWhite, Board board) {
            int offset;
            if(isWhite) {
                offset = Pieces.BLACK;
                sixth = board.getRow(5);
                seventh = board.getRow(6);
            } else {
                offset = Pieces.WHITE;
                sixth = board.getRow(2);
                seventh = board.getRow(1);
            }
            opponentPawn = Pieces.PAWN | offset;
            opponentKnight = Pieces.KNIGHT | offset;
        }
    }

    private static boolean castlingStoppedByKnight(CastlingKnightData data) {
        return(
                data.opponentKnight == data.seventh[2]||
                data.opponentKnight == data.seventh[6] ||
                data.opponentKnight == data.sixth[3] ||
                data.opponentKnight == data.sixth[5] ||
                data.opponentKnight == data.sixth[4] ||
                data.opponentKnight == data.seventh[4] ||
                data.opponentPawn == data.seventh[3] ||
                data.opponentPawn == data.seventh[4] ||
                data.opponentPawn == data.seventh[5]
        );
    }

    private static boolean leftCastlingStoppedByKnight(CastlingKnightData data) {
        return (
                data.opponentKnight == data.seventh[0] ||
                data.opponentKnight == data.seventh[1] ||
                data.opponentKnight == data.sixth[1] ||
                data.opponentKnight == data.sixth[2] ||
                data.opponentKnight == data.seventh[5] ||
                data.opponentPawn == data.seventh[1] ||
                data.opponentPawn == data.seventh[2]
        );
    }

    private static boolean rightCastlingStoppedByKnight(CastlingKnightData data) {
        return(
                data.opponentKnight == data.seventh[3] ||
                data.opponentKnight == data.seventh[7] ||
                data.opponentKnight == data.sixth[6] ||
                data.opponentKnight == data.sixth[7] ||
                data.opponentPawn == data.seventh[6]
        );
    }

    private static boolean backRankThreat(boolean isWhite, Board board) {
        int backRank;
        int offset;
        int opponentRook, opponentQueen;

        if(isWhite) {
            offset = Pieces.BLACK;
            backRank = 7;
        } else {
            offset = Pieces.WHITE;
            backRank = 0;
        }
        opponentRook = Pieces.ROOK | offset;
        opponentQueen = Pieces.QUEEN | offset;

        for (int i = 3; i >= 0; i--) {
            int piece = board.getCoordinate(new Coordinate(backRank, i));
            if (opponentQueen == piece || opponentRook == piece) return true;
            if(piece != 0) break;
        }
        for (int i = 5; i <= 7; i++) {
            int piece = board.getCoordinate(new Coordinate(backRank, i));
            if (opponentQueen == piece || opponentRook == piece) return true;
            if(piece != 0) break;
        }
        return false;
    }

    private static boolean leftCastlingStoppedVertically(boolean isWhite, Board board) {
        return isThreatenedVertically(isWhite, board, 2, 3);
    }

    private static boolean rightCastlingStoppedVertically(boolean isWhite, Board board) {
        return isThreatenedVertically(isWhite, board, 5, 6);
    }

    private static boolean inCheckVertically(boolean isWhite, Board board) {
        return isThreatenedVertically(isWhite, board, 4, 4);
    }

    private static boolean isThreatenedVertically(boolean isWhite, Board board, int startIndex, int endIndex) {
        int backRank, iteration;
        int opponentRook, opponentQueen;
        int offset;
        int oppBackRank;

        if(isWhite) {
            offset = Pieces.BLACK;
            backRank = 6;
            iteration = -1;
            oppBackRank = -1;
        } else {
            offset = Pieces.WHITE;
            backRank = 1;
            iteration = 1;
            oppBackRank = 8;
        }
        opponentRook = Pieces.ROOK | offset;
        opponentQueen = Pieces.QUEEN | offset;

        for (int j = startIndex; j <= endIndex; j++) {
            for(int i = backRank; i != oppBackRank; i += iteration) {
                int piece = board.getCoordinate(new Coordinate(i, j));
                if(opponentRook == piece || opponentQueen == piece) return true;
                if(piece != 0) break;
            }
        }

        return false;
    }

    private static boolean leftCastlingStoppedDiagonally(boolean isWhite, Board board) {
        return isThreatenedDiagonally(isWhite, board, 2, 3);
    }

    private static boolean rightCastlingStoppedDiagonally(boolean isWhite, Board board) {
        return isThreatenedDiagonally(isWhite, board, 5, 6);
    }

    private static boolean inCheckDiagonally(boolean isWhite, Board board) {
        return isThreatenedDiagonally(isWhite, board, 4, 4);
    }

    private static boolean isThreatenedDiagonally(boolean isWhite, Board board, int startIndex, int endIndex) {

        int horIndex, verStart, verIteration, verIndex;
        int offset;
        int opponentBishop, opponentQueen;

        if(isWhite) {
            offset = Pieces.BLACK;
            verIteration = -1;
            verStart = 6;
        } else {
            offset = Pieces.WHITE;
            verIteration = 1;
            verStart = 1;
        }

        opponentBishop = Pieces.BISHOP | offset;
        opponentQueen = Pieces.QUEEN | offset;

        for(int i = startIndex; i <= endIndex; i++) {
            horIndex = i - 1;
            verIndex = verStart;
            while(horIndex <= 7 && horIndex >= 0 && verIndex <= 7 && verIndex >= 0) {
                int piece = board.getCoordinate(verIndex, horIndex);

                if(opponentBishop == piece || opponentQueen == piece) return true;
                if(piece != 0) break;

                horIndex--;
                verIndex += verIteration;
            }
            horIndex = i + 1;
            verIndex = verStart;
            while(horIndex <= 7 && horIndex >= 0 && verIndex <= 7 && verIndex >= 0) {
                int piece = board.getCoordinate(verIndex, horIndex);

                if(opponentBishop == piece || opponentQueen == piece) return true;
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
    public boolean kingNotInCheck(BoardState boardState) {
        //its inverse turn
        boolean white = boardState.turn == PlayerTurn.BLACK;
        Board board = boardState.board;
        //let's try the most likely moves to cause check (bishop and rook)
        Moves moves;
        Coordinate origin = white ? board.getWhiteKing() : board.getBlackKing();
        if(origin == null) return true;
        int colour = white ? Pieces.BLACK : Pieces.WHITE;
        int opponentRook = Pieces.ROOK | colour;
        int opponentQueen = Pieces.QUEEN | colour;
        int opponentBishop = Pieces.BISHOP | colour;
        int opponentKnight = Pieces.KNIGHT | colour;
        int opponentPawn = Pieces.PAWN | colour;

        if (
                pieceHasCheck(rook, boardState, opponentRook, opponentQueen, origin) ||
                pieceHasCheck(bishop, boardState, opponentBishop, opponentQueen, origin)
        ) return false;

        moves = knight.getMoves(origin, boardState);
        for (Move move: moves) {
            int piece = board.getCoordinate(move.destination);
            if(piece == 0) continue;
            if(opponentKnight == piece) {
                return false;
            }
        }
        //check for pawns
        if(origin.row <= 7 && origin.row >= 0 && origin.column >= 0 && origin.column <= 7) {
            int col = origin.column;
            int row = origin.row;
            if(white) {
                if(col != 7 && row > 0 && opponentPawn == board.getCoordinate(row - 1, col + 1)) {
                    return false;
                }
                return col == 0 || row <= 0 || opponentPawn != board.getCoordinate(row - 1, col - 1);
            } else {
                if(col != 7 && row < 7 && opponentPawn == board.getCoordinate(row + 1, col + 1)) {
                    return false;
                }
                return col == 0 || row >= 7 || opponentPawn != board.getCoordinate(row + 1, col - 1);
            }
        }
        return true;
    }

    private boolean pieceHasCheck(PieceGenerator pieceGenerator, BoardState boardState, int checkingPiece, int opponentQueen, Coordinate origin) {
        Moves moves = pieceGenerator.getMoves(origin, boardState);
        for (Move move: moves) {
            int piece = boardState.board.getCoordinate(move.destination);
            if(piece == 0) continue;
            if(checkingPiece == piece || opponentQueen == piece) {
                return true;
            }
        }
        return false;
    }
}
