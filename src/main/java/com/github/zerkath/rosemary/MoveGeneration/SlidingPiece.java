package com.github.zerkath.rosemary.MoveGeneration;

import com.github.zerkath.rosemary.DataTypes.*;

abstract class SlidingPiece {

  static Move getMove(int rowOffset, int colOffset, Coordinate origin) {
    return new Move(
        origin.coord,
        Utils.getCoordinate(origin.getRow() + rowOffset, origin.getColumn() + colOffset));
  }

  static TargetSquare getSquareState(Move move, Board board, boolean isWhite) {

    int target = board.getCoordinate(move.getDestination());
    if (target == 0) return TargetSquare.EMPTY;

    return isWhite == Pieces.isWhite(target) ? TargetSquare.FRIENDLY : TargetSquare.ENEMY;
  }

  static void addMove(TargetSquare state, Move move, Moves moves) {
    switch (state) {
      case EMPTY -> moves.add(move);
      case ENEMY -> moves.add(move);
      case FRIENDLY -> {}
    }
  }
}
