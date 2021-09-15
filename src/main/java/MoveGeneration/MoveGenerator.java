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

        //Iterates over board to play moves, dest denotes the piece currently being looked at
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                int dest = board.getCoordinate(row, column);
                if(dest == 0) continue;
                boolean whitesTurn = turn == PlayerTurn.WHITE;
                if(whitesTurn && Pieces.isWhite(dest)) { //white turn and white or black turn and black
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
        int piece = boardState.board.getCoordinate(coordinate);
        if(piece == 0) return null;
        switch (Pieces.getType(piece)) {
            case Pieces.PAWN: return pawn.getMoves(coordinate, boardState);
            case Pieces.BISHOP: return bishop.getMoves(coordinate, boardState);
            case Pieces.KNIGHT: return knight.getMoves(coordinate, boardState);
            case Pieces.ROOK: return rook.getMoves(coordinate, boardState);
            case Pieces.QUEEN: return queen.getMoves(coordinate, boardState);
            case Pieces.KING: return king.getMoves(coordinate, boardState);
        }
        return null;
    }
}
