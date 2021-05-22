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

    MoveSequenceList moves = new MoveSequenceList();
    MoveSequenceList nextMoves = new MoveSequenceList();

    BoardState task;
    String fen;
    int material = 0; //equal
    int depth = 1;

    ThreadGroup tg = new ThreadGroup("Evaluation_Threads");

    public Evaluation(int threadCount, int depth) {
        this.threadCount = threadCount;
        this.depth = depth;
    }

    public Evaluation(int threadCount, int depth, BoardState task) {
        this.threadCount = threadCount;
        this.depth = depth;
        this.task = task;
    }

    public void startEvaluation() {
        tg.interrupt();
        moves.clear();
        nextMoves.clear();
        depthCount = 1;
        moves.addAll(MoveGenerator.getAllMovesList(task));
        nodesSearched = moves.size();
        System.out.println("info depth " + depthCount + " nodes " + nodesSearched);
        for (int i = 0; i < threadCount; i++) {
            new Worker(tg, i).start();
        }
    }

    public void assignNewTask(BoardState task) {
        tg.interrupt();
        moves.clear();
        nextMoves.clear();
        material = calculateMaterial(task);
        this.fen = Utils.toFenString(task);
        this.task = task;
        moves.addAll(MoveGenerator.getAllMovesList(task));
    }

    public synchronized Moves getNextMoveSequence() {
        if (moves.size() > 0) {
            return moves.pop();
        } else {
            try {
                while(itemsBeingAdded) {
                    wait(750);
                }
            } catch (InterruptedException ignored) {}

            moves.addAll(nextMoves); //change to opponent responses //todo increase depth every other empty list (maybe print the total moves)
            if(depthCount >= depth) {
                tg.interrupt();
            }
            nextMoves.clear(); //clear old items so we can append to list
            depthCount++;
            nodesSearched += moves.size();
            System.out.println("info depth " + depthCount + " nodes " + nodesSearched);
            return null;
        }
    }

    public synchronized void addToNextMoves(Moves moves) {
        itemsBeingAdded = true;
        nextMoves.add(moves);
        itemsBeingAdded = false;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void changeThreadCount(int threads) {
        this.threadCount = threads;
    }

    public int calculateMaterial(BoardState curr) {
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

    public Move endEvaluation() {
        tg.interrupt();
        return new Move(new Coordinate(3, 6), new Coordinate(3, 4)); //todo just a placeholder get best move (this is just d2 to d4)
    }

    class Worker extends Thread {

        public Worker(ThreadGroup tg, int id) {
            super(tg, "worker-"+id);
        }

        public void run() {
            while(!this.isInterrupted() && depthCount < depth) {
                Moves moves = getNextMoveSequence();
                BoardState game = new BoardState(Utils.parseFen(fen));
                if(moves != null) {
                    Moves copy = new Moves();

                    while(moves.size() > 0) {
                        Move move = moves.pop();
                        copy.add(move);
                        game.movePiece(move); //advance to current position
                    }
                    Moves newMoves = MoveGenerator.getAllMoves(game);
                    while(newMoves.size() > 0) {
                        Move move = newMoves.pop(); //get the next move
                        Moves nextMoveSequence = new Moves(); //create new list
                        nextMoveSequence.addAll(copy);
                        nextMoveSequence.add(move); //add new move to the end
                        addToNextMoves(nextMoveSequence); //add this to the next moves list
                    }
                }
            }
        }
    }
}
