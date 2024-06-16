package rosemary.eval;

import rosemary.OutputUtils;
import rosemary.board.*;
import rosemary.generation.MoveGenerator;
import rosemary.types.MoveUtil;
import rosemary.types.Moves;
import rosemary.types.Pieces;

public class EvaluationThread implements Runnable {

    BoardState boardState;
    int startingDepth;
    boolean playerTurnWhite;
    boolean debug;
    int depth;

    public EvaluationThread(BoardState boardState, int depth, boolean debug) {
        this.boardState = boardState;
        this.startingDepth = depth;
        this.depth = depth;
        this.debug = debug;
        this.playerTurnWhite = boardState.isWhiteTurn;
    }

    @Override
    public void run() {
        int eval;
        eval =
                alphaBeta(
                        boardState,
                        Integer.MIN_VALUE,
                        Integer.MAX_VALUE,
                        depth,
                        boardState.isWhiteTurn);
    }

    int alphaBeta(BoardState boardState, int alpha, int beta, int depth, boolean isMaxing) {
        Moves moves = MoveGenerator.getLegalMoves(boardState);

        if (moves.isEmpty()) { // no moves this turn in checkmate or draw
            if (inCheck(boardState)) {
                return isMaxing
                        ? -EvaluationValues.mate + ((startingDepth - depth) / 2)
                        : EvaluationValues.mate - ((startingDepth - depth) / 2);
            }
            return 0;
        }

        if (depth == 0 || Thread.currentThread().isInterrupted()) {
            return EvaluationCalculations.calculateMaterial(boardState);
        }

        short bestMove = -1;

        for (short move : moves) {
            int eval =
                    alphaBeta(Mover.makeMove(boardState, move), alpha, beta, depth - 1, !isMaxing);

            if (depth == startingDepth) {
                printInfoUCI(depth, eval, move, isMaxing);
            }

            if (isMaxing) {
                if (eval >= beta) {
                    return beta;
                }
                if (eval > alpha) {
                    bestMove = move;
                    alpha = eval;
                }
            } else {
                if (eval <= alpha) {
                    return alpha;
                }
                if (eval < beta) {
                    bestMove = move;
                    beta = eval;
                }
            }
        }

        if (depth == startingDepth) {
            if (bestMove == -1) bestMove = moves.getShort(0);
            OutputUtils.println("bestmove " + MoveUtil.moveToString(bestMove));
        }

        return isMaxing ? alpha : beta;
    }

    private void printInfoUCI(int depth, int eval, short move, boolean isWhite) {

        String outString = "info depth " + depth;
        String currMove = " currmove " + MoveUtil.moveToString(move);

        boolean whiteHasMate = eval >= EvaluationValues.mateForWhite;
        boolean isMate = whiteHasMate || eval <= EvaluationValues.mateForBlack;
        int whiteTurn = isWhite ? 1 : -1;

        if (isMate) {
            int offset =
                    whiteHasMate
                            ? whiteTurn + EvaluationValues.mate - eval
                            : whiteTurn - EvaluationValues.mate - eval - (isWhite ? 1 : 0);
            outString += " score mate " + offset;
        } else {
            outString += " score cp " + eval;
        }
        OutputUtils.println(outString + currMove);
    }

    private boolean inCheck(BoardState state) {
        boolean old = state.isWhiteTurn;
        boolean isWhite = state.isWhiteTurn;
        state.isWhiteTurn = !state.isWhiteTurn; // flip turn temporarily for checking a check
        Moves opponent = MoveGenerator.getLegalMoves(state);
        for (short move : opponent) {
            int piece = state.board[MoveUtil.getDestination(move)];
            if (piece == 0) continue;
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
