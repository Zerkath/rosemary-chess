package com.github.zerkath.rosemary.MoveGeneration;

import com.github.zerkath.rosemary.DataTypes.Board;
import com.github.zerkath.rosemary.DataTypes.MoveUtil;
import com.github.zerkath.rosemary.DataTypes.Moves;
import com.github.zerkath.rosemary.DataTypes.Pieces;
import com.github.zerkath.rosemary.DataTypes.Utils;

public class Commons {

  static boolean pieceMoveNotPossible(
      int rowOffset, int colOffset, Board board, Moves moves, short origin, boolean isWhite) {
    short destination =
        Utils.getCoordinate(
            MoveUtil.getRow(origin) + rowOffset, MoveUtil.getColumn(origin) + colOffset);
    int target = board.getCoordinate(destination);

    if (target == 0) {
      moves.add(origin, destination);
      return false;
    }
    if (isWhite != Pieces.isWhite(target)) moves.add(origin, destination);
    return true;
  }
}
