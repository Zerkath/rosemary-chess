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


    int threadCount;
    int depthCount = 0;
    int nodesSearched = 0;
    boolean itemsBeingAdded = false;
    Game task;
    String fen;
    int material = 0; //equal
    int depth = 1;
    Utils utils = new Utils();

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
        depthCount = 1;
        task.getPossibleMovesForTurn();
        nodesSearched = task.moves.size();
        System.out.println("info depth " + depthCount + " nodes " + nodesSearched);
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

    public synchronized LinkedList<int[][]> getNextMoveSequence() {
        if (task.moves.size() > 0) {
            return task.moves.pop();
        } else {
            try {
                while(itemsBeingAdded) {
                    wait(750);
                }
            } catch (InterruptedException ignored) {}

            task.moves.addAll(task.nextMoves); //change to opponent responses //todo increase depth every other empty list (maybe print the total moves)
            if(depthCount >= depth) {
                tg.interrupt();
            }
            task.nextMoves.clear(); //clear old items so we can append to list
            depthCount++;
            nodesSearched += task.moves.size();
            System.out.println("info depth " + depthCount + " nodes " + nodesSearched);
            return null;
        }
    }

    public synchronized void addToNextMoves(LinkedList<int[][]> moveSequence) {
        itemsBeingAdded = true;
        task.nextMoves.add(moveSequence);
        itemsBeingAdded = false;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void changeThreadCount(int threads) {
        this.threadCount = threads;
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

    public int[][] endEvaluation() {
        tg.interrupt();
        return new int[][]{{6, 3}, {4, 3}}; //todo just a placeholder get best move (this is just d2 to d4)
    }

    class Worker extends Thread {

        public Worker(ThreadGroup tg, int id) {
            super(tg, "worker-"+id);
        }

        public void run() {
            while(!this.isInterrupted() && depthCount < depth) {
                LinkedList<int[][]> movesToPos = getNextMoveSequence();
                if(movesToPos != null) {
                    LinkedList<int[][]> movesToPosCopy = new LinkedList<>();
                    Game game = new Game();
                    game.parseFen(fen);
                    while(movesToPos.size() > 0) {
                        int[][] move = movesToPos.pop();
                        movesToPosCopy.add(move);
                        game.movePiece(move); //advance to current position
                    }
                    game.getPossibleMovesForTurn(); //get new moves from position
                    LinkedList<LinkedList<int[][]>> newMoves = game.moves;
                    while(newMoves.size() > 0) {
                        int[][] move = newMoves.pop().pop(); //get the next move
                        LinkedList<int[][]> nextMoveSequence = new LinkedList<>(movesToPosCopy); //create new list
                        nextMoveSequence.add(move); //add new move to the end
                        addToNextMoves(nextMoveSequence); //add this to the next moves list
                    }
                }
            }
        }
    }
}
