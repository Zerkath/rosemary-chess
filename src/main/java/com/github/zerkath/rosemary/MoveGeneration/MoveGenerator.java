package com.github.zerkath.rosemary.MoveGeneration;

import com.github.zerkath.rosemary.BoardRepresentation.BoardState;
import com.github.zerkath.rosemary.DataTypes.*;

import java.util.Iterator;

public class MoveGenerator {

    private Moves getAllMoves(BoardState boardState) {

        Moves moves = new Moves();
        Board board = boardState.board;
        boolean isWhiteTurn = boardState.isWhiteTurn;

        //Iterates over board to play moves, dest denotes the piece currently being looked at
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                int dest = board.getCoordinate(row, column);
                if(dest == 0) continue;
                boolean destIsWhite = Pieces.isWhite(dest);
                if(isWhiteTurn == destIsWhite) { //white turn and white or black turn and black
                    getAllMoves(Utils.getCoordinate(row, column), boardState, moves);
                }
            }
        }
        return moves;
    }

    public Moves getLegalMoves(BoardState boardState) {
        Moves pseudoLegal = getAllMoves(boardState);

        Iterator<Move> pseudoIterator = pseudoLegal.iterator();
        Move move;
        while(pseudoIterator.hasNext()) {
            move = pseudoIterator.next();
            boardState.makeMove(move);
            if(King.kingInCheck(boardState)) pseudoIterator.remove();
            boardState.unMakeMove();
        }
        return pseudoLegal;
    }

    public void getAllMoves(Coordinate coordinate, BoardState boardState, Moves moves) {
        int piece = boardState.board.getCoordinate(coordinate);
        if(piece == 0) return;
        switch (Pieces.getType(piece)) {
            case Pieces.PAWN -> Pawn.getMoves(coordinate, boardState, moves);
            case Pieces.BISHOP -> Bishop.getMoves(coordinate, boardState, moves);
            case Pieces.KNIGHT -> Knight.getMoves(coordinate, boardState, moves);
            case Pieces.ROOK -> Rook.getMoves(coordinate, boardState, moves);
            case Pieces.QUEEN -> Queen.getMoves(coordinate, boardState, moves);
            case Pieces.KING -> King.getMoves(coordinate, boardState, moves);
        }
    }
}
