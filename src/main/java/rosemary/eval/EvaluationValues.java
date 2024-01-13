package rosemary.eval;

public class EvaluationValues {
    public static final int ePawn = 100; // material values
    public static final int eKnight = 300;
    public static final int eBishop = 310;
    public static final int eRook = 500;
    public static final int eQueen = 900;

    public static final int mateOffset = 50;
    public static final int mate = 2000000000 + mateOffset;

    public static final int mateForBlack = -mate + mateOffset;
    public static final int mateForWhite = mate - mateOffset;
}
