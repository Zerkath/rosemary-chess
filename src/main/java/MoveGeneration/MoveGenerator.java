package MoveGeneration;

import BoardRepresentation.BoardState;
import DataTypes.Coordinate;
import DataTypes.Move;
import DataTypes.Moves;
import DataTypes.PlayerTurn;

public class MoveGenerator {

    Rook rook = new Rook();
    Bishop bishop = new Bishop();
    Queen queen = new Queen();
    Knight knight = new Knight();
    Pawn pawn = new Pawn();
    King king = new King();

    MoveGenerationUtils moveUtils = new MoveGenerationUtils();

    public Moves getAllMoves(BoardState boardState) {

        Moves moves = new Moves();
        char [][] board = boardState.board;
        PlayerTurn turn = boardState.turn;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                char dest = board[i][j];
                if(dest != '-' && (moveUtils.isWhite(dest) && turn == PlayerTurn.WHITE) || (!moveUtils.isWhite(dest) && turn == PlayerTurn.BLACK)) {
                    moves.addAll(getPieceMoves(new Coordinate(j, i), boardState));
                }
            }
        }
        return moves;
    }

    public Moves getLegalMoves(BoardState boardState) {
        Moves moves = new Moves();
        PlayerTurn turn = boardState.turn;
        Moves pseudoLegal = getAllMoves(boardState);

        for (Move pLegalMove: pseudoLegal) {
            boardState.makeMove(pLegalMove);
            if(king.kingNotInCheck(boardState)) moves.add(pLegalMove);

            boardState.unMakeMove();
        }
        return moves;
    }

    public Moves getPieceMoves(Coordinate coord, BoardState boardState) {
        char c = Character.toLowerCase(boardState.board[coord.row][coord.column]);

        switch(c) {
            case 'p': return pawn.pawnMoves(coord, boardState);
            case 'b': return bishop.getMoves(coord, boardState);
            case 'n': return knight.getMoves(coord, boardState);
            case 'r': return rook.rookMoves(coord, boardState);
            case 'q': return queen.queenMoves(coord, boardState);
            case 'k': return king.getMoves(coord, boardState);
            default: return null;
        }
    }
}
