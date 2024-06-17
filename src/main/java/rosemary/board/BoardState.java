package rosemary.board;

import java.util.Arrays;
import rosemary.types.*;

public class BoardState {

    // this could be stored in just 4bits
    // 0b00 no castling rights
    // 0b01 kingside
    // 0b10 queenside
    // 0b11 both
    //
    // << 2 to store black or white
    // remainder space could be used for the flag for whose turn it is
    // private byte whiteCastlingRights = CastlingRights.NONE;
    // private byte blackCastlingRights = CastlingRights.NONE;
    // Offsets and masks
    private static final byte CASTLING_OFFSET = 2;
    private static final byte TURN_OFFSET = 4;
    private static final byte CASTLING_MASK = 0b1111;
    private byte moveAndCastling = 0;

    private byte turnNumber = 1;
    private byte halfMove = 0;
    private byte whiteKing = -1;
    private byte blackKing = -1;
    private byte enPassant = -1;

    private byte[] board = new byte[64];

    public BoardState() {
        Arrays.fill(board, (byte) 0);
    }

    public BoardState(BoardState state) {
        setBoardState(state);
    }

    public BoardState(String fen) {
        setBoardState(FenUtils.parseFen(fen));
    }

    public void setBoardState(BoardState state) {
        this.board = BoardUtils.copy(state.board);
        this.turnNumber = state.turnNumber;
        this.halfMove = state.halfMove;
        this.enPassant = state.enPassant;
        this.moveAndCastling = state.moveAndCastling;
        this.whiteKing = state.whiteKing;
        this.blackKing = state.blackKing;
    }

    /**
     * returns a piece map of the board state index is the piece number, value is the number of
     * pieces on the board there are some empty spaces in the array, but maybe cache locality is
     * worth it?
     */
    public byte[] getPieceMap() {
        byte[] pieceMap = new byte[23]; // 22 is the max value of king + white
        Arrays.fill(pieceMap, (byte) 0); // ensure values are initialised

        // unrolled loop for performance
        for (int i = 0; i < 64; i += 8) {
            byte piece1 = board[i];
            byte piece2 = board[i + 1];
            byte piece3 = board[i + 2];
            byte piece4 = board[i + 3];
            byte piece5 = board[i + 4];
            byte piece6 = board[i + 5];
            byte piece7 = board[i + 6];
            byte piece8 = board[i + 7];

            if (piece1 != 0) pieceMap[piece1] = (byte) (1 + pieceMap[piece1]);
            if (piece2 != 0) pieceMap[piece2] = (byte) (1 + pieceMap[piece2]);
            if (piece3 != 0) pieceMap[piece3] = (byte) (1 + pieceMap[piece3]);
            if (piece4 != 0) pieceMap[piece4] = (byte) (1 + pieceMap[piece4]);
            if (piece5 != 0) pieceMap[piece5] = (byte) (1 + pieceMap[piece5]);
            if (piece6 != 0) pieceMap[piece6] = (byte) (1 + pieceMap[piece6]);
            if (piece7 != 0) pieceMap[piece7] = (byte) (1 + pieceMap[piece7]);
            if (piece8 != 0) pieceMap[piece8] = (byte) (1 + pieceMap[piece8]);
        }

        return pieceMap;
    }

    public String toFenString() {
        return FenUtils.getFenString(this);
    }

    public void printBoard(BoardState board) {
        System.out.println(FenUtils.getFenString(board));
        System.out.println(board.board.toString());
    }

    public void printBoard() {
        printBoard(this);
    }

    public void setBlackKing(byte coordinate) {
        this.blackKing = coordinate;
    }

    public void setWhiteKing(byte coordinate) {
        this.whiteKing = coordinate;
    }

    // return (byte) ((byte)(moveAndCastling & castlingMask) >> castlingOffset);
    // return (byte) ((byte)(moveAndCastling & castlingMask) & 0b11);

    public byte getWhiteCastling() {
        // Mask out the black castling rights and shift right to get white castling rights
        return (byte) (moveAndCastling & 0b11);
    }

    public byte getBlackCastling() {
        // Mask out the white castling rights and shift right to get black castling rights
        return (byte) ((moveAndCastling >> CASTLING_OFFSET) & 0b11);
    }

    public void setWhiteCastling(byte rights) {
        // Clear the current white castling rights
        moveAndCastling &= 0b11111100;
        // Set the new white castling rights
        moveAndCastling |= (rights & 0b11);
    }

    public void setBlackCastling(byte rights) {
        // Clear the current black castling rights
        moveAndCastling &= 0b11110011;
        // Set the new black castling rights
        moveAndCastling |= ((rights & 0b11) << CASTLING_OFFSET);
    }

    public byte getWhiteKing() {
        return whiteKing;
    }

    public byte getBlackKing() {
        return blackKing;
    }

    public byte[] getBoard() {
        return board;
    }

    public byte getCoordinate(byte coord) {
        return board[coord];
    }

    public byte setCoordinate(byte coord, byte piece) {
        return board[coord] = piece;
    }

    public void setHalfMove(byte moveC) {
        this.halfMove = moveC;
    }

    public boolean isWhiteTurn() {
        return (this.moveAndCastling >> TURN_OFFSET) == 1;
    }

    public void setWhiteTurn(boolean isWhiteTurn) {
        this.moveAndCastling =
                (byte)
                        (this.moveAndCastling & CASTLING_MASK
                                | ((isWhiteTurn ? 1 : 0) << TURN_OFFSET));
    }

    public void setTurnNumber(byte turnNumber) {
        this.turnNumber = turnNumber;
    }

    public void setEnPassant(byte enPassant) {
        this.enPassant = enPassant;
    }

    public byte getEnPassant() {
        return enPassant;
    }

    public byte getHalfMove() {
        return halfMove;
    }

    public byte getTurnNumber() {
        return turnNumber;
    }

    public void setCastling(byte rights, boolean white) {
        if (white) setWhiteCastling(rights);
        else setBlackCastling(rights);
    }

    private void replaceKing(byte coordinate, int piece) {
        if (Pieces.isWhite(piece)) setWhiteKing(coordinate);
        else setBlackKing(coordinate);
    }

    public void replaceCoordinate(byte coordinate, byte piece) {
        if (piece != 0 && Pieces.getType(piece) == Pieces.KING) replaceKing(coordinate, piece);
        board[coordinate] = piece;
    }

    public void setCastling(char[] castling) {
        if (castling.length == 1 && castling[0] == '-') {

            setWhiteCastling(CastlingRights.NONE);
            setBlackCastling(CastlingRights.NONE);
            return;
        }

        for (char c : castling) {
            boolean white = !Character.isLowerCase(c);
            boolean queen = Character.toLowerCase(c) == 'q';
            byte curr = white ? getWhiteCastling() : getBlackCastling();
            if (curr == CastlingRights.NONE) {
                curr = queen ? CastlingRights.QUEEN : CastlingRights.KING;
            } else if (curr == CastlingRights.BOTH) {
                break;
            } else {
                if (curr == CastlingRights.QUEEN) {
                    if (!queen) curr = CastlingRights.BOTH;
                } else {
                    if (queen) curr = CastlingRights.BOTH;
                }
            }

            setCastling(curr, white);
        }
    }

    /**
     * used to add rows of FEN data to the board state
     *
     * @param rowData a row of FEN
     * @param row which row to place the fen
     */
    public void addRow(String rowData, int row) {
        int column = 0;
        for (Character ch : rowData.toCharArray()) {
            if (Character.isDigit(ch)) {
                int numOfEmpty = Character.digit(ch, 10);
                for (int j = 0; j < numOfEmpty; j++) {
                    board[Utils.getCoordinate(row, column)] = 0; // clearing the coordinate
                    column++;
                }
            } else {
                byte piece = Pieces.getNum(ch);
                replaceCoordinate(Utils.getCoordinate(row, column), piece);
                column++;
            }
        }
    }
}
