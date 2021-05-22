import java.util.LinkedList;

public class MoveGenerator {

    public boolean isWhite(char c) {
        return !Character.isLowerCase(c);
    }

    public boolean isOpposingColor(char a, char b) {
        return (isWhite(a) && !isWhite(b)) || (!isWhite(a) && isWhite(b));
    }

    public boolean isCoordinateInBounds(Coordinate coord) {
        return (coord.column >= 0 && coord.row >= 0 && coord.column < 8 && coord.row < 8);
    }

    public boolean opposingColourAndInbounds(Coordinate coord, char orig, char[][] board) {
        if(isCoordinateInBounds(coord)) {
            char dest = getCoordinate(coord, board);
            return isOpposingColor(orig, dest);

        } else return false;
    }

    public boolean locationIsEmpty(Coordinate coord, char [][] board) {
        return getCoordinate(coord, board) == '-';
    }

    /**
     * Checks also if the coord is in bounds
     */
    public boolean isOpposingColourOrEmpty(Coordinate coord, char orig, char[][] board) {
        if(isCoordinateInBounds(coord)) {
            char dest = getCoordinate(coord, board);
            return isOpposingColor(orig, dest) || locationIsEmpty(coord, board);

        } else return false;
    }

    public char getCoordinate(Coordinate coord, char [][] board) {
        return board[coord.row][coord.column];
    }

    public Moves bishopMoves(Coordinate origin, BoardState boardState) {

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

    public Moves rookMoves(Coordinate origin, BoardState boardState) {

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

    public Moves queenMoves(Coordinate origin, BoardState boardState) {

        Moves moves = new Moves();

        moves.addAll(rookMoves(origin, boardState));
        moves.addAll(bishopMoves(origin, boardState));

        return moves;
    }

    public Moves pawnMoves(Coordinate origin, BoardState boardState) {

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
        if(!leftEdge && opposingColourAndInbounds(new Coordinate(col-1, nextRow), orig, board)) {
            moves.add(new Move(origin, new Coordinate(col-1, nextRow)));
        }

        if(!rightEdge && opposingColourAndInbounds(new Coordinate(col+1, nextRow), orig, board)) {
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

    public Moves knightMoves(Coordinate origin, BoardState boardState) {

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
}
