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

        Piece orig = board.getCoordinate(origin);
        boolean isWhite = orig.isWhite();
        int row = origin.row;
        int col = origin.column;

        for (int row_i = row-1; row_i <= row+1; row_i++) {
            for (int column_i = col-1; column_i <= col+1; column_i++) {
                if(row_i != row || column_i != col) {
                    Coordinate destination = new Coordinate(row_i, column_i);
                    if(board.isOpposingColourOrEmpty(destination, orig)) moves.add(new Move(origin, destination));
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
                Piece [] backRow = board.getRow(row);
                Piece piece = new Piece(isWhite ? 'R' : 'r');

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

    private static boolean queenSidePossible(Piece [] backRow, Piece piece, boolean isWhite, Board board, CastlingKnightData castlingKnightData) {
        if(!piece.equals(backRow[0])) return false;
        if(backRow[1] != null) return false;
        if(backRow[2] != null) return false;
        if(backRow[3] != null) return false;
        if(backRankThreat(isWhite, board)) return false;
        if(leftCastlingStoppedByKnight(castlingKnightData)) return false;
        if(leftCastlingStoppedVertically(isWhite, board)) return false;
        return !leftCastlingStoppedDiagonally(isWhite, board);
    }

    private static boolean kingSidePossible(Piece [] backRow, Piece piece, boolean isWhite, Board board, CastlingKnightData castlingKnightData) {
        if(!piece.equals(backRow[7])) return false;
        if(backRow[6] != null) return false;
        if(backRow[5] != null) return false;
        if(backRankThreat(isWhite, board)) return false;
        if(rightCastlingStoppedByKnight(castlingKnightData)) return false;
        if(rightCastlingStoppedVertically(isWhite, board)) return false;
        return !rightCastlingStoppedDiagonally(isWhite, board);
    }


    private static class CastlingKnightData {
        Piece [] sixth, seventh;
        Piece opponentKnight;
        Piece opponentPawn;
        public CastlingKnightData(boolean isWhite, Board board) {
            if(isWhite) {
                opponentKnight = new Piece('n');
                opponentPawn = new Piece('p');
                sixth = board.getRow(5);
                seventh = board.getRow(6);
            } else {
                opponentKnight = new Piece('N');
                opponentPawn = new Piece('P');
                sixth = board.getRow(2);
                seventh = board.getRow(1);
            }
        }
    }

    private static boolean castlingStoppedByKnight(CastlingKnightData data) {
        return(
                data.opponentKnight.equals(data.seventh[2]) ||
                data.opponentKnight.equals(data.seventh[6]) ||
                data.opponentKnight.equals(data.sixth[3]) ||
                data.opponentKnight.equals(data.sixth[5]) ||
                data.opponentKnight.equals(data.sixth[4]) ||
                data.opponentKnight.equals(data.seventh[4]) ||
                data.opponentPawn.equals(data.seventh[3]) ||
                data.opponentPawn.equals(data.seventh[4]) ||
                data.opponentPawn.equals(data.seventh[5])
        );
    }

    private static boolean leftCastlingStoppedByKnight(CastlingKnightData data) {
        return (
                data.opponentKnight.equals(data.seventh[0])) ||
                data.opponentKnight.equals(data.seventh[1]) ||
                data.opponentKnight.equals(data.sixth[1]) ||
                data.opponentKnight.equals(data.sixth[2]) ||
                data.opponentKnight.equals(data.seventh[5]) ||
                data.opponentPawn.equals(data.seventh[1]) ||
                data.opponentPawn.equals(data.seventh[2]
        );
    }

    private static boolean rightCastlingStoppedByKnight(CastlingKnightData data) {
        return(
                data.opponentKnight.equals(data.seventh[3]) ||
                data.opponentKnight.equals(data.seventh[7]) ||
                data.opponentKnight.equals(data.sixth[6]) ||
                data.opponentKnight.equals(data.sixth[7]) ||
                data.opponentPawn.equals(data.seventh[6])
        );
    }

    private static boolean backRankThreat(boolean isWhite, Board board) {
        int backRank;
        Piece opponentRook, opponentQueen;

        if(isWhite) {
            opponentRook = new Piece('r');
            opponentQueen = new Piece('q');
            backRank = 7;
        } else {
            opponentRook = new Piece('R');
            opponentQueen = new Piece('Q');
            backRank = 0;
        }
        for (int i = 3; i >= 0; i--) {
            Piece piece = board.getCoordinate(new Coordinate(backRank, i));
            if (opponentQueen.equals(piece) || opponentRook.equals(piece)) return true;
            if(piece != null) break;
        }
        for (int i = 5; i <= 7; i++) {
            Piece piece = board.getCoordinate(new Coordinate(backRank, i));
            if (opponentQueen.equals(piece) || opponentRook.equals(piece)) return true;
            if(piece != null) break;
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
        Piece opponentRook, opponentQueen;
        int oppBackRank;

        if(isWhite) {
            opponentRook = new Piece('r');
            opponentQueen = new Piece('q');
            backRank = 6;
            iteration = -1;
            oppBackRank = -1;
        } else {
            opponentRook = new Piece('R');
            opponentQueen = new Piece('Q');
            backRank = 1;
            iteration = 1;
            oppBackRank = 8;
        }

        for (int j = startIndex; j <= endIndex; j++) {
            for(int i = backRank; i != oppBackRank; i += iteration) {
                Piece piece = board.getCoordinate(new Coordinate(i, j));
                if(opponentRook.equals(piece) || opponentQueen.equals(piece)) return true;
                if(piece != null) break;
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
        Piece opponentBishop, opponentQueen;

        if(isWhite) {
            verIteration = -1;
            verStart = 6;
            opponentBishop = new Piece('b');
            opponentQueen = new Piece('q');
        } else {
            verIteration = 1;
            verStart = 1;
            opponentBishop = new Piece('B');
            opponentQueen = new Piece('Q');
        }

        for(int i = startIndex; i <= endIndex; i++) {
            horIndex = i - 1;
            verIndex = verStart;
            while(horIndex <= 7 && horIndex >= 0 && verIndex <= 7 && verIndex >= 0) {
                Piece piece = board.getCoordinate(verIndex, horIndex);

                if(opponentBishop.equals(piece) || opponentQueen.equals(piece)) return true;
                if(piece != null) break;

                horIndex--;
                verIndex += verIteration;
            }
            horIndex = i + 1;
            verIndex = verStart;
            while(horIndex <= 7 && horIndex >= 0 && verIndex <= 7 && verIndex >= 0) {
                Piece piece = board.getCoordinate(verIndex, horIndex);

                if(opponentBishop.equals(piece) || opponentQueen.equals(piece)) return true;
                if(piece != null) break;

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
        Colour colour = white ? Colour.BLACK : Colour.WHITE;
        Piece opponentRook = new Piece(colour, PieceType.ROOK);
        Piece opponentQueen = new Piece(colour, PieceType.QUEEN);
        Piece opponentBishop = new Piece(colour, PieceType.BISHOP);
        Piece opponentKnight = new Piece(colour, PieceType.KNIGHT);
        Piece opponentPawn = new Piece(colour, PieceType.PAWN);

        if (
                pieceHasCheck(rook, boardState, opponentRook, opponentQueen, origin) ||
                pieceHasCheck(bishop, boardState, opponentBishop, opponentQueen, origin)
        ) return false;

        moves = knight.getMoves(origin, boardState);
        for (Move move: moves) {
            Piece piece = board.getCoordinate(move.destination);
            if(piece == null) continue;
            if(opponentKnight.equals(piece)) {
                return false;
            }
        }
        //check for pawns
        if(origin.row <= 7 && origin.row >= 0 && origin.column >= 0 && origin.column <= 7) {
            int col = origin.column;
            int row = origin.row;
            if(white) {
                if(col != 7 && row > 0 && opponentPawn.equals(board.getCoordinate(row - 1, col + 1))) {
                    return false;
                }
                return col == 0 || row <= 0 || !opponentPawn.equals(board.getCoordinate(row - 1, col - 1));
            } else {
                if(col != 7 && row < 7 && opponentPawn.equals(board.getCoordinate(row + 1, col + 1))) {
                    return false;
                }
                return col == 0 || row >= 7 || !opponentPawn.equals(board.getCoordinate(row + 1, col - 1));
            }
        }
        return true;
    }

    private boolean pieceHasCheck(PieceGenerator pieceGenerator, BoardState boardState, Piece checkingPiece, Piece opponentQueen, Coordinate origin) {
        Moves moves = pieceGenerator.getMoves(origin, boardState);
        for (Move move: moves) {
            Piece piece = boardState.board.getCoordinate(move.destination);
            if(piece == null) continue;
            if(checkingPiece.equals(piece) || opponentQueen.equals(piece)) {
                return true;
            }
        }
        return false;
    }
}
