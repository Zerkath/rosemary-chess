package MoveGeneration;

import BoardRepresentation.BoardState;
import CommonTools.Utils;
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
        boolean isWhite = Utils.isWhite(orig);
        int row = origin.row;
        int col = origin.column;

        for (int i = row-1; i <= row+1; i++) {
            for (int j = col-1; j <= col+1; j++) {
                if(i != row || j != col) {
                    Coordinate destination = new Coordinate(j, i);
                    if(board.isOpposingColourOrEmpty(destination, orig)) moves.add(new Move(origin, destination));
                }
            }
        }

        //castling
        if((isWhite && whiteCastling != CastlingRights.NONE) || (!isWhite && blackCastling != CastlingRights.NONE)) {
            if(col == 4 && ((isWhite && row == 7) || (!isWhite && row == 0)) && !bothCastlingsStoppedByKnight(isWhite, board) && !inCheckVertically(isWhite, board) && !inCheckDiagonally(isWhite, board))  { //only check if the king is in the original position and hasn't moved
                char [] backRow = board[row];

                boolean qSide = Character.toLowerCase(backRow[0]) == 'r' &&
                        !moveUtils.isOpposingColor(orig, backRow[0]) &&
                        backRow[1] == '-' &&
                        backRow[2] == '-' &&
                        backRow[3] == '-' &&
                        !backRankThreat(isWhite, board) &&
                        !leftCastlingStoppedByKnight(isWhite, board) &&
                        !leftCastlingStoppedVertically(isWhite, board) &&
                        !leftCastlingStoppedDiagonally(isWhite, board);

                boolean kSide = Character.toLowerCase(backRow[7]) == 'r' &&
                        !moveUtils.isOpposingColor(orig, backRow[7]) &&
                        backRow[6] == '-' &&
                        backRow[5] == '-' &&
                        !backRankThreat(isWhite, board) &&
                        !rightCastlingStoppedByKnight(isWhite, board) &&
                        !rightCastlingStoppedVertically(isWhite, board) &&
                        !rightCastlingStoppedDiagonally(isWhite, board);

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
                            if(qSide) moves.add(new Move(origin, new Coordinate(2, row)));
                            if(kSide) moves.add(new Move(origin, new Coordinate(6, row))); //king side
                            break;
                        }
                        case KING: {
                            if(kSide) moves.add(new Move(origin, new Coordinate(6, row)));
                            break;
                        }
                        case QUEEN: {
                            if(qSide) moves.add(new Move(origin, new Coordinate(2, row)));
                            break;
                        }
                    }
                }
            }
        }

        return moves;
    }

    private boolean bothCastlingsStoppedByKnight(boolean isWhite, Piece board) {

        int sixth, seventh;
        char oK;
        char p;
        if(isWhite) {
            oK = 'n';
            p = 'p';
            sixth = 5;
            seventh = 6;
        } else {
            oK = 'N';
            p = 'P';
            sixth = 2;
            seventh = 1;
        }
        return(board[seventh][2] == oK ||
                board[seventh][6] == oK ||
                board[sixth][3] == oK ||
                board[sixth][5] == oK ||
                board[sixth][4] == oK ||
                board[seventh][4] == oK ||
                board[seventh][3] == p ||
                board[seventh][4] == p ||
                board[seventh][5] == p);
    }

    private boolean leftCastlingStoppedByKnight(boolean isWhite, Board board) {

        int sixth, seventh;
        char oK;
        char p;
        if(isWhite) {
            oK = 'n';
            p = 'p';
            sixth = 5;
            seventh = 6;
        } else {
            oK = 'N';
            p = 'P';
            sixth = 2;
            seventh = 1;
        }
        return(board[seventh][0] == oK ||
                board[seventh][1] == oK ||
                board[sixth][1] == oK ||
                board[sixth][2] == oK ||
                board[seventh][5] == oK ||
                board[seventh][1] == p ||
                board[seventh][2] == p);
    }

    private boolean rightCastlingStoppedByKnight(boolean isWhite, Board board) {
        int sixth, seventh;
        char oK;
        char p;
        if(isWhite) {
            oK = 'n';
            p = 'p';
            sixth = 5;
            seventh = 6;
        } else {
            oK = 'N';
            p = 'P';
            sixth = 2;
            seventh = 1;
        }
        return(board[seventh][3] == oK ||
                board[seventh][7] == oK ||
                board[sixth][6] == oK ||
                board[sixth][7] == oK ||
                board[seventh][6] == p);
    }

    private boolean backRankThreat(boolean isWhite, Board board) {
        int backrank;
        char oppRook;
        char oppQueen;
        if(isWhite) {
            backrank = 7;
            oppRook = 'r';
            oppQueen = 'q';
        } else {
            backrank = 0;
            oppRook = 'R';
            oppQueen = 'Q';
        }
        for (int i = 3; i >= 0; i--) {
            Piece piece = board.getCoordinate(new Coordinate(backrank, i));
            if (piece == oppQueen || piece == oppRook) return true;
            if(piece != null) break;
        }
        for (int i = 5; i <= 7; i++) {
            Piece piece = board.getCoordinate(new Coordinate(backrank, i));
            if (piece == oppQueen || piece == oppRook) return true;
            if(piece != null) break;
        }
        return false;
    }

    private boolean leftCastlingStoppedVertically(boolean isWhite, Board board) {
        return isThreatenedVertically(isWhite, board, 2, 3);
    }

    private boolean rightCastlingStoppedVertically(boolean isWhite, Board board) {
        return isThreatenedVertically(isWhite, board, 5, 6);
    }

    private boolean inCheckVertically(boolean isWhite, Board board) {
        return isThreatenedVertically(isWhite, board, 4, 4);
    }

    private boolean isThreatenedVertically(boolean isWhite, Board board, int startIndex, int endIndex) {
        int backRank, iteration;
        int oppBackRank;

        if(isWhite) {
            oR = 'r';
            oQ = 'q';
            backRank = 6;
            iteration = -1;
            oppBackRank = -1;
        } else {
            oR = 'R';
            oQ = 'Q';
            backRank = 1;
            iteration = 1;
            oppBackRank = 8;
        }

        for (int j = startIndex; j <= endIndex; j++) {
            for(int i = backRank; i != oppBackRank; i += iteration) {
                Piece piece = board.getCoordinate(new Coordinate(i, j));
                if(piece == oR || piece == oQ) return true;
                if(piece != null) break;
            }
        }

        return false;
    }

    private boolean leftCastlingStoppedDiagonally(boolean isWhite, Board board) {
        return isThreatenedDiagonally(isWhite, board, 2, 3);
    }

    private boolean rightCastlingStoppedDiagonally(boolean isWhite, Board board) {
        return isThreatenedDiagonally(isWhite, board, 5, 6);
    }

    private boolean inCheckDiagonally(boolean isWhite, Board board) {
        return isThreatenedDiagonally(isWhite, board, 4, 4);
    }

    private boolean isThreatenedDiagonally(boolean isWhite, Board board, int startIndex, int endIndex) {

        int horIndex, verStart, verIteration, verIndex;

        if(isWhite) {
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
                Piece piece = board.getCoordinate(new Coordinate(verIndex, horIndex));

                if(piece == oB || piece == oQ) return true;
                if(piece != null) break;

                horIndex--;
                verIndex += verIteration;
            }
            horIndex = i + 1;
            verIndex = verStart;
            while(horIndex <= 7 && horIndex >= 0 && verIndex <= 7 && verIndex >= 0) {
                Piece piece = board.getCoordinate(new Coordinate(verIndex, horIndex));

                if(piece == oB || piece == oQ) return true;
                if(piece != null) break;

                horIndex++;
                verIndex += verIteration;
            }
        }
        return false;
    }

    private boolean check(Piece piece, boolean white) {
        return piece.getColour() == Colour.BLACK && white || piece.getColour() == Colour.WHITE && !white;
    }

    /**
     * returns true if the king isn't in check
     * @return true if the state is legal
     */
    public boolean kingNotInCheck(BoardState boardState) {
        //its inverse turn
        boolean white = boardState.turn == PlayerTurn.BLACK;
        Board board = boardState.board;
        //lets try the most likely moves to cause check (bishop and rook)
        Moves moves;
        Coordinate origin = white ? board.getWhiteKing() : board.getBlackKing();
        if(origin == null) return true;
        moves = rook.rookMoves(origin, boardState); //todo refactor

        for (Move move: moves) {
            Piece c = board.getCoordinate(move.destination);
            if(c.getType() == PieceType.ROOK || c.getType() == PieceType.QUEEN) {
                if(check(c, white)) {
                    return false; //found a check return false
                }
            }
        }

        moves = bishop.getMoves(origin, boardState);
        for (Move move: moves) {
            Piece c = board.getCoordinate(move.destination);
            if(c.getType() == PieceType.BISHOP || c.getType() == PieceType.QUEEN) {
                if(check(c, white)) {
                    return false; //found a check return false
                }
            }
        }

        moves = knight.getMoves(origin, boardState);
        for (Move move: moves) {
            Piece c = board.getCoordinate(move.destination);
            if(c.getType() == PieceType.KNIGHT) {
                if(check(c, white)) {
                    return false; //found a check return false
                }
            }
        }
        //check for pawns
        if(origin.row <= 7 && origin.row >= 0 && origin.column >= 0 && origin.column <= 7) {
            int col = origin.column;
            int row = origin.row;
            Piece whitePawn = new Piece('P');
            Piece blackPawn = new Piece('p');
            if(white) {
                if(col != 7 && row > 0 && blackPawn.equals(board.getCoordinate(new Coordinate(col + 1, row - 1)))) {
                    return false;
                }
                return col == 0 || row <= 0 || !blackPawn.equals(board.getCoordinate(new Coordinate(col - 1, row - 1)));
            } else {
                if(col != 7 && row < 7 && whitePawn.equals(board.getCoordinate(new Coordinate(col + 1, row + 1)))) {
                    return false;
                }
                return col == 0 || row >= 7 || !blackPawn.equals(board.getCoordinate(new Coordinate(col - 1, row + 1)));
            }
        }
        return true;
    }
}
