package rosemary;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.*;
import rosemary.board.BoardState;
import rosemary.board.FenUtils;

/**
 * <p>Each invocation is tuned to finish in roughly 50-300ms.
 * Depths differ between positions to keep the invocation cost
 * comparable, so only compare a given benchmark against itself.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 3, jvmArgsAppend = {"-Xms2G", "-Xmx4G"})
@State(Scope.Thread)
public class PerftBenchmark {

    private static final int PAWNS_DEPTH = 5; // ~258k nodes
    private static final int ROOKS_DEPTH = 4; // ~617k nodes
    private static final int BISHOPS_DEPTH = 5; // ~2.2M nodes
    private static final int QUEENS_DEPTH = 4; // ~1.8M nodes
    private static final int KNIGHTS_DEPTH = 5; // ~3.0M nodes
    private static final int NORMAL_DEPTH = 4; // ~1.4M nodes

    private BoardState pawns;
    private BoardState rooks;
    private BoardState bishops;
    private BoardState queens;
    private BoardState knights;
    private BoardState normal;

    @Setup(Level.Trial)
    public void setup() {
        pawns = FenUtils.parseFen("8/5kpp/4p3/5P2/5p2/4P3/5KPP/8 w - - 0 1"); // 4 pawns each side
        rooks = FenUtils.parseFen("1k6/2r5/R7/4r3/8/6R1/1K6/8 w - - 0 1"); // 2 rooks each side
        bishops = FenUtils.parseFen("k7/8/5b2/7B/8/4B3/1b6/7K w - - 0 1"); // 2 bishops each side
        queens = FenUtils.parseFen("k7/4Q3/5q2/8/2q5/4Q3/8/7K w - - 0 1"); // 2 queens each side
        knights = FenUtils.parseFen("6k1/5n2/6N1/1n4n1/3N4/8/2N5/1K6 w - - 0 1"); // 3 knights each side
        normal = FenUtils.parseFen("r1bq1rk1/ppp2pp1/2n1pn1p/b2p4/3P1B2/2P1PN2/PP1NBPPP/R2Q1RK1 w - - 2 9");
    }

    @Benchmark
    public long position_pawns() {
        return PerftRunner.perft(PAWNS_DEPTH, false, pawns);
    }

    @Benchmark
    public long position_rooks() {
        return PerftRunner.perft(ROOKS_DEPTH, false, rooks);
    }

    @Benchmark
    public long position_bishops() {
        return PerftRunner.perft(BISHOPS_DEPTH, false, bishops);
    }

    @Benchmark
    public long position_queens() {
        return PerftRunner.perft(QUEENS_DEPTH, false, queens);
    }

    @Benchmark
    public long position_knights() {
        return PerftRunner.perft(KNIGHTS_DEPTH, false, knights);
    }

    @Benchmark
    public long position_normal() {
        return PerftRunner.perft(NORMAL_DEPTH, false, normal);
    }
}
