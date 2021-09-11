package MoveGeneration;

import BoardRepresentation.BoardState;
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

    public Moves getPieceMoves(Coordinate coordinate, BoardState boardState) {
        Piece piece = boardState.board.getCoordinate(coordinate);
        if(piece == null) return null;
        switch (piece.getType()) {
            case PAWN: return pawn.getMoves(coordinate, boardState);
            case BISHOP: return bishop.getMoves(coordinate, boardState);
            case KNIGHT: return knight.getMoves(coordinate, boardState);
            case ROOK: return rook.getMoves(coordinate, boardState);
            case QUEEN: return queen.getMoves(coordinate, boardState);
            case KING: return king.getMoves(coordinate, boardState);
        }
        return null;
    }
}
