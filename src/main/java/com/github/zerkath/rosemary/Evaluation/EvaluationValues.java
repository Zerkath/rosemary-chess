package com.github.zerkath.rosemary.Evaluation;

public class EvaluationValues {
  public final int ePawn = 100; // material values
  public final int eKnight = 300;
  public final int eBishop = 310;
  public final int eRook = 500;
  public final int eQueen = 900;

  public final int mateOffset = 50;
  public final int mate = 2000000000 + mateOffset;

  public final int mateForBlack = -mate + mateOffset;
  public final int mateForWhite = mate - mateOffset;
}
