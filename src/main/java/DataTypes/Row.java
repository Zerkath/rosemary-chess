package DataTypes;

public class Row {
    private final Piece[] row = new Piece[8];

    public Row() {
    }

    public Row(Row row) {
        for (int column = 0; column < 8; column++) {
            replaceColumn(column, row.getColumn(column));
        }
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
