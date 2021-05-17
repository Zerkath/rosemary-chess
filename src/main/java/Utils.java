public class Utils {
    public char convertColumnToChar(int i) {
        return Character.forDigit(i + 'a', 10);
    }

    public int convertCharToColumn(char c) {
        return c - 'a';
    }

    public char convertToRowChar(int i) {
        return Character.forDigit(i+1, 10);
    }

    public int convertCharToRow(char c) {
        return 8 - Integer.parseInt(String.valueOf(c));
    }

    //give a string with 2 characters fen format (en passant)
    public int[] parseCoordinate(String str) {
        if(str.equals("-")) return null;
        char[] c = str.toCharArray();
        return new int[]{convertCharToRow(c[1]), convertCharToColumn(c[0])};
    }

    public String parseCoordinate(int[] coords) {
        return String.valueOf(convertColumnToChar(coords[1])) + convertToRowChar(coords[0]);
    }

    public String parseCommand(int[][] move) {
        return parseCoordinate(move[0]) + parseCoordinate(move[1]);
    }
}