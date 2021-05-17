public class UCI_Controller {
    public Game game = new Game();
    public boolean uci_mode;
    public int threadCount = 1;
    public int maxDepth = 2;
    private final String defaultBoard = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public Evaluation eval = new Evaluation(threadCount, maxDepth);
    private final Utils utils = new Utils();

    public UCI_Controller() {
        game.parseFen(defaultBoard);
    }

    public void setToDefault() {
        game.parseFen(defaultBoard);
    }

    public void handleMessage(String message) {
        String [] split = message.split(" ");
        if(message.equals("uci")) {
            setToUCI();
            return;
        }
        if(split[0].equals("position")) {
            if(split[1].equals("fen")) {
                setFen(split[2]);
                return;
            }
        }
        if(split[0].equals("isready")) {
            readyResponse();
            return;
        }
        if(split[0].equals("go")) {
            startEval();
            return;
        }
        if(split[0].equals("stop")) {
            endEval();
            return;
        }
        if(split[0].equals("setoption")) {
            if(split[2].equals("Threads")) {
                eval.setDepth(Integer.parseInt(split[4]));
                return;
            }
        }
        if(split[0].equals("quit")) {
            System.exit(0);
        }
        System.out.println("COMMANDNOTRECOGNIZED");
    }


    public void setFen(String fen) {
        game.parseFen(fen);
    }

    public void setToUCI() {
        uci_mode = true;
        System.out.println("id name Rosemary 1");
        System.out.println("id author Rosemary Devs");
        System.out.println();
        System.out.println("option name Threads type spin default 1 min 1 max 250");
        System.out.println("uciok");
    }

    public void readyResponse() {
        System.out.println("readyok");
    }

    public void startEval() {
        startEval(maxDepth);
    }

    public void startEval(int depth) {
        maxDepth = depth;
        eval.assignNewTask(game);
        eval.startEvaluation();
    }

    public void endEval() {
        int[][] bestMove = eval.endEvaluation();
        System.out.println("bestmove " + utils.parseCommand(bestMove));
    }
}
