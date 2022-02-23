package Evaluation;

import BoardRepresentation.BoardState;
import DataTypes.Move;
import DataTypes.Moves;
import DataTypes.Pieces;
import Main.OutputUtils;
import MoveGeneration.MoveGenerator;

import java.io.BufferedOutputStream;

public class EvaluationThread extends OutputUtils implements Runnable  {

    BoardState boardState;
    int startingDepth;
    boolean playerTurnWhite;
    boolean debug;
    int depth;
    EvaluationValues values = new EvaluationValues();
    EvaluationCalculations evalCalculator = new EvaluationCalculations();
    MoveGenerator moveGenerator = new MoveGenerator();

    public EvaluationThread(BoardState boardState, int depth, boolean debug, BufferedOutputStream writer) {
        super(writer);
        this.boardState = boardState;
        this.startingDepth = depth;
        this.depth = depth;
        this.debug = debug;
        this.playerTurnWhite = boardState.isWhiteTurn;
    }

    @Override
    public void run() {
        int eval;
        eval = boardState.isWhiteTurn ?
                alphaBetaMax(boardState, Integer.MIN_VALUE, Integer.MAX_VALUE, depth) :
                alphaBetaMin(boardState, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
    }

    int alphaBetaMax(BoardState boardState, int alpha, int beta, int depth) { //white

        Moves moves = moveGenerator.getLegalMoves(boardState);
        if (moves.isEmpty()) { //no moves this turn in checkmate or draw
            if (inCheck(boardState)) {
                return -values.mate + ((startingDepth - depth) / 2);
            }
            return 0;
        }

        if (depth == 0 || Thread.currentThread().isInterrupted()) {
            return evalCalculator.calculateMaterial(boardState);
        }

        Move bestMove = null;

        for (Move move : moves) {
            boardState.makeMove(move);
            int eval = alphaBetaMin(boardState, alpha, beta, depth - 1);
            boardState.unMakeMove();
            if (depth == startingDepth) {
                printInfoUCI(depth, eval, move, true);
            }
            if (eval >= beta) {
                return beta;
            }
            if (eval > alpha) {
                bestMove = move;
                alpha = eval;
            }
        }

        if (depth == startingDepth) {
            if (bestMove == null) bestMove = moves.getFirst();
            println("bestmove " + bestMove.toString());
        }
        return alpha;
    }

    private int alphaBetaMin(BoardState boardState, int alpha, int beta, int depth) { //black

        Moves moves = moveGenerator.getLegalMoves(boardState);
        if (moves.isEmpty()) { //no moves this turn in checkmate or draw
            if (inCheck(boardState)) {
                return values.mate - ((startingDepth - depth) / 2);
            }
            return 0;
        }

        if (depth == 0 || Thread.currentThread().isInterrupted()) {
            return evalCalculator.calculateMaterial(boardState);
        }

        Move bestMove = null;

        for (Move move : moves) {
            boardState.makeMove(move);
            int eval = alphaBetaMax(boardState, alpha, beta, depth - 1);
            boardState.unMakeMove();
            if (depth == startingDepth) {
                printInfoUCI(depth, eval, move, false);
            }
            if (eval <= alpha) {
                return alpha;
            }
            if (eval < beta) {
                bestMove = move;
                beta = eval;
            }
        }

        if (depth == startingDepth) {
            if (bestMove == null) bestMove = moves.getFirst();
            println("bestmove " + bestMove.toString());
        }
        return beta;
    }

    private void printInfoUCI(int depth, int eval, Move move, boolean isWhite) {

        String outString = "info depth " + depth;
        String currMove = " currmove " + move.toString();

        boolean whiteHasMate = eval >= values.mateForWhite;
        boolean isMate = whiteHasMate || eval <= values.mateForBlack;
        int whiteTurn = isWhite ? 1 : -1;

        if (isMate) {
            int offset = whiteHasMate ? whiteTurn + values.mate - eval : whiteTurn - values.mate - eval - (isWhite ? 1 : 0);
            outString += " score mate " + offset;
        } else {
            outString += " score cp " + eval;
        }
        println(outString + currMove);
    }

    private boolean inCheck(BoardState state) {
        boolean old = state.isWhiteTurn;
        boolean isWhite = state.isWhiteTurn;
        state.isWhiteTurn = !state.isWhiteTurn; // flip turn temporarily for checking a check
        Moves opponent = moveGenerator.getLegalMoves(state);
        for (Move move : opponent) {
            int piece = state.board.getCoordinate(move.destination);
            if(piece == 0) continue;
            if ((isWhite && piece == (Pieces.KING | Pieces.WHITE)) || (!isWhite && piece == (Pieces.KING | Pieces.BLACK))) {
                state.isWhiteTurn = old;
                return true;
            }
        }
        state.isWhiteTurn = old;
        return false;
    }
}