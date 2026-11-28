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
@Warmup(iterations = 3, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 3, jvmArgsAppend = {"-Xms2G", "-Xmx4G"})
@State(Scope.Thread)
public class PerftBenchmark {

    private static final int NORMAL_DEPTH = 4; // ~1.4M nodes
    private static final int KIWIPETE_DEPTH = 4; // ~4M nodes
    private static final int STARTPOS_DEPTH = 5; // ~5M nodes

    private BoardState normal;
    private BoardState kiwipete;
    private BoardState startpos;

    @Setup(Level.Trial)
    public void setup() {
        normal = FenUtils.parseFen("r1bq1rk1/ppp2pp1/2n1pn1p/b2p4/3P1B2/2P1PN2/PP1NBPPP/R2Q1RK1 w - - 2 9");
        kiwipete = FenUtils.parseFen("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
        startpos = FenUtils.parseFen(FenUtils.default_fen);
    }

    @Benchmark
    public long position_normal() {
        return PerftRunner.perft(NORMAL_DEPTH, false, normal);
    }

    @Benchmark
    public long position_kiwipete() {
        return PerftRunner.perft(KIWIPETE_DEPTH, false, kiwipete);
    }

    @Benchmark
    public long position_startpos() {
        return PerftRunner.perft(STARTPOS_DEPTH, false, startpos);
    }
}
