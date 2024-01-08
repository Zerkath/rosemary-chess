package com.github.zerkath.rosemary.MoveGeneration;

import com.github.zerkath.rosemary.BoardRepresentation.BoardState;
import com.github.zerkath.rosemary.types.Moves;

public class Queen {

  public static void getMoves(short origin, BoardState boardState, Moves moves) {
    Rook.getMoves(origin, boardState, moves);
    Bishop.getMoves(origin, boardState, moves);
  }
}
