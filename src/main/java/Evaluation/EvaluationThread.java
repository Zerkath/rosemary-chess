package Evaluation;

import BoardRepresentation.BoardState;
import CommonTools.Utils;
import DataTypes.Move;
import DataTypes.Moves;
import DataTypes.PlayerTurn;
import MoveGeneration.MoveGenerator;

public class EvaluationThread implements Runnable {

    BoardState boardState;
    int startingDepth;
    PlayerTurn playerTurn;
    boolean debug;
    int depth;
    EvaluationValues values = new EvaluationValues();
    EvaluationCalculations evalCalculator = new EvaluationCalculations();
    MoveGenerator moveGenerator = new MoveGenerator();
    MoveGenerationUtils moveUtils = new MoveGenerationUtils();

    public EvaluationThread(BoardState boardState, int depth, boolean debug) {
        this.boardState = boardState;
        this.startingDepth = depth;
        this.depth = depth;
        this.debug = debug;
        this.playerTurn = boardState.turn;
    }

    @Override
    public void run() {
        int eval;
        eval = boardState.turn == PlayerTurn.WHITE ? alphaBetaMax(boardState, Integer.MIN_VALUE, Integer.MAX_VALUE, depth) : alphaBetaMin(boardState, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
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
            if (bestMove == null) bestMove = moves.iterator().next();
            System.out.print("bestmove " + Utils.parseCommand(bestMove) + "\n");
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
            if (bestMove == null) bestMove = moves.iterator().next();
            System.out.print("bestmove " + Utils.parseCommand(bestMove) + "\n");
        }
        return beta;
    }

    private void printInfoUCI(int depth, int eval, Move move, boolean isWhite) {

        String outString = "info depth " + depth;
        String currMove = " currmove " + Utils.parseCommand(move);

        boolean whiteHasMate = eval >= values.mateForWhite;
        boolean isMate = whiteHasMate || eval <= values.mateForBlack;
        int whiteTurn = isWhite ? 1 : -1;

        if (isMate) {
            int offset = whiteHasMate ? whiteTurn + values.mate - eval : whiteTurn - values.mate - eval - (isWhite ? 1 : 0);
            outString += " score mate " + offset;
        } else {
            outString += " score cp " + eval;
        }
        System.out.println(outString + currMove);
    }

    private boolean inCheck(BoardState state) {
        PlayerTurn old = state.turn;
        boolean isWhite = state.turn == PlayerTurn.WHITE;
        state.turn = state.turn == PlayerTurn.WHITE ? PlayerTurn.BLACK : PlayerTurn.WHITE;
        Moves opponent = moveGenerator.getLegalMoves(state);
        for (Move move : opponent) {
            char c = moveUtils.getCoordinate(move.destination, state.board);
            if ((isWhite && c == 'K') || (!isWhite && c == 'k')) {
                state.turn = old;
                return true;
            }
        }
        state.turn = old;
        return false;
    }
}