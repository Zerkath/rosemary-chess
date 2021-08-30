package BoardRepresentation;

import DataTypes.*;
import CommonTools.Utils;
import MoveGenerator.MoveGenerator;

public class BoardState {
    public char[][] board = new char[8][8];
    public BoardState previous;
    public PlayerTurn turn;

    public CastlingRights whiteCastling = CastlingRights.NONE;
    public CastlingRights blackCastling = CastlingRights.NONE;

    public int turnNumber = 1;
    public int halfMove = 0;

    public int [] pieces;

    public Coordinate enPassant;

    public Coordinate whiteKing;
    public Coordinate blackKing;

    public BoardState() { }

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
        this.pieces = countPieces();
        this.blackKing = state.blackKing;
        this.whiteKing = state.whiteKing;
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
                if(Character.toLowerCase(ch) == 'k') {
                    Coordinate pos = new Coordinate(c, index);
                    if(MoveGenerator.isWhite(ch)) {
                        whiteKing = pos;
                    } else {
                        blackKing = pos;
                    }
                }
                c++;
            }
        }
    }

    public void playMoves(String [] moves) {
        for (String move : moves) {
            this.makeMove(Utils.parseCommand(move));
        }
    }

    public void unMakeMove() {
        setBoardState(previous);
    }

    private void addEnPassantMove(boolean white, char destination, int dCol, int dRow) {
        if(white && !MoveGenerator.isWhite(destination)) {
            enPassant = new Coordinate(dCol, dRow+1);
        }
        if(!white && MoveGenerator.isWhite(destination)) {
            enPassant = new Coordinate(dCol, dRow-1);
        }
    }

    private void checkForCastlingRights(Move move) {
        int oRow = move.origin.row;
        int oCol = move.origin.column;
        int dRow = move.destination.row;
        int dCol = move.destination.column;

        if(oRow == 0 && oCol == 0 || dRow == 0 && dCol == 0) { //black queen side rook
            if(blackCastling == CastlingRights.BOTH) {
                blackCastling = CastlingRights.KINGSIDE;
            } else if(blackCastling == CastlingRights.QUEENSIDE) {
                blackCastling = CastlingRights.NONE;
            }
        } else if(oRow == 0 && oCol == 7 || dRow == 0 && dCol == 7) { //black king side rook
            if(blackCastling == CastlingRights.BOTH) {
                blackCastling = CastlingRights.QUEENSIDE;
            } else if(blackCastling == CastlingRights.KINGSIDE) {
                blackCastling = CastlingRights.NONE;
            }
        } else if(oRow == 7 && oCol == 0 || dRow == 7  && dCol == 0) { //white queen side rook
            if(whiteCastling == CastlingRights.BOTH) {
                whiteCastling = CastlingRights.KINGSIDE;
            } else if(whiteCastling == CastlingRights.QUEENSIDE) {
                whiteCastling = CastlingRights.NONE;
            }
        } else if(oRow == 7 && oCol == 7 || dRow == 7 && dCol == 7) { //white king side rook
            if(whiteCastling == CastlingRights.BOTH) {
                whiteCastling = CastlingRights.QUEENSIDE;
            } else if(whiteCastling == CastlingRights.KINGSIDE) {
                whiteCastling = CastlingRights.NONE;
            }
        }
    }


    public void makeMove(Move move) {

        previous = new BoardState(this);

        int dRow = move.destination.row;
        int dCol = move.destination.column;
        char selected = MoveGenerator.getCoordinate(move.origin, board);
        char nSelected = Character.toLowerCase(selected);
        boolean isBeingPromoted = move.promotion != '-';
        boolean isWhite = MoveGenerator.isWhite(selected);

        checkForCastlingRights(move);

        if(nSelected == 'p' || MoveGenerator.getCoordinate(move.destination, board) != '-') {
            halfMove = 0;
        } else {
            halfMove++;
        }

        if(nSelected == 'p' && enPassant != null && enPassant.row == move.destination.row && enPassant.column == move.destination.column) {
            int offSet = isWhite ? 1 : -1;
            board[enPassant.row + offSet][enPassant.column] = '-';
        }

        enPassant = null;
        //add En passant
        if(nSelected == 'p' && ((move.origin.row == 6 && move.destination.row == 4) || (move.origin.row == 1 && move.destination.row == 3))) {
            char right = '-';
            char left = '-';
            if(dCol == 0) right = board[dRow][dCol+1];
            if(dCol == 7) left  = board[dRow][dCol-1];
            if(dCol > 0 && dCol < 7) {
                right = board[dRow][dCol+1];
                left  = board[dRow][dCol-1];
            }

            if(Character.toLowerCase(right) == 'p') {
                addEnPassantMove(isWhite, right, dCol, dRow);
            }

            if(Character.toLowerCase(left) == 'p') {
                addEnPassantMove(isWhite, left, dCol, dRow);
            }
        }

        //Castling
        if(nSelected == 'k' && move.origin.column == 4 && ((move.origin.row == 0 && dRow == 0)  || move.origin.row == 7 && dRow == 7) && (dCol == 2 || dCol == 6)) {
            if (isWhite) {
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

        if(nSelected == 'k') {
            if(isWhite) {
                this.whiteKing = move.destination;
                this.whiteCastling = CastlingRights.NONE;
            } else {
                this.blackKing = move.destination;
                this.blackCastling = CastlingRights.NONE;
            }
        }

        removePiece(move.origin);
        char piece = selected;
        if(isBeingPromoted) {
            if(isWhite) {
                piece = Character.toUpperCase(move.promotion);
            } else {
                piece = Character.toLowerCase(move.promotion);
            }
        }
        replaceSquare(move.destination, piece);

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
    private int[] countPieces() {

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

    public void updatePieceCount() {
        this.pieces = countPieces();
    }
}
