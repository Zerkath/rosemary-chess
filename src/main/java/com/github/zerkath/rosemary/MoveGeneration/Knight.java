package com.github.zerkath.rosemary.MoveGeneration;

import com.github.zerkath.rosemary.BoardRepresentation.BoardState;
import com.github.zerkath.rosemary.DataTypes.*;

public class Knight {

  static Moves[] knightMoves = new Moves[64];

  static {
    for (short origin = 0; origin < 64; origin++) {
      int row = MoveUtil.getRow(origin);
      int col = MoveUtil.getColumn(origin);

      int[] columns = new int[] {col - 2, col + 2};
      int[] rows = new int[] {row - 2, row + 2};
      Moves moves = new Moves();
      for (int d_column : columns) {
        Utils.addToCollection(row + 1, d_column, row, col, moves);
        Utils.addToCollection(row - 1, d_column, row, col, moves);
      }

      for (int d_row : rows) {
        Utils.addToCollection(d_row, col + 1, row, col, moves);
        Utils.addToCollection(d_row, col - 1, row, col, moves);
      }
      knightMoves[origin] = moves;
    }
  }

  public static void getMoves(short origin, BoardState boardState, Moves moves) {

    Board board = boardState.board;
    Moves n_moves = knightMoves[origin];

    for (short move : n_moves) {
      if (board.isValidMove(move)) moves.add(move);
    }
  }
}
