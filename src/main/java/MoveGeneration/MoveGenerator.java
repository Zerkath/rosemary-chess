package MoveGeneration;

import BoardRepresentation.BoardState;
import CommonTools.Utils;
import DataTypes.*;

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

        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                Piece dest = board.getCoordinate(row, column);
                if(dest == null) continue;
                if(turn == dest.getTurnColour()) {
                    moves.addAll(getPieceMoves(new Coordinate(row, column), boardState));
                }
            }
        }
        return moves;
    }

    public Moves getLegalMoves(BoardState boardState) {
        Moves moves = new Moves();
        Moves pseudoLegal = getAllMoves(boardState);

        for (Move pLegalMove: pseudoLegal) {
            boardState.makeMove(pLegalMove);
            if(king.kingNotInCheck(boardState)) moves.add(pLegalMove);
            boardState.unMakeMove();
        }
        return moves;
    }

    public Moves getPieceMoves(Coordinate coord, BoardState boardState) {
        Piece piece = boardState.board.getCoordinate(coord);
        if(piece == null) return null;
        Moves moves = new Moves();
        switch(piece.getType()) {
            case PAWN: moves = pawn.getMoves(coord, boardState); break;
            case BISHOP: moves = bishop.getMoves(coord, boardState); break;
            case KNIGHT: moves = knight.getMoves(coord, boardState); break;
            case ROOK: moves = rook.getMoves(coord, boardState); break;
            case QUEEN: moves = queen.getMoves(coord, boardState); break;
            case KING: moves = king.getMoves(coord, boardState); break;
        }
        return moves;
    }
}
