import java.util.LinkedList;

/**
 * Contains data pertaining to the current game
 * board state
 * castling rights
 * player turns
 */
public class Game {
    Utils utils = new Utils();
    Piece[][] board = new Piece[8][8];
    enum CastlingRights {
        QUEENSIDE,
        KINGSIDE,
        BOTH,
        NONE
    }

    enum PlayerTurn {
        WHITE,
        BLACK,
    }

//    int[][][] threats = new int[8][8][2];
    CastlingRights whiteCastling = CastlingRights.NONE;
    CastlingRights blackCastling = CastlingRights.NONE;

    PlayerTurn turn;

    int turnNumber = 1;
    int halfMove = 0;

    //null if no enPassant this turn
    //coordinate of possible en passant
    int [] enPassant;

    LinkedList<LinkedList<int[]>> moves = new LinkedList<>();

    public Game() {
    }

//    public Piece[][] copyBoard(Game game) {
//        Piece [][] result = new Piece[8][8];
//
//        for (int i = 0; i < game.board.length; i++) {
//            for (int j = 0; j < game.board.length; j++) {
//                if(game.board[i][j] != null) {
//                    result[i][j] = game.board[i][j];
//                    result[i][j].setGameState(game);
//                }
//            }
//        }
//        return result;
//    }
//
//    public Game(Game game) {
//        this.board = game.board;
//        this.turn = game.turn;
//        this.whiteCastling = game.whiteCastling;
//        this.blackCastling = game.blackCastling;
//        this.moves = game.moves;
//        this.turnNumber = game.turnNumber;
//        this.halfMove = game.halfMove;
//        this.board = copyBoard(game);
//    }

    public void getPossibleMovesForTurn() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Piece piece = board[i][j];
                if(piece != null) {
                    if(turn == PlayerTurn.WHITE && piece.isWhite || turn == PlayerTurn.BLACK && !piece.isWhite) {
                        LinkedList<int[]> m = new LinkedList<>(piece.getPossibleMoves());
                        if(m.size() > 0) {
                            m.addFirst(new int[]{i, j});
                            moves.add(m);
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns the piece in a square
     * @param coords should contain indexes 0 and 1
     * @return Piece or null
     */
    public Piece getSquare(int [] coords) {
        return this.board[coords[0]][coords[1]];
    }

    public void removePiece(int [] coords) {
        this.board[coords[0]][coords[1]] = null;
    }

    public void setCastling(char [] castling) {
        if(castling.length == 1 && castling[0] == '-') {
            whiteCastling = CastlingRights.NONE;
            blackCastling = CastlingRights.NONE;
            return;
        }

        for (char c : castling) {
            boolean black = Character.isLowerCase(c);
            boolean queen = Character.toLowerCase(c) == 'q';
            CastlingRights curr = black ? blackCastling : whiteCastling;
            if(curr == CastlingRights.NONE) {
                curr = queen ? CastlingRights.QUEENSIDE : CastlingRights.KINGSIDE;
            } else if(curr == CastlingRights.BOTH) {
                break;
            } else {
                if (curr == CastlingRights.QUEENSIDE) {
                    if (!queen) curr = CastlingRights.BOTH;
                } else {
                    if (queen) curr = CastlingRights.BOTH;
                }
            }

            if(black) {
                blackCastling = curr;
            } else {
                whiteCastling = curr;
            }
        }
    }

    public void printPieceLegalMove(int row, int col) {
        if(this.board[row][col] == null) {
            System.out.println("No Piece");
            return;
        }
        System.out.print(this.board[row][col].getClass() + "\n");
        LinkedList<int[]> moves = this.board[row][col].getPossibleMoves();
        int c = 1;
        for (int[] move : moves) {
            System.out.println("Move " + c + "\t" + move[0] + " " + move[1]);
            c++;
        }
    }

    /**
     * Moves piece from starting square to destination square, leaving starting square null
     * @param startingSquare array where 0 is starting row and 1 is starting column
     * @param destinationSquare array where 0 is destination row and 1 is destination column
     * @return true if destination square is King
     */
    public boolean movePiece(int [] startingSquare, int [] destinationSquare) {
        enPassant = null;
        int dRow = destinationSquare[0];
        int dCol = destinationSquare[1];
        Piece selected = getSquare(startingSquare);
        if(selected instanceof Pawn || getSquare(destinationSquare) != null) {
            halfMove = 0;
        } else {
            halfMove++;
        }

        Piece destination = this.board[dRow][dCol];
        if(destination instanceof King) {
            return true; //todo end game
        }

        //En passant
        if(selected instanceof Pawn && startingSquare[0] - dRow == 2 || startingSquare[0] - dRow == -2) {
            boolean white = selected.isWhite;
            Piece right = null;
            Piece left = null;
            if(dCol == 0) right = board[dRow][dCol+1];
            if(dCol == 7) left  = board[dRow][dCol-1];
            if(dCol > 0 && dCol < 7) {
                right = board[dRow][dCol+1];
                left  = board[dRow][dCol-1];
            }
            if(right instanceof Pawn) {
                if(white && !right.isWhite) {
                    enPassant = new int[]{dRow-1, dCol};
                }
                if(!white && right.isWhite) {
                    enPassant = new int[]{dRow+1, dCol};
                }
            }

            if(left instanceof Pawn) {
                if(white && !left.isWhite) {
                    enPassant = new int[]{dRow-1, dCol};
                }
                if(!white && left.isWhite) {
                    enPassant = new int[]{dRow+1, dCol};
                }
            }
        }

        //Castling
        if(selected instanceof King && (dCol == 2 || dCol == 6)) {
            if(selected.isWhite) {
                this.whiteCastling = CastlingRights.NONE;
            } else {
                this.blackCastling = CastlingRights.NONE;
            }

            Piece rook;
            if(dCol == 2) {
                rook = board[dRow][0];
                rook.updatePosition(dRow, 3);
                board[dRow][3] = rook;
                board[dRow][0] = null;
            } else {
                rook = board[dRow][7];
                rook.updatePosition(dRow, 5);
                board[dRow][5] = rook;
                board[dRow][7] = null;
            }
            board[dRow][dCol] = selected;
            selected.updatePosition(dRow, dCol);
        } else {
            selected.updatePosition(dRow, dCol);
            this.board[dRow][dCol] = selected;
        }

        if(selected instanceof King) {
            if(selected.isWhite) {
                this.whiteCastling = CastlingRights.NONE;
            } else {
                this.blackCastling = CastlingRights.NONE;
            }
        }

        removePiece(startingSquare);
        if(this.turn == PlayerTurn.BLACK) {
            turnNumber++;
            this.turn = PlayerTurn.WHITE;
        } else {
            this.turn = PlayerTurn.BLACK;
        }

        return false;
    }

    public void printBoard() {
        System.out.println("  0 1 2 3 4 5 6 7");
        for(int i = 0; i < board.length; i++) {
            System.out.print(i + " ");
            for(int j = 0; j < board[i].length; j++) {
                Piece piece = board[i][j];
                if(piece != null) {
                    System.out.print(piece.getFenSymbol() + " ");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
        System.out.println();

    }

    /**
     * used to add rows of FEN data to the board state
     * @param row a row of FEN
     * @param index which row to place the fen
     */
    public void addRow(String row, int index) {
        int c = 0;
        for (Character ch : row.toCharArray()) {
            if(Character.isDigit(ch)) {
                int numOfEmpty = Character.digit(ch, 10);
                for (int j = 0; j < numOfEmpty; j++) {
                    board[index][c] = null;
                    c++;
                }
            } else {
                board[index][c] = characterToPiece(ch, index, c);
                c++;
            }
        }
    }

    public void parseFen(String fen) {

        /*
        split data indexes
        0 = fen board data
        1 = white or black turn
        2 = castling rights
        3 = en passant move
        4 = half-move clock (how many turns since last capture or pawn move 50 move rule)
        5 = full-move number starts at 1 incremented after blacks move or at the start of white move
        */
        String [] split = fen.split(" ");

        if(split.length != 6) return; //check if fen is somewhat valid TODO errors
        String [] rows = split[0].split("/");
        if(rows.length != 8) return; //another validity check

        if(split[1].equals("w")) {
            turn = PlayerTurn.WHITE;
        } else if (split[1].equals("b")) {
            turn = PlayerTurn.BLACK;
        }
        setCastling(split[2].toCharArray()); //set castling rights
        turnNumber = Integer.parseInt(split[5]);

        enPassant = utils.parseCoordinate(split[3]);

        // Add pieces to the board
        for (int i = 0; i < rows.length; i++) {
            addRow(rows[i], i);
        }
        for (Piece[] row : board) {
            for (Piece piece : row) {
                if(piece != null) piece.setGameState(this);
            }
        }
    }

    public String toFenString() {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < board.length; i++) {
            int empty = 0;
            for (int j = 0; j < board.length; j++) {
                if(board[i][j] == null) {
                    empty++;
                } else if(board[i][j] != null) {
                    if (empty != 0) {
                        result.append(empty);
                        empty = 0;
                    }
                    result.append(board[i][j].fenSymbol);
                }
            }
            if(empty != 0) {
                result.append(empty);
            }
            if(i != 7) result.append("/");
        }

        String turnChar = turn == PlayerTurn.BLACK ? " b" : " w";

        result.append(turnChar);
        String WhiteCastlingString = "";
        switch (whiteCastling) {
            case KINGSIDE: WhiteCastlingString = "K"; break;
            case QUEENSIDE: WhiteCastlingString = "Q"; break;
            case BOTH: WhiteCastlingString = "KQ"; break;
        }

        String BlackCastlingString = "";
        switch (blackCastling) {
            case KINGSIDE: BlackCastlingString = "k"; break;
            case QUEENSIDE: BlackCastlingString = "q"; break;
            case BOTH: BlackCastlingString = "kq"; break;
        }

        if(WhiteCastlingString.length() < 1 && BlackCastlingString.length() < 1) {
            result.append(" - ");
        } else {
            result.append(" ");
            result.append(WhiteCastlingString);
            result.append(BlackCastlingString);
            result.append(" ");
        }
        if(enPassant != null) {
            result.append(utils.parseCoordinate(enPassant));
        } else {
            result.append("-");
        }

        result.append(" ").append(halfMove).append(" ");
        result.append(turnNumber);
        return result.toString();
    }

    public Piece characterToPiece(char ch, int row, int col) {
        boolean isWhite = Character.isUpperCase(ch);

        switch(Character.toLowerCase(ch)) {
            case 'p': return new Pawn(row, col, isWhite);
            case 'b': return new Bishop(row, col, isWhite);
            case 'n': return new Knight(row, col, isWhite);
            case 'r': return new Rook(row, col, isWhite);
            case 'q': return new Queen(row, col, isWhite);
            case 'k': return new King(row, col, isWhite);
            default: return null;
        }
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
    public int[] countPieces() {

        int [] results = new int[15];
        for (Piece [] row: this.board) {
            for (Piece piece: row) {
                if(piece != null) {
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
        results[2] = results[0] + results[1];
        return results;
    }
}
