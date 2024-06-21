package rosemary;

import org.openjdk.jmh.annotations.*;
import rosemary.board.*;

@Fork(1)
@OutputTimeUnit(java.util.concurrent.TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 250, timeUnit = java.util.concurrent.TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 500, timeUnit = java.util.concurrent.TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class PerftBenchmark {

    @Benchmark
    public void position_pawns() {
        PerftRunner.perft(7, false, new BoardState("8/5kpp/4p3/5P2/5p2/4P3/5KPP/8 w - - 0 1")); // 4 pawns each side
    }

    @Benchmark
    public void position_rooks() {
        PerftRunner.perft(5, false, new BoardState("1k6/2r5/R7/4r3/8/6R1/1K6/8 w - - 0 1")); // 2 rooks each side
    }

    @Benchmark
    public void position_bishops() {
        PerftRunner.perft(6, false, new BoardState("k7/8/5b2/7B/8/4B3/1b6/7K w - - 0 1")); // 2 bishops each side
    }

    @Benchmark
    public void position_queens() {
        PerftRunner.perft(5, false, new BoardState("k7/4Q3/5q2/8/2q5/4Q3/8/7K w - - 0 1")); // 2 queens each side
    }

    @Benchmark
    public void position_knights() {
        PerftRunner.perft(6, false, new BoardState("6k1/5n2/6N1/1n4n1/3N4/8/2N5/1K6 w - - 0 1")); // 3 knights each side
    }

    @Benchmark
    public void position_normal() {
        PerftRunner.perft(
                5, false, new BoardState("r1bq1rk1/ppp2pp1/2n1pn1p/b2p4/3P1B2/2P1PN2/PP1NBPPP/R2Q1RK1 w - - 2 9"));
    }
}
