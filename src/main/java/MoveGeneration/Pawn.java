package MoveGeneration;

import BoardRepresentation.BoardState;
import DataTypes.Coordinate;
import DataTypes.Move;
import DataTypes.Moves;

import java.util.Arrays;

public class Pawn {

    MoveGenerationUtils moveUtils = new MoveGenerationUtils();

    private Moves pawnPromotions(Move move, boolean isWhite) {
        Moves moves = new Moves();
        char queen, knight, rook, bishop;

        if(isWhite) {
            queen = 'Q'; knight = 'N'; rook = 'R'; bishop = 'B';
        } else {
            queen = 'q'; knight = 'n'; rook = 'r'; bishop = 'b';
        }
        moves.addAll(Arrays.asList(new Move(move, queen), new Move(move, knight), new Move(move, rook), new Move(move, bishop)));
        return moves;
    }

    public Moves pawnMoves(Coordinate origin, BoardState boardState) {

        char [][] board = boardState.board;

        Moves moves = new Moves();
        char orig = moveUtils.getCoordinate(origin, board);
        boolean isWhite = moveUtils.isWhite(orig);
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

        if(!moveUtils.isCoordinateInBounds(new Coordinate(col, nextRow))) return moves; // if nextRow == -1 / 8

        //forward
        if(moveUtils.locationIsEmpty(new Coordinate(col, nextRow), board)) {
            Move nextMove = new Move(origin, new Coordinate(col, nextRow));
            if(promotion) {
                moves.addAll(pawnPromotions(nextMove, isWhite));
            } else {
                moves.add(nextMove);
            }

            if((moveUtils.isWhite(orig) && row == 6 || (!moveUtils.isWhite(orig) && row == 1))
                    && moveUtils.locationIsEmpty(new Coordinate(col, doubleJump), board)) {
                //if at starting square and nothing in front
                Coordinate destination = new Coordinate(col, doubleJump);
                moves.add(new Move(origin, destination));
            }
        }

        //captures
        if(!leftEdge && moveUtils.pawnCapturePossible(new Coordinate(col-1, nextRow), orig, board)) {
            Move nextMove = new Move(origin, new Coordinate(col-1, nextRow));
            if(promotion) {
                moves.addAll(pawnPromotions(nextMove, isWhite));
            } else {
                moves.add(nextMove);
            }
        }

        if(!rightEdge && moveUtils.pawnCapturePossible(new Coordinate(col+1, nextRow), orig, board)) {
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
}
