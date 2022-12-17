package Evaluation;

import BoardRepresentation.BoardState;
import DataTypes.Move;
import DataTypes.Moves;
import DataTypes.Pieces;
import Main.OutputUtils;
import MoveGeneration.MoveGenerator;

import java.io.BufferedOutputStream;

public class EvaluationThread extends OutputUtils implements Runnable {

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
        eval = alphaBeta(boardState, Integer.MIN_VALUE, Integer.MAX_VALUE, depth, boardState.isWhiteTurn);
    }

    private int alphaBeta(BoardState boardState, int alpha, int beta, int depth, boolean isWhite) { // black

        Moves moves = moveGenerator.getLegalMoves(boardState);
        if (moves.isEmpty()) // no moves this turn in checkmate or draw
            return noMoves(boardState, false, depth);

        if (depth == 0 || Thread.currentThread().isInterrupted())
            return evalCalculator.calculateMaterial(boardState);

        Move bestMove = null;

        for (Move move : moves) {
            boardState.makeMove(move);
            int eval = alphaBeta(boardState, alpha, beta, depth - 1, !isWhite);
            boardState.unMakeMove();
            if (depth == startingDepth)
                printInfoUCI(depth, eval, move, false);
            if (isWhite) {
                if (eval >= beta)
                    return beta;
                if (eval > alpha) {
                    bestMove = move;
                    alpha = eval;
                }

            } else {
                if (eval <= alpha)
                    return alpha;
                if (eval < beta) {
                    bestMove = move;
                    beta = eval;
                }
            }
        }

        if (depth == startingDepth) {
            if (bestMove == null)
                bestMove = moves.getFirst();
            println("bestmove " + bestMove.toString());
        }
        return beta;
    }

    private int noMoves(BoardState bs, boolean isWhite, int depth) {
        if (inCheck(bs))
            return 0; // draw
        else
            return isWhite ? -values.mate + ((startingDepth - depth) / 2) : values.mate - ((startingDepth - depth) / 2);
    }

    private void printInfoUCI(int depth, int eval, Move move, boolean isWhite) {

        StringBuilder builder = new StringBuilder();
        builder
                .append("info depth")
                .append(depth);

        boolean whiteHasMate = eval >= values.mateForWhite;
        boolean isMate = whiteHasMate || eval <= values.mateForBlack;
        int whiteTurn = isWhite ? 1 : -1;

        if (isMate) {
            int offset = whiteHasMate ? whiteTurn + values.mate - eval
                    : whiteTurn - values.mate - eval - (isWhite ? 1 : 0);
            builder
                    .append(" score mate ")
                    .append(offset);
        } else
            builder
                    .append(" score cp ")
                    .append(eval);
        builder
                .append(" currmove ")
                .append(move.toString());

        println(builder.toString());
    }

    private boolean inCheck(BoardState state) {
        boolean old = state.isWhiteTurn;
        boolean isWhite = state.isWhiteTurn;
        state.isWhiteTurn = !state.isWhiteTurn; // flip turn temporarily for checking a check
        Moves opponent = moveGenerator.getLegalMoves(state);
        for (Move move : opponent) {
            int piece = state.board.getCoordinate(move.destination);
            if (piece == 0)
                continue;
            if ((isWhite && piece == (Pieces.KING | Pieces.WHITE))
                    || (!isWhite && piece == (Pieces.KING | Pieces.BLACK))) {
                state.isWhiteTurn = old;
                return true;
            }
        }
        state.isWhiteTurn = old;
        return false;
    }
}
