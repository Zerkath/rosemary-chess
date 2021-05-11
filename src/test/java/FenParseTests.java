import org.junit.jupiter.api.*;
public class FenParseTests {
    Game game = new Game();

    @Test
    void startingBoard() {
        game.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        game.printBoard();
        Assertions.assertEquals(Game.CastlingRights.BOTH, game.blackCastling);
        Assertions.assertEquals(Game.CastlingRights.BOTH, game.whiteCastling);
    }

    @Test
    void noWhiteQueenSide() {
        game.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w Kkq - 0 1");
        game.printBoard();
        Assertions.assertEquals(Game.CastlingRights.BOTH, game.blackCastling);
        Assertions.assertEquals(Game.CastlingRights.KINGSIDE, game.whiteCastling);
    }

    @Test
    void noBlackQueenSide() {
        game.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQk - 0 1");
        game.printBoard();
        Assertions.assertEquals(Game.CastlingRights.KINGSIDE, game.blackCastling);
        Assertions.assertEquals(Game.CastlingRights.BOTH, game.whiteCastling);
    }

    @Test
    void noCastlingRights() {
        game.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1");
        game.printBoard();
        Assertions.assertEquals(Game.CastlingRights.NONE, game.blackCastling);
        Assertions.assertEquals(Game.CastlingRights.NONE, game.whiteCastling);
    }

    @Test
    void correctAmountOfPieces() {

        game.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        int [] countedPieces = countPieces();
        game.printBoard();
        Assertions.assertEquals(16, countedPieces[0]);
        Assertions.assertEquals(16, countedPieces[1]);
        Assertions.assertEquals(32, countedPieces[2]);
    }
    @Test
    void noBlackPieces() {

        game.parseFen("8/8/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        int [] countedPieces = countPieces();
        game.printBoard();
        Assertions.assertEquals(16, countedPieces[0]);
        Assertions.assertEquals(0, countedPieces[1]);
    }

    @Test
    void noWhitePieces() {

        game.parseFen("rnbqkbnr/pppppppp/8/8/8/8/8/8 w KQkq - 0 1");
        int [] countedPieces = countPieces();
        game.printBoard();
        Assertions.assertEquals(0, countedPieces[0]);
        Assertions.assertEquals(16, countedPieces[1]);
    }

    @Test
    void weirdBoardPosition() {
        //https://lichess.org/editor/q3k3/2P3P1/6q1/3P1P2/1P1q4/3B1N2/P2B2N1/1R2K2R_w_K_-_0_1
        game.parseFen("q3k3/2P3P1/6q1/3P1P2/1P1q4/3B1N2/P2B2N1/1R2K2R w K - 0 1");
        int [] countedPieces = countPieces();
        game.printBoard();
        Assertions.assertEquals(3, countedPieces[12]); //black queens
        Assertions.assertEquals(6, countedPieces[3]); //white pawns
        Assertions.assertEquals(2, countedPieces[9]); //white knights
        Assertions.assertEquals(2, countedPieces[5]); //white bishops

        Assertions.assertEquals(1, countedPieces[13]); //kings
        Assertions.assertEquals(1, countedPieces[14]);

    }

    /**
     *
     * @return results array        <br />
     * w = white<br /> b = black    <br />
     * 0 w piece count      <br />
     * 1 b piece count      <br />
     * 2 total pieces       <br />
     * 3 w pawns            <br />
     * 4 b pawns            <br />
     * 5 w bishops          <br />
     * 6 b bishops          <br />
     * 7 w rooks            <br />
     * 8 b rooks            <br />
     * 9 w knights          <br />
     * 10 b knights         <br />
     * 11 w queens          <br />
     * 12 b queens          <br />
     * 13 w king            <br />
     * 14 b king            <br />
     */
    int[] countPieces() {

        int [] results = new int[15];
        for (Piece [] row: game.board) {
            for (Piece piece: row) {
                if(piece != null) {
                    results[2]++;
                    if(piece.isWhite) {
                        results[0]++;
                    } else {
                        results[1]++;
                    }
                    int offSet = piece.isWhite ? 0 : 1;
                    switch(Character.toLowerCase(piece.getFenSymbol())) {
                        case 'p':
                            results[3 + offSet]++;
                            break;
                        case 'b':
                            results[5 + offSet]++;
                            break;
                        case 'r':
                            results[7 + offSet]++;
                            break;
                        case 'n':
                            results[9 + offSet]++;
                            break;
                        case 'q':
                            results[11 + offSet]++;
                            break;
                        case 'k':
                            results[13 + offSet]++;
                            break;
                    }
                }
            }
        }
        return results;
    }
}
