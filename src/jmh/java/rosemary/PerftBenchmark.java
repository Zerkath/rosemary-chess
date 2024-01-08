package rosemary;

import org.openjdk.jmh.annotations.*;
import rosemary.board.*;
import rosemary.generation.*;

public class PerftBenchmark {

  @Benchmark
  @Fork(value = 1, warmups = 1)
  @OutputTimeUnit(java.util.concurrent.TimeUnit.MILLISECONDS)
  @BenchmarkMode(Mode.Throughput)
  @Warmup(iterations = 5, time = 1, timeUnit = java.util.concurrent.TimeUnit.SECONDS)
  @Measurement(iterations = 5, time = 2, timeUnit = java.util.concurrent.TimeUnit.SECONDS)
  public void position_pawns() {
    UCI_Controller uci = new UCI_Controller();
    uci.runPerft(
        2, false, new BoardState("8/5kpp/4p3/5P2/5p2/4P3/5KPP/8 w - - 0 1")); // 4 pawns each side
  }

  @Benchmark
  @Fork(value = 1, warmups = 1)
  @OutputTimeUnit(java.util.concurrent.TimeUnit.MILLISECONDS)
  @BenchmarkMode(Mode.Throughput)
  @Warmup(iterations = 5, time = 1, timeUnit = java.util.concurrent.TimeUnit.SECONDS)
  @Measurement(iterations = 5, time = 2, timeUnit = java.util.concurrent.TimeUnit.SECONDS)
  public void position_rooks() {
    UCI_Controller uci = new UCI_Controller();
    uci.runPerft(
        2, false, new BoardState("1k6/2r5/R5R1/4r3/8/6R1/1K6/2r5 w - - 0 1")); // 3 rooks each side
  }

  @Benchmark
  @Fork(value = 1, warmups = 1)
  @OutputTimeUnit(java.util.concurrent.TimeUnit.MILLISECONDS)
  @BenchmarkMode(Mode.Throughput)
  @Warmup(iterations = 5, time = 1, timeUnit = java.util.concurrent.TimeUnit.SECONDS)
  @Measurement(iterations = 5, time = 2, timeUnit = java.util.concurrent.TimeUnit.SECONDS)
  public void position_bishops() {
    UCI_Controller uci = new UCI_Controller();
    uci.runPerft(
        2, false, new BoardState("k7/4B3/5b2/7B/2b5/4B3/1b6/7K w - - 0 1")); // 3 bishops each side
  }

  @Benchmark
  @Fork(value = 1, warmups = 1)
  @OutputTimeUnit(java.util.concurrent.TimeUnit.MILLISECONDS)
  @BenchmarkMode(Mode.Throughput)
  @Warmup(iterations = 5, time = 1, timeUnit = java.util.concurrent.TimeUnit.SECONDS)
  @Measurement(iterations = 5, time = 2, timeUnit = java.util.concurrent.TimeUnit.SECONDS)
  public void position_queens() {
    UCI_Controller uci = new UCI_Controller();
    uci.runPerft(
        2, false, new BoardState("k7/4Q3/5q2/7Q/2q5/4Q3/1q6/7K w - - 0 1")); // 3 queens each side
  }

  @Benchmark
  @Fork(value = 1, warmups = 1)
  @OutputTimeUnit(java.util.concurrent.TimeUnit.MILLISECONDS)
  @BenchmarkMode(Mode.Throughput)
  @Warmup(iterations = 5, time = 1, timeUnit = java.util.concurrent.TimeUnit.SECONDS)
  @Measurement(iterations = 5, time = 2, timeUnit = java.util.concurrent.TimeUnit.SECONDS)
  public void position_knights() {
    UCI_Controller uci = new UCI_Controller();
    uci.runPerft(
        2,
        false,
        new BoardState("6k1/5n2/6N1/1n4n1/3N4/8/2N5/1K6 w - - 0 1")); // 3 knights each side
  }

  @Benchmark
  @Fork(value = 1, warmups = 1)
  @OutputTimeUnit(java.util.concurrent.TimeUnit.MILLISECONDS)
  @BenchmarkMode(Mode.Throughput)
  @Warmup(iterations = 5, time = 1, timeUnit = java.util.concurrent.TimeUnit.SECONDS)
  @Measurement(iterations = 5, time = 2, timeUnit = java.util.concurrent.TimeUnit.SECONDS)
  public void position_normal() {
    UCI_Controller uci = new UCI_Controller();
    uci.runPerft(
        2,
        false,
        new BoardState("r1bq1rk1/ppp2pp1/2n1pn1p/b2p4/3P1B2/2P1PN2/PP1NBPPP/R2Q1RK1 w - - 2 9"));
  }
}
