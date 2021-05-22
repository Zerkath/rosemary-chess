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
            Coordinate destination = new Coordinate(col, row + 1);

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

    static public Moves pawnMoves(Coordinate origin, BoardState boardState) {

        char [][] board = boardState.board;

        Moves moves = new Moves();
        char orig = getCoordinate(origin, board);
        int row = origin.row;
        int col = origin.column;

        int nextRow = row;
        int doubleJump = row;
        int enPassantRow;

        if(isWhite(orig)) {
            nextRow -= 1;
            doubleJump -=2;
            enPassantRow = 3;
            if(nextRow == 0) {
                //promotion todo
            }
        } else {
            nextRow += 1;
            doubleJump +=2;
            enPassantRow = 4;
            if(nextRow == 7) {
                //promotion todo
            }
        }

        boolean leftEdge = col == 0;
        boolean rightEdge = col == 7;

        //forward
        if(locationIsEmpty(new Coordinate(col, nextRow), board)) {
            moves.add(new Move(origin, new Coordinate(col, nextRow)));

            if((isWhite(orig) && row == 6) || (!isWhite(orig) && row == 1) && locationIsEmpty(new Coordinate(col, doubleJump), board)) {
                //if at starting square and nothing in front
                moves.add(new Move(origin, new Coordinate(col, doubleJump)));
            }
        }

        //captures
        if(!leftEdge && pawnCapturePossible(new Coordinate(col-1, nextRow), orig, board)) {
            moves.add(new Move(origin, new Coordinate(col-1, nextRow)));
        }

        if(!rightEdge && pawnCapturePossible(new Coordinate(col+1, nextRow), orig, board)) {
            moves.add(new Move(origin, new Coordinate(col+1, nextRow)));
        }


        if(row == enPassantRow) { //en passant behavior
            if(!leftEdge && opposingColourAndInbounds(new Coordinate(col-1, row), orig, board) && Character.toLowerCase(board[row][col-1]) == 'p') {
//                    System.out.println("to the left is a pawn"); //TODO check if last move from opponent was 2 squares
            }
            if(!rightEdge && opposingColourAndInbounds(new Coordinate(col+1, row), orig, board) && Character.toLowerCase(board[row][col+1]) == 'p') {
//                    System.out.println("to the right is a pawn");
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
        if(col == 4) { //only check if the king is in the original position and hasn't moved
            char [] backRow = board[row];

            boolean qSide = Character.toLowerCase(backRow[0]) == 'r' &&
                    !isOpposingColor(orig, backRow[0]) &&
                    backRow[1] == '-' &&
                    backRow[2] == '-' &&
                    backRow[3] == '-';

            boolean kSide = Character.toLowerCase(backRow[7]) == 'r' &&
                    !isOpposingColor(orig, backRow[7]) &&
                    backRow[6] == '-' &&
                    backRow[5] == '-';

            CastlingRights current = null;

            if(isWhite(orig) && row == 7 && turn == PlayerTurn.WHITE) {
                current = whiteCastling;
            }

            if(!isWhite(orig) && row == 0 && turn == PlayerTurn.BLACK) {
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
        return moves;
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
    static public MoveSequenceList getAllMovesList(BoardState boardState) {
        Moves moves = getAllMoves(boardState);
        MoveSequenceList moveSequence = new MoveSequenceList();
        for (Move move: moves) {
            Moves tempList = new Moves();
            tempList.add(move);
            moveSequence.add(tempList);
        }
        return moveSequence;
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
