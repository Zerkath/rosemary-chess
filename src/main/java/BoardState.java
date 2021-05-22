import java.util.LinkedList;

public class BoardState {
    char[][] board = new char[8][8];
    BoardState previous;
    PlayerTurn turn;


    CastlingRights whiteCastling = CastlingRights.NONE;
    CastlingRights blackCastling = CastlingRights.NONE;

    int turnNumber = 1;
    int halfMove = 0;

    Coordinate enPassant;

    public BoardState() {

    }

    public BoardState(BoardState state) {
        setBoardState(state);
    }

    public void setBoardState(BoardState state) {

        this.blackCastling = state.blackCastling;
        this.whiteCastling = state.whiteCastling;
        this.halfMove = state.halfMove;
        this.turn = state.turn;
        this.enPassant = state.enPassant;
        this.turnNumber = state.turnNumber;
        this.previous = state.previous;
        for (int i = 0; i < state.board.length; i++) {
            System.arraycopy(state.board[i], 0, this.board[i], 0, state.board.length);
        }
    }

    public BoardState(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, this.board[i], 0, board.length);
        }
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
                    board[index][c] = '-';
                    c++;
                }
            } else {
                board[index][c] = ch;
                c++;
            }
        }
    }

    public void unMakeMove() {
        setBoardState(previous);
    }

    public void movePiece(Move move) {

        previous = new BoardState(this);

        int dRow = move.destination.row;
        int dCol = move.destination.column;
        char selected = MoveGenerator.getCoordinate(move.origin, board);
        if(Character.toLowerCase(selected) == 'p' || MoveGenerator.getCoordinate(move.destination, board) != '-') {
            halfMove = 0;
        } else {
            halfMove++;
        }

        enPassant = null;
        //add En passant
        if(Character.toLowerCase(selected) == 'p' && (move.origin.row == 6 && move.destination.row == 4) || (move.origin.row == 1 && move.destination.row == 3)) {
            boolean white = MoveGenerator.isWhite(selected);
            char right = '-';
            char left = '-';
            if(dCol == 0) right = board[dRow][dCol+1];
            if(dCol == 7) left  = board[dRow][dCol-1];
            if(dCol > 0 && dCol < 7) {
                right = board[dRow][dCol+1];
                left  = board[dRow][dCol-1];
            }

            if(Character.toLowerCase(right) == 'p') {
                if(white && !MoveGenerator.isWhite(right)) {
                    enPassant = new Coordinate(dCol, dRow+1);
                }
                if(!white && MoveGenerator.isWhite(right)) {
                    enPassant = new Coordinate(dCol, dRow-1);
                }
            }

            if(Character.toLowerCase(left) == 'p') {
                if(white && !MoveGenerator.isWhite(left)) {
                    enPassant = new Coordinate(dCol, dRow+1);
                }
                if(!white && MoveGenerator.isWhite(left)) {
                    enPassant = new Coordinate(dCol, dRow-1);
                }
            }
        }

        //Castling
        if(Character.toLowerCase(selected) == 'k' && (dCol == 2 || dCol == 6)) {
            if (MoveGenerator.isWhite(selected)) {
                whiteCastling = CastlingRights.NONE;
            } else {
                blackCastling = CastlingRights.NONE;
            }

            char rook;
            if (dCol == 2) {
                rook = board[dRow][0];
                board[dRow][3] = rook;
                board[dRow][0] = '-';
            } else {
                rook = board[dRow][7];
                board[dRow][5] = rook;
                board[dRow][7] = '-';
            }
        }

        if(Character.toLowerCase(selected) == 'k') {
            if(MoveGenerator.isWhite(selected)) {
                this.whiteCastling = CastlingRights.NONE;
            } else {
                this.blackCastling = CastlingRights.NONE;
            }
        }

        removePiece(move.origin);
        replaceSquare(move.destination, selected);
        if(this.turn == PlayerTurn.BLACK) {
            turnNumber++;
            this.turn = PlayerTurn.WHITE;
        } else {
            this.turn = PlayerTurn.BLACK;
        }

    }

    private void removePiece(Coordinate coord) {
        replaceSquare(coord, '-');
    }

    private void replaceSquare(Coordinate coord, char piece) {
        board[coord.row][coord.column] = piece;
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
        for (char [] row: board) {
            for (char piece: row) {
                if(piece != '-') {
                    if(MoveGenerator.isWhite(piece)) {
                        results[0]++;
                    } else {
                        results[1]++;
                    }
                    int offSet = MoveGenerator.isWhite(piece) ? 0 : 1;
                    switch(Character.toLowerCase(piece)) {
                        case 'p': results[3 + offSet]++; break;
                        case 'b': results[5 + offSet]++; break;
                        case 'r': results[7 + offSet]++; break;
                        case 'n': results[9 + offSet]++; break;
                        case 'q': results[11 + offSet]++; break;
                        case 'k': results[13 + offSet]++; break;
                    }
                }
            }
        }
        results[2] = results[0] + results[1];
        return results;
    }
}
