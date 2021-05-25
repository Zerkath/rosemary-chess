public class TestProgram {
    public static void main(String[] args) {
        UCI_Controller uci = new UCI_Controller();
        String pgn = "position startpos moves d2d4 f7f6 g2g4 e7e6 d4d5 e6d5 d1d5 f8b4 c2c3 b4c3 b2c3 d8e7 g1f3 d7d6 f1h3 b7b5 e1g1 c8e6 d5a8 h7h5 g4g5 e6g4 h3g4 h5g4 f3d4 f6f5 a8b8 e8f7 c1f4 h8h4 b1d2 e7e5 f4e5 d6e5 d4f5 h4h2 g1h2 g4g3 h2g3 a7a5 d2e4 c7c5 b8c7 f7e6 c7d6 e6f5 f2f3 g8f6 g5f6 c5c4";
        uci.handleMessage(pgn);
        BoardState boardState = Utils.parseFen(uci.getFen());
        Utils.printBoard(boardState);
        uci.handleMessage("go");
    }
}
