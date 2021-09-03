package DataTypes;

public class Row {
    private final Piece[] row;

    public Row() {
        this.row = new Piece[8];
    }

    public Piece getColumn(int column) {
        return this.row[column];
    }

    public Piece replaceColumn(int column, Piece piece) {
        return this.row[column] = piece;
    }

    public Piece replaceColumn(int column, char piece) {
        return this.row[column] = new Piece(piece);
    }

    public void clearColumn(int column) {
        this.row[column] = null;
    }
}
