package rosemary.types;

public class Utils {

  public static short coordinateMask = 0b111;
  public static short moveMask = 0b111111;

  public static boolean isOutOfBounds(short coord) {
    return coord < 0 || coord > 64;
  }

  public static short getCoordinate(int row, int column) {
    return (short) ((row << 3) | column);
  }

  public static void addToCollection(
      int row, int column, int origin_row, int origin_col, Moves moves) {
    if (row < 0 || row > 7 || column < 0 || column > 7) return;
    moves.add(MoveUtil.getMove(getCoordinate(origin_row, origin_col), getCoordinate(row, column)));
  }

  public static short getCoordinate(String coordinateString) {
    return getCoordinate(
        toRowInt(coordinateString.charAt(1)), toColumnInt(coordinateString.charAt(0)));
  }

  public static String moveToString(short move) {
    int origin = (move >> 6);
    int destination = (move & Utils.moveMask);

    return coordinateToString((short) origin) + "" + coordinateToString((short) destination);
  }

  public static String coordinateToString(short coordinate) {
    int row = (coordinate >> 3);
    int column = (coordinate & coordinateMask);

    return toColumnChar(column) + "" + toRowChar(row);
  }

  static char toColumnChar(int i) {
    return (char) (i + 'a');
  }

  static int toColumnInt(char c) {
    return c - 'a';
  }

  static char toRowChar(int i) {
    return Character.forDigit(8 - i, 10);
  }

  static int toRowInt(char c) {
    return 8 - Integer.parseInt(String.valueOf(c));
  }
}
