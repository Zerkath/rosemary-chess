package DataTypes;

public class Coordinate {
    public int column;
    public int row;

    public Coordinate(int row, int column) {
        this.column = column;
        this.row = row;
    }

    public Coordinate(String coordinateString) {
        column = toColumnInt(coordinateString.charAt(0));
        row = toRowInt(coordinateString.charAt(1));
    }

    public String toString() {
        return toColumnChar(column) + "" + toRowChar(row);
    }

    private char toColumnChar(int i) {
        return (char)(i + 'a');
    }

    private int toColumnInt(char c) {
        return c - 'a';
    }

    private char toRowChar(int i) {
        return Character.forDigit(8-i, 10);
    }

    private int toRowInt(char c) {
        return 8 - Integer.parseInt(String.valueOf(c));
    }
}