package DataTypes;

public class Coordinate {
    public Coordinate(int column, int row) {
        this.column = (byte)column;
        this.row = (byte)row;
    }
    public Coordinate() {}
    public byte column;
    public byte row;
}