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
                        task.movePiece(selectedSquare, destination);

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



        private char convertToRowChar(int i) {
            switch (i) {
                case 0: return 'a';
                case 1: return 'b';
                case 2: return 'c';
                case 3: return 'd';
                case 4: return 'e';
                case 5: return 'f';
                case 6: return 'g';
                case 7: return 'h';
            }
            return '0';
        }

        private char convertColumnToChar(int i) {
            switch (i) {
                case 0: return '8';
                case 1: return '7';
                case 2: return '6';
                case 3: return '5';
                case 4: return '4';
                case 5: return '3';
                case 6: return '2';
                case 7: return '1';
            }
            return '0';
        }

        private String parseCommand(int [][] move) {
            int [] start = move[0];
            int [] end = move[1];
            return String.valueOf(convertToRowChar(start[1])) +
                    convertColumnToChar(start[0]) +
                    convertToRowChar(end[1]) +
                    convertColumnToChar(end[0]);
        }

        private void recursion(int depth, LinkedList<int[][]> movesToPosition) {



            Game local = new Game();
            local.parseFen(fen);
            for (int [][] move: movesToPosition) {
                local.movePiece(move[0], move[1]);
            }
            local.getPossibleMovesForTurn();

            if(depth <= 0 || local.moves.isEmpty()) { //print variation
                StringBuilder str = new StringBuilder();
                Game print = new Game();
                print.parseFen(fen);
                for (int [][] move: movesToPosition) {
                    str.append(parseCommand(move));
                    str.append(", ");
                    print.movePiece(move[0], move[1]);
                }
                System.out.println(str);
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
