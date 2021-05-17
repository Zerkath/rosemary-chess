public class Utils {
    public char toColumnCharacter(int i) {
        return (char)(i + 'a');
    }

    public int toColumnNumber(char c) {
        return c - 'a';
    }

    public char toRowNumberChar(int i) {
        return Character.forDigit(8-i, 10);
    }

    public int toRowNumber(char c) {
        return 8 - Integer.parseInt(String.valueOf(c));
    }

    //give a string with 2 characters fen format (en passant)
    public int[] parseCoordinate(String str) {
        if(str.equals("-")) return null;
        char[] c = str.toCharArray();
        return new int[]{toRowNumber(c[1]), toColumnNumber(c[0])};
    }

    public String parseCoordinate(int[] coords) {
        return String.valueOf(toColumnCharacter(coords[1])) + toRowNumberChar(coords[0]);
    }

    public String parseCommand(int[][] move) {
        return parseCoordinate(move[0]) + parseCoordinate(move[1]);
    }

    public int[][] parseCommand(String command) {
        return new int[][]{parseCoordinate(command.substring(0, 2)), parseCoordinate(command.substring(2, 4))};
    }
}