import java.util.Arrays;
import java.util.LinkedList;

public class Evaluation {

    int threadCount = 1;
    Game task;
    int material = 0; //equal
    boolean depthHasMoreWork = true;
    ThreadGroup tg = new ThreadGroup("Evaluation_Threads");
    final int ePawn = 100;
    final int eKnight = 180;
    final int eBishop = 220;
    final int eRook = 400;
    final int eQueen = 780;
    final int eKing = 10000;


    public Evaluation(int threadCount) {
        this.threadCount = threadCount;
    }

    public Evaluation(int threadCount, Game task) {
        this.threadCount = threadCount;
        this.task = task;
    }

    public void startEvaluation() {
        for (int i = 0; i < threadCount; i++) {
            new Worker(tg, task, i).start();
        }
    }

    public void updateTask(Game task) {
        material = calculateMaterial(task);
        this.task = task;
    }

    public synchronized LinkedList<int[]> getNextBranch() {

        if (task.moves.size() > 1) {
            return task.moves.pop();
        } else {
            depthHasMoreWork = false;
            return new LinkedList<int[]>();
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

        private Game workerTask;
        private int id;

        public Worker(ThreadGroup tg, Game task, int id) {
            super(tg, "worker-"+id);
            this.workerTask = task;
        }

        public void run() {
            while(depthHasMoreWork) {
                LinkedList<int[]> pieceMoves = getNextBranch();
                if(pieceMoves.size() > 1) {
                    int [] startingSquare = pieceMoves.pop();
                    while(!pieceMoves.isEmpty()) {
                        int [] destinationSquare = pieceMoves.pop();
                        Game possible = new Game();
                        possible.parseFen(workerTask.toFenString());
                        possible.movePiece(startingSquare, destinationSquare);
                        int workerMaterial = calculateMaterial(possible);
                        System.out.println(Arrays.toString(startingSquare) + Arrays.toString(destinationSquare) + workerMaterial + " " + possible.turnNumber);
                    }
                }
            }
        }
    }
}
