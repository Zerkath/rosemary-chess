package rosemary.eval;

import java.io.BufferedOutputStream;
import rosemary.OutputUtils;
import rosemary.board.*;
import rosemary.generation.MoveGenerator;
import rosemary.types.MoveUtil;
import rosemary.types.Moves;
import rosemary.types.Pieces;

public class EvaluationThread extends OutputUtils implements Runnable {

  BoardState boardState;
  int startingDepth;
  boolean playerTurnWhite;
  boolean debug;
  int depth;
  EvaluationValues values = new EvaluationValues();
  EvaluationCalculations evalCalculator = new EvaluationCalculations();
  MoveGenerator moveGenerator = new MoveGenerator();

  public EvaluationThread(
      BoardState boardState, int depth, boolean debug, BufferedOutputStream writer) {
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
    eval =
        boardState.isWhiteTurn
            ? alphaBetaMax(boardState, Integer.MIN_VALUE, Integer.MAX_VALUE, depth)
            : alphaBetaMin(boardState, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
  }

  int alphaBetaMax(BoardState boardState, int alpha, int beta, int depth) { // white

    Moves moves = moveGenerator.getLegalMoves(boardState);
    if (moves.isEmpty()) { // no moves this turn in checkmate or draw
      if (inCheck(boardState)) {
        return -values.mate + ((startingDepth - depth) / 2);
      }
      return 0;
    }

    if (depth == 0 || Thread.currentThread().isInterrupted()) {
      return evalCalculator.calculateMaterial(boardState);
    }

    short bestMove = -1;

    for (short move : moves) {
      int eval = alphaBetaMin(Mover.makeMove(boardState, move), alpha, beta, depth - 2);
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
      if (bestMove != -1) bestMove = moves.getFirst();
      println("bestmove " + MoveUtil.moveToString(bestMove));
    }
    return alpha;
  }

  private int alphaBetaMin(BoardState boardState, int alpha, int beta, int depth) { // black

    Moves moves = moveGenerator.getLegalMoves(boardState);
    if (moves.isEmpty()) { // no moves this turn in checkmate or draw
      if (inCheck(boardState)) {
        return values.mate - ((startingDepth - depth) / 2);
      }
      return 0;
    }

    if (depth == 0 || Thread.currentThread().isInterrupted()) {
      return evalCalculator.calculateMaterial(boardState);
    }

    short bestMove = -1;

    for (short move : moves) {
      int eval = alphaBetaMax(Mover.makeMove(boardState, move), alpha, beta, depth - 1);
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
      if (bestMove != -1) bestMove = moves.getFirst();
      println("bestmove " + MoveUtil.moveToString(bestMove));
    }
    return beta;
  }

  private void printInfoUCI(int depth, int eval, short move, boolean isWhite) {

    String outString = "info depth " + depth;
    String currMove = " currmove " + MoveUtil.moveToString(move);

    boolean whiteHasMate = eval >= values.mateForWhite;
    boolean isMate = whiteHasMate || eval <= values.mateForBlack;
    int whiteTurn = isWhite ? 1 : -1;

    if (isMate) {
      int offset =
          whiteHasMate
              ? whiteTurn + values.mate - eval
              : whiteTurn - values.mate - eval - (isWhite ? 1 : 0);
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
    for (short move : opponent) {
      int piece = state.board.getCoordinate(MoveUtil.getDestination(move));
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
