import java.util.Arrays;
import java.util.LinkedList;

public class Evaluation {

    static final int ePawn = 100;
    static final int eKnight = 300;
    static final int eBishop = 310;
    static final int eRook = 500;
    static final int eQueen = 900;

    int threadCount;

    ThreadGroup tg = new ThreadGroup("Evaluation_Threads");

    public Evaluation(int threadCount, int depth) {
        this.threadCount = threadCount;
    }

    static public int calculateEvaluation(BoardState board) {
        int negation = board.turn == PlayerTurn.BLACK ? -1 : 1;
        Moves moves = MoveGenerator.getLegalMoves(board);
        int options = (moves.size() * negation * 2);
        int centralControl = calculatePiecesInMiddle(board);
        int materialAdvantage = calculateMaterial(board);
        return options+materialAdvantage+centralControl;
    }

    static private int calculatePiecesInMiddle(BoardState board) {
        char [][] data = board.board;
        int value = 0;
        for (int i = 2; i < 6; i++) {
            for (int j = 2; j < 6; j++) {
                char piece = data[i][j];
                if(piece != '-') {
                    if(Character.isLowerCase(piece)) {
                        value -= 30;
                    } else {
                        value += 30;
                    }
                }
            }
        }
        return value;
    }

    static public int calculateMaterial(BoardState curr) { //+ if white, -if black
        int result = 0;
        int [] arr = curr.countPieces();
        result += arr[3] * ePawn;
        result -= arr[4] * ePawn;
        result += arr[5] * eBishop;
        result -= arr[6] * eBishop;
        result += arr[7] * eRook;
        result -= arr[8] * eRook;
        result += arr[9] * eKnight;
        result -= arr[10] * eKnight;
        result += arr[11] * eQueen;
        result -= arr[12] * eQueen;
        return result;
    }

    static class EvalutionThread implements Runnable {

        BoardState boardState;
        int startingDepth;
        int depth;

        public EvalutionThread(BoardState boardState, int depth) {
            this.boardState = boardState;
            this.startingDepth = depth;
            this.depth = depth;
        }

        @Override
        public void run() {
            if(this.boardState.turn == PlayerTurn.WHITE) {
                alphaBetaMax(this.boardState, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
            } else {
                alphaBetaMin(this.boardState, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
            }
        }

        int alphaBetaMax(BoardState boardState, int alpha, int beta, int depth) {
            if(depth == 0 || Thread.currentThread().isInterrupted()) return (int) (Evaluation.calculateEvaluation(boardState) * (Math.random() + 0.25)); //random variety
            Moves moves = MoveGenerator.getLegalMoves(boardState);
            Move bestMove = null;
            for(Move move: moves) {
                boardState.makeMove(move);
                int eval = alphaBetaMin(boardState, alpha, beta, depth-1);
                boardState.unMakeMove();
                if(eval >= beta) {
                    return beta;
                }
                if(eval > alpha) {
                    bestMove = move;
                    alpha = eval;
                }
            }
            if(startingDepth == depth && bestMove != null) {
                System.out.print("bestmove " + Utils.parseCommand(bestMove) + "\n");
            }
            return alpha;
        }

        int alphaBetaMin(BoardState boardState, int alpha, int beta, int depth) {
            if(depth == 0 || Thread.currentThread().isInterrupted()) return (int) (Evaluation.calculateEvaluation(boardState) * (Math.random() + 0.25));
            Moves moves = MoveGenerator.getLegalMoves(boardState);
            Move bestMove = null;
            for(Move move: moves) {
                boardState.makeMove(move);
                int eval = alphaBetaMax(boardState, alpha, beta, depth-1);
                boardState.unMakeMove();
                if(eval <= alpha) {
                    return alpha;
                }
                if(eval < beta) {
                    bestMove = move;
                    beta = eval;
                }
            }
            if(startingDepth == depth && bestMove != null) {
                System.out.print("bestmove " + Utils.parseCommand(bestMove) + "\n");
            }
            return beta;
        }
    }
}
