package com.github.zerkath.rosemary;

import com.github.zerkath.rosemary.BoardRepresentation.*;
import com.github.zerkath.rosemary.Main.UCI_Controller;
import com.github.zerkath.rosemary.MoveGeneration.*;
import org.openjdk.jmh.annotations.*;

public class PerftBenchmark {

  @Benchmark
  @Fork(value = 1, warmups = 1)
  @OutputTimeUnit(java.util.concurrent.TimeUnit.SECONDS)
  @BenchmarkMode(Mode.Throughput)
  @Warmup(iterations = 5, time = 500, timeUnit = java.util.concurrent.TimeUnit.MILLISECONDS)
  @Measurement(iterations = 5, time = 500, timeUnit = java.util.concurrent.TimeUnit.MILLISECONDS)
  public void position_r1b1k1r1() {
    UCI_Controller uci = new UCI_Controller();
    uci.runPerft(
        4, false, new BoardState("r1b1k1r1/1p3p2/p4b1p/2N5/5q2/8/4PPBP/1RR3K1 b q - 1 27"));
  }

  @Benchmark
  @Fork(value = 1, warmups = 1)
  @OutputTimeUnit(java.util.concurrent.TimeUnit.SECONDS)
  @BenchmarkMode(Mode.Throughput)
  @Warmup(iterations = 5, time = 500, timeUnit = java.util.concurrent.TimeUnit.MILLISECONDS)
  @Measurement(iterations = 5, time = 500, timeUnit = java.util.concurrent.TimeUnit.MILLISECONDS)
  public void position_r1bQk2r() {
    UCI_Controller uci = new UCI_Controller();
    uci.runPerft(
        4, false, new BoardState("r1bQk2r/ppp2ppp/2p5/2b5/4n3/2N4P/PPP2PP1/R1B1KB1R b KQkq - 0 8"));
  }

  @Benchmark
  @Fork(value = 1, warmups = 1)
  @OutputTimeUnit(java.util.concurrent.TimeUnit.SECONDS)
  @BenchmarkMode(Mode.Throughput)
  @Warmup(iterations = 5, time = 500, timeUnit = java.util.concurrent.TimeUnit.MILLISECONDS)
  @Measurement(iterations = 5, time = 500, timeUnit = java.util.concurrent.TimeUnit.MILLISECONDS)
  public void position_2kr3r() {
    UCI_Controller uci = new UCI_Controller();
    uci.runPerft(
        4,
        false,
        new BoardState("2kr3r/2p4p/2nq1p1n/1N1p2p1/1P1P4/P3PN1P/5PP1/R2Q1RK1 b - - 0 20"));
  }
}
