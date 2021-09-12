package Evaluation;

import BoardRepresentation.BoardState;
import DataTypes.Move;
import DataTypes.Moves;
import DataTypes.Piece;
import DataTypes.PlayerTurn;
import MoveGeneration.MoveGenerator;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EvaluationThread implements Runnable {

    BoardState boardState;
    int startingDepth;
    PlayerTurn playerTurn;
    boolean debug;
    int depth;
    EvaluationValues values = new EvaluationValues();
    EvaluationCalculations evalCalculator = new EvaluationCalculations();
    MoveGenerator moveGenerator = new MoveGenerator();
    private final BufferedOutputStream bufferedWriter = new BufferedOutputStream(System.out);

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
            if (bestMove == null) bestMove = moves.iterator().next();
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
        PlayerTurn old = state.turn;
        boolean isWhite = state.turn == PlayerTurn.WHITE;
        state.turn = state.turn == PlayerTurn.WHITE ? PlayerTurn.BLACK : PlayerTurn.WHITE;
        Moves opponent = moveGenerator.getLegalMoves(state);
        for (Move move : opponent) {
            Piece piece = state.board.getCoordinate(move.destination);
            if(piece == null) continue;
            if ((isWhite && piece.equals(new Piece('K'))) || (!isWhite && piece.equals(new Piece('k')))) {
                state.turn = old;
                return true;
            }
        }
        state.turn = old;
        return false;
    }

    private void print(String str) {
        try {
            bufferedWriter.write(str.getBytes(StandardCharsets.UTF_8));
            bufferedWriter.flush();
        } catch (IOException ignored) {}
    }
    private void println(String str) {
        print(str + "\n");
    }
}