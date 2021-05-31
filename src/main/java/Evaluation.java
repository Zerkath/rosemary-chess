public class Evaluation {

    static final int ePawn = 100; //material values
    static final int eKnight = 300;
    static final int eBishop = 310;
    static final int eRook = 500;
    static final int eQueen = 900;

    static final int mate = 2000000050;

    int threadCount;
    int depth;

    public Evaluation(int depth, int threadCount) {
        this.depth = depth;
        this.threadCount = threadCount;
    }

    public Evaluation() {

    }

    static class EvaluationThread implements Runnable {

        BoardState boardState;
        int startingDepth;
        PlayerTurn playerTurn;
        boolean debug;
        int depth;

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

            Moves moves = MoveGenerator.getLegalMoves(boardState);
            if(moves.isEmpty()) { //no moves this turn in checkmate or draw
                if(inCheck(boardState)) {
                    return -mate+((startingDepth - depth)/2);
                }
                return 0;
            }

            if(depth == 0 || Thread.currentThread().isInterrupted()) {
                return Evaluation.calculateEvaluation(boardState);
            }


            Move bestMove = null;

            for(Move move: moves) {
                boardState.makeMove(move);
                int eval = alphaBetaMin(boardState, alpha, beta, depth-1);
                boardState.unMakeMove();
                if(depth == startingDepth) {
                    printInfoUCI(depth, eval, move);
                }
                if(eval >= beta) {
                    return beta;
                }
                if(eval > alpha) {
                    bestMove = move;
                    alpha = eval;
                }
            }

            if(depth == startingDepth) {
                if(bestMove == null) bestMove = moves.get(0);
                System.out.print("bestmove " + Utils.parseCommand(bestMove) + "\n");
            }
            return alpha;
        }

        int alphaBetaMin(BoardState boardState, int alpha, int beta, int depth) { //black

            Moves moves = MoveGenerator.getLegalMoves(boardState);
            if(moves.isEmpty()) { //no moves this turn in checkmate or draw
                if(inCheck(boardState)) {
                    return mate-((startingDepth - depth)/2);
                }
                return 0;
            }

            if(depth == 0 || Thread.currentThread().isInterrupted()) {
                return Evaluation.calculateEvaluation(boardState);
            }

            Move bestMove = null;

            for(Move move: moves) {
                boardState.makeMove(move);
                int eval = alphaBetaMax(boardState, alpha, beta, depth-1);
                boardState.unMakeMove();
                if(depth == startingDepth) {
                    printInfoUCI(depth, eval, move);
                }
                if(eval <= alpha) {
                    return alpha;
                }
                if(eval < beta) {
                    bestMove = move;
                    beta = eval;
                }
            }

            if(depth == startingDepth) {
                if(bestMove == null) bestMove = moves.get(0);
                System.out.print("bestmove " + Utils.parseCommand(bestMove) + "\n");
            }
            return beta;
        }

        private void printInfoUCI(int depth, int eval, Move move) {

            String outString = "info depth " + depth;
            String currMove = " currmove " + Utils.parseCommand(move);

            boolean isMate = eval >= mate-50 || eval <= -mate+50;

            if(isMate) {

                int offset = eval >= mate-50 ?  1+mate-eval : -1-mate-eval;
                outString += " score mate " + offset;
            } else {
                outString += " score cp " + eval;
            }
            System.out.println(outString+currMove);
        }
    }


    static private boolean inCheck(BoardState state) {
        PlayerTurn old = state.turn;
        boolean isWhite = state.turn == PlayerTurn.WHITE;
        state.turn = state.turn == PlayerTurn.WHITE ? PlayerTurn.BLACK : PlayerTurn.WHITE;
        Moves opponent = MoveGenerator.getLegalMoves(state);
        for (Move move: opponent) {
            char c = MoveGenerator.getCoordinate(move.destination, state.board);
            if((isWhite && c == 'K') || (!isWhite && c == 'k')) {
                state.turn = old;
                return true;
            }
        }
        state.turn = old;
        return false;
    }

    //evaluation values

    static public int calculateEvaluation(BoardState state) {
        int negation = state.turn == PlayerTurn.BLACK ? -1 : 1;
        Moves moves = MoveGenerator.getLegalMoves(state);
        int centralControl = calculatePiecesInMiddle(state);
        int materialAdvantage = calculateMaterial(state);
        return materialAdvantage+centralControl+kingSafety(state);
    }

    static private int calculatePiecesInMiddle(BoardState state) {
        char [][] data = state.board;
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

    static private int kingSafety(BoardState state) {
        char [][] data = state.board;
        int value = 0;
        if(state.turnNumber < 20) {
            if(data[0][2] == 'k') { //black king queenSide
                for (int i = 0; i < 3; i++) {
                    if(data[1][i] != 'p') break;
                    if(i == 2) value-=10;
                }
            }
            if(data[7][2] == 'K') { //white king queenSide
                for (int i = 0; i < 3; i++) {
                    if(data[6][i] != 'P') break;
                    if(i == 2) value+=10;
                }
            }
            if(data[0][6] == 'k') { //black king kingSide
                for (int i = 5; i < 8; i++) {
                    if(data[1][i] != 'p') break;
                    if(i == 7) value-=20;
                }
            }
            if(data[7][6] == 'k') { //white king kingSide
                for (int i = 5; i < 8; i++) {
                    if(data[6][i] != 'p') break;
                    if(i == 7) value+=20;
                }
            }
        }
        return value;
    }

    static private int development(BoardState state) {
        char [][] data = state.board;
        int value = 0;




        return value;
    }

    static public int calculateMaterial(BoardState state) { //+ if white, -if black
        int result = 0;
        int [] arr = state.pieces;
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

}
