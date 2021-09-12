package DataTypes;

public class Row {
    private final Piece[] row = new Piece[8];

    public Row() {
    }

    public Row(Row row) {
        System.arraycopy(row.row, 0, this.row, 0, 8);
    }
    
    public Piece getColumn(int column) {
        return this.row[column];
    }

    public void replaceColumn(int column, Piece piece) {
        this.row[column] = piece;
    }

    public void clearColumn(int column) {
        this.row[column] = null;
    }
}
