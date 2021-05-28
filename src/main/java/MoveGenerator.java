public class MoveGenerator {

    static public boolean isWhite(char c) {
        return !Character.isLowerCase(c);
    }

    static public boolean isEmpty(char c) {
        return c == '-';
    }

    static public boolean isOpposingColor(char a, char b) {
        return (isWhite(a) && !isWhite(b)) || (!isWhite(a) && isWhite(b));
    }

    static private boolean pawnCapturePossible(Coordinate coordinate, char orig, char[][] board) {
        return !isEmpty(getCoordinate(coordinate, board)) && opposingColourAndInbounds(coordinate, orig, board);
    }

    static public boolean isCoordinateInBounds(Coordinate coord) {
        return (coord.column >= 0 && coord.row >= 0 && coord.column < 8 && coord.row < 8);
    }

    static public boolean opposingColourAndInbounds(Coordinate coord, char orig, char[][] board) {
        if(isCoordinateInBounds(coord)) {
            char dest = getCoordinate(coord, board);
            return isOpposingColor(orig, dest);

        } else return false;
    }

    static public boolean locationIsEmpty(Coordinate coord, char [][] board) {
        return getCoordinate(coord, board) == '-';
    }

    /**
     * Checks also if the coord is in bounds
     */
    static public boolean isOpposingColourOrEmpty(Coordinate coord, char orig, char[][] board) {
        if(isCoordinateInBounds(coord)) {
            char dest = getCoordinate(coord, board);
            return isOpposingColor(orig, dest) || locationIsEmpty(coord, board);

        } else return false;
    }

    static public char getCoordinate(Coordinate coord, char [][] board) {

        return board[coord.row][coord.column];
    }

    static public Moves bishopMoves(Coordinate origin, BoardState boardState) {

        char [][] board = boardState.board;

        Moves moves = new Moves();
        char orig = getCoordinate(origin, board);
        int row = origin.row;
        int col = origin.column;

        //Bishop moves down and right
        for(int i = 1; row + i <= 7 && col + i <= 7; i++) { //Runs as long as destination is within board limits
            Coordinate destination = new Coordinate(col + i, row + i);
            if(isOpposingColourOrEmpty(destination, orig, board)) {
                moves.add(new Move(origin, destination));

                if(!locationIsEmpty(destination, board)) {
                    break;
                }
            } else {
                break;
            }
        }

        //Bishop moves down and left
        for(int i = 1; row + i <= 7 && col - i >= 0; i++) {
            Coordinate destination = new Coordinate(col - i, row + i);
            if(isOpposingColourOrEmpty(destination, orig, board)) {
                moves.add(new Move(origin, destination));

                if(!locationIsEmpty(destination, board)) {
                    break;
                }
            } else {
                break;
            }
        }

        //Bishop moves up and right
        for(int i = 1; row - i >= 0 && col + i <= 7; i++) {
            Coordinate destination = new Coordinate(col + i, row - i);
            if(isOpposingColourOrEmpty(destination, orig, board)) {
                moves.add(new Move(origin, destination));

                if(!locationIsEmpty(destination, board)) {
                    break;
                }
            } else {
                break;
            }
        }

        //Bishop moves up and left
        for(int i = 1; row - i >= 0 && col - i >= 0; i++) {
            Coordinate destination = new Coordinate(col - i, row - i);
            if(isOpposingColourOrEmpty(destination, orig, board)) {
                moves.add(new Move(origin, destination));

                if(!locationIsEmpty(destination, board)) {
                    break;
                }
            } else {
                break;
            }
        }
        return moves;
    }

    static public Moves rookMoves(Coordinate origin, BoardState boardState) {

        char [][] board = boardState.board;

        Moves moves = new Moves();
        char orig = getCoordinate(origin, board);
        int row = origin.row;
        int col = origin.column;

        //Rook moves to left
        for(int i = 1; col - i >= 0; i++) { //Runs as long as destination is within board limits
            Coordinate destination = new Coordinate(col - i, row);

            if(isOpposingColourOrEmpty(destination, orig, board)) {
                moves.add(new Move(origin, destination));

                if(!locationIsEmpty(destination, board)) { //Checks if move ends in capture to end the loop
                    break;
                }
            } else {
                break;
            }
        }


        //Rook moves upwards
        for(int i = 1; row - i >= 0; i++) {
            Coordinate destination = new Coordinate(col, row - i);

            if(isOpposingColourOrEmpty(destination, orig, board)) {
                moves.add(new Move(origin, destination));

                if(!locationIsEmpty(destination, board)) { //Checks if move ends in capture to end the loop
                    break;
                }
            } else {
                break;
            }
        }

        //Rook moves to right
        for(int i = 1; col + i <= 7; i++) {
            Coordinate destination = new Coordinate(col + i, row);

            if(isOpposingColourOrEmpty(destination, orig, board)) {
                moves.add(new Move(origin, destination));

                if(!locationIsEmpty(destination, board)) { //Checks if move ends in capture to end the loop
                    break;
                }
            } else {
                break;
            }
        }

        //Rook moves downwards
        for(int i = 1; row + i <= 7; i++) {
            Coordinate destination = new Coordinate(col, row + i);

            if(isOpposingColourOrEmpty(destination, orig, board)) {
                moves.add(new Move(origin, destination));

                if(!locationIsEmpty(destination, board)) { //Checks if move ends in capture to end the loop
                    break;
                }
            } else {
                break;
            }
        }

        return moves;
    }

    static public Moves queenMoves(Coordinate origin, BoardState boardState) {

        Moves moves = new Moves();

        moves.addAll(rookMoves(origin, boardState));
        moves.addAll(bishopMoves(origin, boardState));

        return moves;
    }

    static private Moves pawnPromotions(Move move, boolean isWhite) {
        Moves moves = new Moves();
        if(isWhite) {
            moves.add(new Move(move.origin, move.destination, 'Q'));
            moves.add(new Move(move.origin, move.destination, 'N'));
            moves.add(new Move(move.origin, move.destination, 'R'));
            moves.add(new Move(move.origin, move.destination, 'B'));
        } else {
            moves.add(new Move(move.origin, move.destination, 'q'));
            moves.add(new Move(move.origin, move.destination, 'n'));
            moves.add(new Move(move.origin, move.destination, 'r'));
            moves.add(new Move(move.origin, move.destination, 'b'));
        }
        return moves;
    }

    static public Moves pawnMoves(Coordinate origin, BoardState boardState) {

        char [][] board = boardState.board;

        Moves moves = new Moves();
        char orig = getCoordinate(origin, board);
        boolean isWhite = isWhite(orig);
        boolean promotion = false;
        int row = origin.row;
        int col = origin.column;

        int nextRow = row;
        int doubleJump = row;
        int enPassantRow;

        if(isWhite) {
            nextRow -= 1;
            doubleJump -=2;
            enPassantRow = 3;
            if(nextRow == 0) {
                promotion = true;
            }
        } else {
            nextRow += 1;
            doubleJump +=2;
            enPassantRow = 4;
            if(nextRow == 7) {
                promotion = true;
            }
        }

        boolean leftEdge = col == 0;
        boolean rightEdge = col == 7;

        if(!isCoordinateInBounds(new Coordinate(col, nextRow))) return moves; // if nextRow == -1 / 8

        //forward
        if(locationIsEmpty(new Coordinate(col, nextRow), board)) {
            Move nextMove = new Move(origin, new Coordinate(col, nextRow));
            if(promotion) {
                moves.addAll(pawnPromotions(nextMove, isWhite));
            } else {
                moves.add(nextMove);
            }

            if((isWhite(orig) && row == 6 || (!isWhite(orig) && row == 1)) && locationIsEmpty(new Coordinate(col, doubleJump), board)) {
                //if at starting square and nothing in front
                Coordinate destination = new Coordinate(col, doubleJump);
                moves.add(new Move(origin, destination));
            }
        }

        //captures
        if(!leftEdge && pawnCapturePossible(new Coordinate(col-1, nextRow), orig, board)) {
            Move nextMove = new Move(origin, new Coordinate(col-1, nextRow));
            if(promotion) {
                moves.addAll(pawnPromotions(nextMove, isWhite));
            } else {
                moves.add(nextMove);
            }
        }

        if(!rightEdge && pawnCapturePossible(new Coordinate(col+1, nextRow), orig, board)) {
            Move nextMove = new Move(origin, new Coordinate(col+1, nextRow));
            if(promotion) {
                moves.addAll(pawnPromotions(nextMove, isWhite));
            } else {
                moves.add(nextMove);
            }
        }

        if(boardState.enPassant != null && row == enPassantRow) {

            Coordinate destination = boardState.enPassant;
            int distance = origin.column - destination.column;
            if(distance == 1 || distance == -1) {
                moves.add(new Move(origin, destination));
            }
        }
        return moves;
    }

    static public Moves knightMoves(Coordinate origin, BoardState boardState) {

        char [][] board = boardState.board;

        Moves moves = new Moves();

        char orig = getCoordinate(origin, board);
        int row = origin.row;
        int col = origin.column;

        int [] columns = new int []{col-2, col+2};
        int [] rows = new int []{row-2, row+2};
        for (int d_column: columns) {
            Coordinate destination = new Coordinate(d_column, row + 1);
            if(isOpposingColourOrEmpty(destination, orig, board)) moves.add(new Move(origin, destination));
            destination = new Coordinate(d_column, row - 1);
            if(isOpposingColourOrEmpty(destination, orig, board)) moves.add(new Move(origin, destination));
        }

        for (int d_row: rows) {
            Coordinate destination = new Coordinate(col + 1, d_row);
            if(isOpposingColourOrEmpty(destination, orig, board)) moves.add(new Move(origin, destination));
            destination = new Coordinate(col - 1, d_row);
            if(isOpposingColourOrEmpty(destination, orig, board)) moves.add(new Move(origin, destination));
        }

        return moves;
    }

    static public Moves kingMoves(Coordinate origin, BoardState boardState) {

        char [][] board = boardState.board;
        PlayerTurn turn = boardState.turn;
        CastlingRights whiteCastling = boardState.whiteCastling;
        CastlingRights blackCastling = boardState.blackCastling;

        Moves moves = new Moves();

        char orig = getCoordinate(origin, board);
        boolean isWhite = isWhite(orig);
        int row = origin.row;
        int col = origin.column;

        for (int i = row-1; i <= row+1; i++) {
            for (int j = col-1; j <= col+1; j++) {
                if(i != row || j != col) {
                    Coordinate destination = new Coordinate(j, i);
                    if(isOpposingColourOrEmpty(destination, orig, board)) moves.add(new Move(origin, destination));
                }
            }
        }

        //castling
        if((isWhite && whiteCastling != CastlingRights.NONE) || (!isWhite && blackCastling != CastlingRights.NONE)) { //todo add rook and bishop checks
            if(col == 4 && ((isWhite && row == 7) || (!isWhite && row == 0)) && !bothCastlingsStoppedByKnight(isWhite, board) && !inCheckVertically(isWhite, board) && !inCheckDiagonally(isWhite, board))  { //only check if the king is in the original position and hasn't moved
                char [] backRow = board[row];

                boolean qSide = Character.toLowerCase(backRow[0]) == 'r' &&
                        !isOpposingColor(orig, backRow[0]) &&
                        backRow[1] == '-' &&
                        backRow[2] == '-' &&
                        backRow[3] == '-' &&
                        !backRankThreat(isWhite, board) &&
                        !leftCastlingStoppedByKnight(isWhite, board) &&
                        !leftCastlingStoppedVertically(isWhite, board) &&
                        !leftCastlingStoppedDiagonally(isWhite, board);

                boolean kSide = Character.toLowerCase(backRow[7]) == 'r' &&
                        !isOpposingColor(orig, backRow[7]) &&
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
                        case KINGSIDE: {
                            if(kSide) moves.add(new Move(origin, new Coordinate(6, row)));
                            break;
                        }
                        case QUEENSIDE: {
                            if(qSide) moves.add(new Move(origin, new Coordinate(2, row)));
                            break;
                        }
                    }
                }
            }
        }

        return moves;
    }
    static private boolean bothCastlingsStoppedByKnight(boolean isWhite, char[][] board) {

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

    static private boolean leftCastlingStoppedByKnight(boolean isWhite, char[][] board) {

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

    static private boolean rightCastlingStoppedByKnight(boolean isWhite, char[][] board) {
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

    static private boolean backRankThreat(boolean isWhite, char[][] board) {
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
            char piece = board[backrank][i];
            if (piece == oppQueen || piece == oppRook) return true;
            if(piece != '-') break;
        }
        for (int i = 5; i <= 7; i++) {
            char piece = board[backrank][i];
            if (piece == oppQueen || piece == oppRook) return true;
            if(piece != '-') break;
        }
        return false;
    }

    static private boolean leftCastlingStoppedVertically(boolean isWhite, char[][] board) {
        return isThreatenedVertically(isWhite, board, 2, 3);
    }

    static private boolean rightCastlingStoppedVertically(boolean isWhite, char[][] board) {
        return isThreatenedVertically(isWhite, board, 5, 6);
    }

    static private boolean inCheckVertically(boolean isWhite, char [][] board) {
        return isThreatenedVertically(isWhite, board, 4, 4);
    }

    static private boolean isThreatenedVertically(boolean isWhite, char[][] board, int startIndex, int endIndex) {
        int backRank, iteration;
        char oR, oQ;
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
                char piece = board[i][j];
                if(piece == oR || piece == oQ) return true;
                if(piece != '-') break;
            }
        }

        return false;
    }

    static private boolean leftCastlingStoppedDiagonally(boolean isWhite, char[][] board) {
        return isThreatenedDiagonally(isWhite, board, 2, 3);
    }

    static private boolean rightCastlingStoppedDiagonally(boolean isWhite, char[][] board) {
        return isThreatenedDiagonally(isWhite, board, 5, 6);
    }

    static private boolean inCheckDiagonally(boolean isWhite, char[][] board) {
        return isThreatenedDiagonally(isWhite, board, 4, 4);
    }

    static private boolean isThreatenedDiagonally(boolean isWhite, char[][] board, int startIndex, int endIndex) {

        char oB, oQ, k;
        int horIndex, verStart, verIteration, verIndex;

        if(isWhite) {
            oB = 'b';
            oQ = 'q';
            k = 'K';
            verIteration = -1;
            verStart = 6;
        } else {
            oB = 'B';
            oQ = 'Q';
            k = 'k';
            verIteration = 1;
            verStart = 1;
        }

        for(int i = startIndex; i <= endIndex; i++) {
            horIndex = i - 1;
            verIndex = verStart;
            while(horIndex <= 7 && horIndex >= 0 && verIndex <= 7 && verIndex >= 0) {
                char piece = board[verIndex][horIndex];

                if(piece == oB || piece == oQ) return true;
                if(piece != '-') break;

                horIndex--;
                verIndex += verIteration;
            }
            horIndex = i + 1;
            verIndex = verStart;
            while(horIndex <= 7 && horIndex >= 0 && verIndex <= 7 && verIndex >= 0) {
                char piece = board[verIndex][horIndex];

                if(piece == oB || piece == oQ) return true;
                if(piece != '-') break;

                horIndex++;
                verIndex += verIteration;
            }
        }
        return false;
    }

    static public Moves getAllMoves(BoardState boardState) {
        
        Moves moves = new Moves();
        char [][] board = boardState.board;
        PlayerTurn turn = boardState.turn;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                char dest = board[i][j];
                if(dest != '-' && (isWhite(dest) && turn == PlayerTurn.WHITE) || (!isWhite(dest) && turn == PlayerTurn.BLACK)) {
                    moves.addAll(getPieceMoves(new Coordinate(j, i), boardState));
                }
            }
        }
        return moves;
    }

    static public Moves getLegalMoves(BoardState boardState) {
        Moves moves = new Moves();
        PlayerTurn turn = boardState.turn;
        Moves pseudoLegal = getAllMoves(boardState);

        for (Move pLegalMove: pseudoLegal) {
            boardState.makeMove(pLegalMove);
            Moves responses = getAllMoves(boardState);
            boolean legal = true;
            for (Move response: responses) {
                char target = getCoordinate(response.destination, boardState.board);
                if(Character.toLowerCase(target) == 'k') {
                    legal = false;
                    break;
                }
            }
            if(legal) moves.add(pLegalMove);

            boardState.unMakeMove();
        }
        return moves;
    }

    static public Moves getPieceMoves(Coordinate coord, BoardState boardState) {
        char c = Character.toLowerCase(boardState.board[coord.row][coord.column]);

        switch(c) {
            case 'p': return pawnMoves(coord, boardState);
            case 'b': return bishopMoves(coord, boardState);
            case 'n': return knightMoves(coord, boardState);
            case 'r': return rookMoves(coord, boardState);
            case 'q': return queenMoves(coord, boardState);
            case 'k': return kingMoves(coord, boardState);
            default: return null;
        }
    }
}
