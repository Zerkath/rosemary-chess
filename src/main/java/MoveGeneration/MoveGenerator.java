package MoveGeneration;

import BoardRepresentation.BoardState;
import DataTypes.*;

import java.util.Iterator;

public class MoveGenerator {

    Rook rook = new Rook();
    Bishop bishop = new Bishop();
    Queen queen = new Queen();
    Knight knight = new Knight();
    Pawn pawn = new Pawn();
    King king = new King();

    private Moves getAllMoves(BoardState boardState) {

        Moves moves = new Moves();
        Board board = boardState.board;
        PlayerTurn turn = boardState.turn;

        //Iterates over board to play moves, dest denotes the piece currently being looked at
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                int dest = board.getCoordinate(row, column);
                if(dest == 0) continue;
                boolean whitesTurn = turn == PlayerTurn.WHITE;
                boolean destIsWhite = Pieces.isWhite(dest);
                if(whitesTurn == destIsWhite) { //white turn and white or black turn and black
                    getPieceMoves(new Coordinate(row, column), boardState, moves);
                }
            }
        }
        return moves;
    }

    public Moves getLegalMoves(BoardState boardState) {
        Moves pseudoLegal = getAllMoves(boardState);
        Iterator<Move> moveIterator = pseudoLegal.iterator();
        Move move;
        while(moveIterator.hasNext()) {
            move = moveIterator.next();
            boardState.makeMove(move);
            if(!king.kingNotInCheck(boardState)) moveIterator.remove();
            boardState.unMakeMove();
        }
        return pseudoLegal;
    }

    public void getPieceMoves(Coordinate coordinate, BoardState boardState, Moves moves) {
        int piece = boardState.board.getCoordinate(coordinate);
        if(piece == 0) return;
        switch (Pieces.getType(piece)) {
            case Pieces.PAWN: pawn.getMoves(coordinate, boardState, moves); break;
            case Pieces.BISHOP: bishop.getMoves(coordinate, boardState, moves); break;
            case Pieces.KNIGHT: knight.getMoves(coordinate, boardState, moves); break;
            case Pieces.ROOK: rook.getMoves(coordinate, boardState, moves); break;
            case Pieces.QUEEN: queen.getMoves(coordinate, boardState, moves); break;
            case Pieces.KING: king.getMoves(coordinate, boardState, moves); break;
        }
    }
}
