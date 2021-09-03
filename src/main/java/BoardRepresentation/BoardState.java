package BoardRepresentation;

import DataTypes.*;
import CommonTools.Utils;

public class BoardState {
    public Board board = new Board();
    public BoardState previous;
    public PlayerTurn turn;

    public int turnNumber = 1;
    public int halfMove = 0;

    public int [] pieces;

    public Coordinate enPassant;
    public BoardState() {}

    public BoardState(BoardState state) {
        setBoardState(state);
    }

    public void setBoardState(BoardState state) {

        this.board = new Board(state.board);
        this.halfMove = state.halfMove;
        this.turn = state.turn;
        this.enPassant = state.enPassant;
        this.turnNumber = state.turnNumber;
        this.previous = state.previous;
//        this.pieces = countPieces();
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
            CastlingRights curr = white ? board.getBlackCastling() : board.getWhiteCastling();
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
    public void addRow(String rowData, int row) {
        int column = 0;
        for (Character ch : rowData.toCharArray()) {
            if(Character.isDigit(ch)) {
                int numOfEmpty = Character.digit(ch, 10);
                for (int j = 0; j < numOfEmpty; j++) {
                    board.clearCoordinate(new Coordinate(row, column));
                    column++;
                }
            } else {
                board.replaceCoordinate(new Coordinate(row, column), new Piece(ch));
                column++;
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

    private void addEnPassantMove(boolean white, Piece piece, int dCol, int dRow) {
        if(white && !Utils.isWhite(piece)) {
            enPassant = new Coordinate(dCol, dRow+1);
        }
        if(!white && Utils.isWhite(piece)) {
            enPassant = new Coordinate(dCol, dRow-1);
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


    public void makeMove(Move move) {

        previous = new BoardState(this);

        int dRow = move.destination.row;
        int dCol = move.destination.column;
        Piece selected = board.getCoordinate(move.origin);
        boolean isBeingPromoted = move.promotion != '-';
        boolean isWhite = selected.getColour() == Colour.WHITE;

        checkForCastlingRights(move);

        if(selected.getType() == PieceType.PAWN || board.getCoordinate(move.destination) != null) {
            halfMove = 0;
        } else {
            halfMove++;
        }

        if(selected.getType() == PieceType.PAWN &&
                enPassant != null &&
                enPassant.row == move.destination.row &&
                enPassant.column == move.destination.column
        ) {
            int offSet = isWhite ? 1 : -1;
            board.clearCoordinate(new Coordinate((enPassant.row + offSet), enPassant.column));
        }

        enPassant = null;
        //add En passant
        if(selected.getType() == PieceType.PAWN &&
                ((move.origin.row == 6 && move.destination.row == 4) || (move.origin.row == 1 && move.destination.row == 3))) {
            Piece right = null;
            Piece left = null;
            if(dCol == 0) right = board.getCoordinate(new Coordinate(dRow, dCol+1));
            if(dCol == 7) left  = board.getCoordinate(new Coordinate(dRow, dCol-1));
            if(dCol > 0 && dCol < 7) {
                right = board.getCoordinate(new Coordinate(dRow, dCol+1));
                left  = board.getCoordinate(new Coordinate(dRow, dCol-1));
            }

            if(right != null && right.getType() == PieceType.PAWN) {
                addEnPassantMove(isWhite, right, dCol, dRow);
            }

            if(left != null && left.getType() == PieceType.PAWN) {
                addEnPassantMove(isWhite, left, dCol, dRow);
            }
        }

        //Castling
        if(selected.getType() == PieceType.KING &&
                move.origin.column == 4 &&
                ((move.origin.row == 0 && dRow == 0)  || move.origin.row == 7 && dRow == 7) && (dCol == 2 || dCol == 6)) {

            board.setCastling(CastlingRights.NONE, isWhite);

            Piece rook;
            if (dCol == 2) {
                rook = board.getCoordinate(new Coordinate(dRow, 0));
                board.replaceCoordinate(new Coordinate(dRow, 3), rook);
                board.clearCoordinate(new Coordinate(dRow, 0));
            } else {
                rook = board.getCoordinate(new Coordinate(dRow, 7));
                board.replaceCoordinate(new Coordinate(dRow, 5), rook);
                board.clearCoordinate(new Coordinate(dRow, 7));
            }
        }

        if(selected.getType() == PieceType.KING) {
            board.replaceKing(move.destination, selected);
            board.setCastling(CastlingRights.NONE, isWhite);
        }

        board.clearCoordinate(move.origin);
        Piece piece = selected;
        if(isBeingPromoted) {
            piece = new Piece(move.promotion);
        }
        board.isOpposingColourOrEmpty(move.destination, piece);

        if(this.turn == PlayerTurn.BLACK) {
            turnNumber++;
            this.turn = PlayerTurn.WHITE;
        } else {
            this.turn = PlayerTurn.BLACK;
        }
    }

//    private int[] countPieces() {
//
//        int [] results = new int[15];
//        for (char [] row: board.getRow()) {
//            for (char piece: row) {
//                if(piece != '-') {
//                    if(Utils.isWhite(piece)) {
//                        results[0]++;
//                    } else {
//                        results[1]++;
//                    }
//                    int offSet = Utils.isWhite(piece) ? 0 : 1;
//                    switch(Character.toLowerCase(piece)) {
//                        case 'p': results[3 + offSet]++; break;
//                        case 'b': results[5 + offSet]++; break;
//                        case 'r': results[7 + offSet]++; break;
//                        case 'n': results[9 + offSet]++; break;
//                        case 'q': results[11 + offSet]++; break;
//                        case 'k': results[13 + offSet]++; break;
//                    }
//                }
//            }
//        }
//        results[2] = results[0] + results[1];
//        return results;
//    }
//
//    public void updatePieceCount() {
//        this.pieces = countPieces();
//    }
}
