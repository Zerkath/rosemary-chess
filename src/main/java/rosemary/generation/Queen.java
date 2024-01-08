package rosemary.generation;

import rosemary.board.BoardState;
import rosemary.types.Moves;

public class Queen {

  public static void getMoves(short origin, BoardState boardState, Moves moves) {
    Rook.getMoves(origin, boardState, moves);
    Bishop.getMoves(origin, boardState, moves);
  }
}
