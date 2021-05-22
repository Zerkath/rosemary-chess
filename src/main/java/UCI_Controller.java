public class UCI_Controller {
    public BoardState boardState;
    public boolean uci_mode;
    public int threadCount = 2; //defaults
    public int depth = 5;
    private final String defaultBoard = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public Evaluation eval = new Evaluation(threadCount, 5);
    private final String name = "Rosemary 1";
    private final String authors = "Rosemary devs";

    public UCI_Controller() {
        boardState = new BoardState(Utils.parseFen(defaultBoard));
        System.out.print(name + " by " + authors + "\n");
    }

    public void setToDefault() {
        boardState = new BoardState(Utils.parseFen(defaultBoard));
    }

    public void handleMessage(String message) {
        String [] split = message.split(" ");
        if(message.equals("uci")) {
            setToUCI();
            return;
        }
        if(split[0].equals("position")) {
            if(split[1].equals("fen")) {
                setFen(message.substring(13));
                return;
            }

            if(split[1].equals("startpos")) {
                setFen(defaultBoard);
                for (int i = 3; i < split.length; i++) {
                    boardState.movePiece(Utils.parseCommand(split[i]));
                }
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
                this.threadCount = Integer.parseInt(split[4]);
                return;
            }
            if(split[2].equals("Depth")) {
                this.depth = Integer.parseInt(split[4]);
                return;
            }
        }
        if(split[0].equals("quit")) {
            System.exit(0);
        }
        System.out.println("COMMANDNOTRECOGNIZED");
    }


    public void setFen(String fen) {
        boardState = new BoardState(Utils.parseFen(fen));
    }
    public String getFen() {
        return Utils.toFenString(boardState);
    }

    public void setToUCI() {
        uci_mode = true;
        System.out.print("id name Rosemary 1\n");
        System.out.print("id author Rosemary Devs\n\n");
        System.out.print("option name Threads type spin default 2 min 1 max 250\n");
        System.out.print("option name Depth type spin default 5 min 1 max 99\n");
        System.out.print("uciok\n");
    }

    public void readyResponse() {
        System.out.print("readyok\n");
    }

    public void startEval() {
        startEval(this.depth);
    }

    public void startEval(int depth) {
        eval.assignNewTask(boardState);
        eval.setDepth(depth);
        eval.startEvaluation();
    }

    public void endEval() {
        Move bestMove = eval.endEvaluation();
        System.out.print("bestmove " + Utils.parseCommand(bestMove));
        System.out.print('\n');
    }
}
