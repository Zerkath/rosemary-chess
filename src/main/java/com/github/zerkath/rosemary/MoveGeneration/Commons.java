package com.github.zerkath.rosemary.MoveGeneration;

import com.github.zerkath.rosemary.DataTypes.Board;
import com.github.zerkath.rosemary.DataTypes.Coordinate;
import com.github.zerkath.rosemary.DataTypes.Moves;
import com.github.zerkath.rosemary.DataTypes.Pieces;
import com.github.zerkath.rosemary.DataTypes.Utils;

public class Commons {

  static boolean pieceMoveNotPossible(int rowOffset, int colOffset, Board board, Moves moves, Coordinate origin,
      boolean isWhite) {
    short destination = Utils.getCoordinate(origin.getRow() + rowOffset, origin.getColumn() + colOffset);
    int target = board.getCoordinate(destination);

    if (target == 0) {
      moves.add(origin, new Coordinate(destination));
      return false;
    }
    if (isWhite != Pieces.isWhite(target))
      moves.add(origin, new Coordinate(destination));
    return true;
  }
}
