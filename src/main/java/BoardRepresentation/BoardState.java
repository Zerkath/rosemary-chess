package BoardRepresentation;

import DataTypes.*;

import java.util.HashMap;
import java.util.Map;

public class BoardState {

    private final static String default_fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private final StringBuilder strBuilder = new StringBuilder();

    public Board board = new Board();
    public BoardState previous;
    public boolean isWhiteTurn;

    public int turnNumber = 1;
    public int halfMove = 0;

    public Map<Integer, Integer> pieceMap = new HashMap<>();

    public Coordinate enPassant;
    public BoardState() {}

    public BoardState(BoardState state) {
        setBoardState(state);
    }

    public BoardState(String fen) {
        setBoardState(parseFen(fen));
    }

    public void setBoardState(BoardState state) {
        this.board = new Board(state.board);
        this.previous = state.previous;
        this.isWhiteTurn = state.isWhiteTurn;
        this.turnNumber = state.turnNumber;
        this.halfMove = state.halfMove;
        this.enPassant = state.enPassant;
    }

    public void setCastling(char [] castling) {
        if(castling.length == 1 && castling[0] == '-') {
            board.setWhiteCastlingRights(CastlingRights.NONE);
            board.setBlackCastlingRights(CastlingRights.NONE);
            return;
        }

        for (char c : castling) {
            boolean white = !Character.isLowerCase(c);
            boolean queen = Character.toLowerCase(c) == 'q';
            CastlingRights curr = white ? board.getWhiteCastling() : board.getBlackCastling();
            if(curr == CastlingRights.NONE) {
                curr = queen ? CastlingRights.QUEEN : CastlingRights.KING;
            } else if(curr == CastlingRights.BOTH) {
                break;
            } else {
                if (curr == CastlingRights.QUEEN) {
                    if (!queen) curr = CastlingRights.BOTH;
                } else {
                    if (queen) curr = CastlingRights.BOTH;
                }
            }

            board.setCastling(curr, white);
        }
    }

    /**
     * used to add rows of FEN data to the board state
     * @param rowData a row of FEN
     * @param row which row to place the fen
     */
    private void addRow(String rowData, int row) {
        int column = 0;
        for (Character ch : rowData.toCharArray()) {
            if(Character.isDigit(ch)) {
                int numOfEmpty = Character.digit(ch, 10);
                for (int j = 0; j < numOfEmpty; j++) {
                    board.clearCoordinate(new Coordinate(row, column));
                    column++;
                }
            } else {
                int piece = Pieces.getNum(ch);
                board.replaceCoordinate(new Coordinate(row, column), piece);
                incrementPiece(piece);
                column++;
            }
        }
    }

    public void playMoves(String [] moves) {
        for (String move : moves) {
            this.makeMove(new Move(move));
        }
    }

    private void addEnPassantMove(boolean white, int piece, int dCol, int dRow) {
        boolean pieceIsWhite = Pieces.isWhite(piece);
        if(white && !pieceIsWhite) {
            enPassant = new Coordinate(dRow+1, dCol);
        }
        if(!white && pieceIsWhite) {
            enPassant = new Coordinate(dRow-1, dCol);
        }
    }

    private void checkForCastlingRights(Move move) {
        int oRow = move.origin.row;
        int oCol = move.origin.column;
        int dRow = move.destination.row;
        int dCol = move.destination.column;

        CastlingRights blackCastling = board.getBlackCastling();
        CastlingRights whiteCastling = board.getWhiteCastling();

        if(oRow == 0 && oCol == 0 || dRow == 0 && dCol == 0) { //black queen side rook
            if(blackCastling == CastlingRights.BOTH) {
                board.setBlackCastlingRights(CastlingRights.KING);
            } else if(blackCastling == CastlingRights.QUEEN) {
                board.setBlackCastlingRights(CastlingRights.NONE);
            }
        } else if(oRow == 0 && oCol == 7 || dRow == 0 && dCol == 7) { //black king side rook
            if(blackCastling == CastlingRights.BOTH) {
                board.setBlackCastlingRights(CastlingRights.QUEEN);
            } else if(blackCastling == CastlingRights.KING) {
                board.setBlackCastlingRights(CastlingRights.NONE);
            }
        } else if(oRow == 7 && oCol == 0 || dRow == 7  && dCol == 0) { //white queen side rook
            if(whiteCastling == CastlingRights.BOTH) {
                board.setWhiteCastlingRights(CastlingRights.KING);
            } else if(whiteCastling == CastlingRights.QUEEN) {
                board.setWhiteCastlingRights(CastlingRights.NONE);
            }
        } else if(oRow == 7 && oCol == 7 || dRow == 7 && dCol == 7) { //white king side rook
            if(whiteCastling == CastlingRights.BOTH) {
                board.setWhiteCastlingRights(CastlingRights.QUEEN);
            } else if(whiteCastling == CastlingRights.KING) {
                board.setWhiteCastlingRights(CastlingRights.NONE);
            }
        }
    }

    public void unMakeMove() {
        setBoardState(previous);
    }

    public void makeMove(Move move) {
        previous = new BoardState(this);

        int dRow = move.destination.row;
        int dCol = move.destination.column;
        int selected = board.getCoordinate(move.origin);
        boolean isBeingPromoted = move.promotion != 0;
        boolean isWhite = Pieces.isWhite(selected);

        checkForCastlingRights(move);

        if(Pieces.getType(selected) == Pieces.PAWN || board.getCoordinate(move.destination) != 0) {
            halfMove = 0;
        } else {
            halfMove++;
        }

        if(Pieces.getType(selected) == Pieces.PAWN &&
                enPassant != null &&
                enPassant.row == move.destination.row &&
                enPassant.column == move.destination.column
        ) {
            int offSet = isWhite ? 1 : -1;
            board.clearCoordinate(enPassant.row + offSet, enPassant.column);
        }

        enPassant = null;
        //add En passant
        if(Pieces.getType(selected) == Pieces.PAWN &&
                ((move.origin.row == 6 && move.destination.row == 4) || (move.origin.row == 1 && move.destination.row == 3))) {
            int right = 0;
            int left = 0;
            if(dCol == 0) right = board.getCoordinate(dRow, dCol+1);
            if(dCol == 7) left  = board.getCoordinate(dRow, dCol-1);
            if(dCol > 0 && dCol < 7) {
                right = board.getCoordinate(dRow, dCol+1);
                left  = board.getCoordinate(dRow, dCol-1);
            }

            if(Pieces.getType(right) == Pieces.PAWN) {
                addEnPassantMove(isWhite, right, dCol, dRow);
            }

            if(Pieces.getType(left) == Pieces.PAWN) {
                addEnPassantMove(isWhite, left, dCol, dRow);
            }
        }

        //Castling
        if(Pieces.getType(selected) == Pieces.KING &&
                move.origin.column == 4 &&
                ((move.origin.row == 0 && dRow == 0)  || move.origin.row == 7 && dRow == 7) && (dCol == 2 || dCol == 6)) {

            board.setCastling(CastlingRights.NONE, isWhite);

            int rook;
            Coordinate destination;
            Coordinate origin;
            if (dCol == 2) {
                destination = new Coordinate(dRow, 3);
                origin = new Coordinate(dRow, 0);
            } else {
                destination = new Coordinate(dRow, 5);
                origin = new Coordinate(dRow, 7);
            }
            rook = board.getCoordinate(origin);
            board.replaceCoordinate(destination, rook);
            board.clearCoordinate(origin);
        }

        if(Pieces.getType(selected) == Pieces.KING) {
            board.setCastling(CastlingRights.NONE, isWhite);
        }

        board.clearCoordinate(move.origin);

        int piece = selected;
        if(isBeingPromoted) {
            piece = move.promotion;
        }
        board.replaceCoordinate(move.destination, piece);

        if(!this.isWhiteTurn) turnNumber++;

        this.isWhiteTurn = !this.isWhiteTurn;
    }

    private void incrementPiece(int piece) {
        if(piece == 0) return;
        pieceMap.merge(piece, 1, Integer::sum);
    }

    public void updatePieceCount() {
        pieceMap.clear();
        for (int piece: board.getBoard()) {
            incrementPiece(piece);
        }
    }

    public String toFenString() {
        return getFenString(this);
    }

    public String getFenString(BoardState boardState) {
        strBuilder.setLength(0);
        Board board = boardState.board;

        for (int row = 0; row < 8; row++) {
            int empty = 0;
            for (int column = 0; column < 8; column++) {
                int piece = board.getCoordinate(row, column);
                if(piece == 0) {
                    empty++;
                } else if(empty != 0) {
                    strBuilder.append(empty);
                    empty = 0;
                    strBuilder.append(Pieces.getChar(piece));
                } else strBuilder.append(Pieces.getChar(piece));
            }
            if(empty != 0) strBuilder.append(empty);
            if(row != 7) strBuilder.append("/");
        }

        strBuilder.append(boardState.isWhiteTurn ? " w" : " b");

        String WhiteCastlingString = "";
        switch (board.getWhiteCastling()) {
            case KING: WhiteCastlingString = "K"; break;
            case QUEEN: WhiteCastlingString = "Q"; break;
            case BOTH: WhiteCastlingString = "KQ"; break;
        }

        String BlackCastlingString = "";
        switch (board.getBlackCastling()) {
            case KING: BlackCastlingString = "k"; break;
            case QUEEN: BlackCastlingString = "q"; break;
            case BOTH: BlackCastlingString = "kq"; break;
        }

        if(WhiteCastlingString.length() < 1 && BlackCastlingString.length() < 1) {
            strBuilder.append(" - ");
        } else {
            strBuilder.append(" ")
                    .append(WhiteCastlingString)
                    .append(BlackCastlingString)
                    .append(" ");
        }

        strBuilder.append(boardState.enPassant != null ? boardState.enPassant : "-");

        strBuilder.append(" ")
                .append(boardState.halfMove)
                .append(" ")
                .append(boardState.turnNumber);

        return strBuilder.toString();
    }

    public BoardState parseFen(String fen) {

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

        BoardState boardState = new BoardState();

        if(split.length != 6) return new BoardState(parseFen(default_fen)); //todo give out errors
        String [] rows = split[0].split("/");
        if(rows.length != 8) return new BoardState(parseFen(default_fen));


        boardState.isWhiteTurn = split[1].equals("w");

        boardState.setCastling(split[2].toCharArray()); //set castling rights
        boardState.turnNumber = Integer.parseInt(split[5]);

        boardState.enPassant = split[3].length() == 2 ? new Coordinate(split[3]) : null;

        for (int row = 0; row < rows.length; row++) {
            boardState.addRow(rows[row], row);
        }

        return boardState;
    }

    public void printBoard(BoardState board) {
        System.out.println(getFenString(board));
        System.out.println(board);
    }

    public void printBoard() {
        printBoard(this);
    }
}
