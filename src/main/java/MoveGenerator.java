import java.util.LinkedList;

public class MoveGenerator {

    public boolean isWhite(char c) {
        return !Character.isLowerCase(c);
    }

    public boolean opposingColours(char a, char b) {
        return ((isWhite(a) && !isWhite(b)) || !isWhite(a) && isWhite(b));
    }

    public boolean isCoordinateInBounds(Coordinate coord) {
        return (coord.column >= 0 && coord.row >= 0 && coord.column < 8 && coord.row < 8);
    }

    public boolean opposingColourAndInbounds(Coordinate coord, char orig, char[][] board) {
        if(isCoordinateInBounds(coord)) {
            char dest = getCoord(coord, board);
            return opposingColours(orig, dest);

        } else return false;
    }

    public boolean locationIsEmpty(Coordinate coord, char [][] board) {
        return getCoord(coord, board) == '-';
    }

    public boolean isOpposingColourOrEmpty(Coordinate coord, char orig, char[][] board) {
        if(isCoordinateInBounds(coord)) {
            char dest = getCoord(coord, board);
            return opposingColours(orig, dest) || locationIsEmpty(coord, board);

        } else return false;
    }

    public char getCoord(Coordinate coord, char [][] board) {
        return board[coord.row][coord.column];
    }

    public Moves bishopMoves(Coordinate origin, BoardState boardState) {

        char [][] board = boardState.board;

        Moves moves = new Moves();
        char orig = getCoord(origin, board);
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
        char orig = getCoord(origin, board);
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

    public LinkedList<Move> queenMoves(Coordinate origin, BoardState boardState) {

        LinkedList<Move> moves = new LinkedList<>();

        moves.addAll(rookMoves(origin, boardState));
        moves.addAll(bishopMoves(origin, boardState));

        return moves;
    }
}
