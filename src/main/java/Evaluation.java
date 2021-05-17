import java.util.Arrays;
import java.util.LinkedList;

public class Evaluation {

    //eval values TODO optimize
    final int ePawn = 100;
    final int eKnight = 180;
    final int eBishop = 220;
    final int eRook = 400;
    final int eQueen = 780;
    final int eKing = 10000;


    int threadCount = 1;

    Game task;

    String fen;

    int material = 0; //equal

    int depth = 1;

    Utils utils = new Utils();

    boolean depthHasMoreWork = true;
    ThreadGroup tg = new ThreadGroup("Evaluation_Threads");

    public Evaluation(int threadCount, int depth) {
        this.threadCount = threadCount;
        this.depth = depth;
    }

    public Evaluation(int threadCount, Game task) {
        this.threadCount = threadCount;
        this.task = task;
    }

    public void startEvaluation() {
        for (int i = 0; i < threadCount; i++) {
            new Worker(tg, i).start();
        }
    }

    public void assignNewTask(Game task) {
        material = calculateMaterial(task);
        this.fen = task.toFenString();
        this.task = task;
        this.task.getPossibleMovesForTurn();
    }

    public synchronized LinkedList<int[]> getNextBranch() {
        if (!task.moves.isEmpty()) {
            return task.moves.pop();
        } else {
            depthHasMoreWork = false;
            return new LinkedList<>();
        }
    }

    public int calculateMaterial(Game curr) {
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
        result += arr[13] * eKing;
        result -= arr[14] * eKing;
        return result;
    }

    public int[] endEvaluation() {
        tg.interrupt();
        return new int[2];
    }

    class Worker extends Thread {

        public Worker(ThreadGroup tg, int id) {
            super(tg, "worker-"+id);
        }

        public void run() {
            while(depthHasMoreWork) {
                LinkedList<int[]> pieceMoves = getNextBranch();
                if(!pieceMoves.isEmpty()) {

                    int [] selectedSquare = pieceMoves.pop();

                    while(!pieceMoves.isEmpty()) {
                        int [] destination = pieceMoves.pop();

                        Game task = new Game();
                        task.parseFen(fen);
                        boolean kingCapture = task.movePiece(selectedSquare, destination);
                        if (kingCapture) {
                            System.out.println("****" + utils.parseCommand(new int[][]{selectedSquare, destination}));
                        }

                        LinkedList<int[][]> list = new LinkedList<>();

                        int [][] move = new int[2][];
                        move[0] = selectedSquare;
                        move[1] = destination;
                        list.add(move);

                        recursion(depth, list);
                    }
                }
            }
        }

        private void recursion(int depth, LinkedList<int[][]> movesToPosition) {

            Game local = new Game();
            local.parseFen(fen);
            boolean kingCapture = false;
            for (int [][] move: movesToPosition) {
                kingCapture = local.movePiece(move[0], move[1]);
                if (kingCapture && move[1][0] != 0) {
                    kingCapture = false;
                    //System.out.println("****" + parseCommand(new int[][]{move[0], move[1]}));
                }
            }
            local.getPossibleMovesForTurn();

            if(depth <= 0 || local.moves.isEmpty() || kingCapture) { //print variation
                StringBuilder str = new StringBuilder();
                Game print = new Game();
                print.parseFen(fen);
                for (int [][] move: movesToPosition) {
                    str.append(utils.parseCommand(move));
                    str.append(", ");
                    print.movePiece(move[0], move[1]);
                }
                System.out.println(str + " eval " + calculateMaterial(print));
                return;
            }

            int r_depth = depth-1;

            while(!local.moves.isEmpty()) {

                LinkedList<int []> subMoves = local.moves.pop();
                int [] selectedSquare = subMoves.pop();

                while(!subMoves.isEmpty()) {

                    int [] destination = subMoves.pop();
                    int [][] move = new int[2][];

                    LinkedList<int[][]> newMoves = new LinkedList<>(movesToPosition);
                    move[0] = selectedSquare;
                    move[1] = destination;
                    newMoves.add(move);

                    recursion(r_depth, newMoves);
                }
            }
        }
    }

}
