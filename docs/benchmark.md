# Benchmarks

Benchmarks can be ran with `./gradlew jmh`

# Result of 07.01.2024 - Using arrays instead of HashSets

## Before 
```
PerftBenchmark.position_2kr3r     thrpt    5  10.959 ± 0.117  ops/s
PerftBenchmark.position_6bR       thrpt    5   5.637 ± 0.151  ops/s
PerftBenchmark.position_r1b1k1r1  thrpt    5   3.728 ± 0.065  ops/s
PerftBenchmark.position_r1bQk2r   thrpt    5  69.888 ± 4.363  ops/s
```

## After
```
Benchmark                          Mode  Cnt   Score   Error  Units
PerftBenchmark.position_2kr3r     thrpt    5  10.853 ± 0.335  ops/s
PerftBenchmark.position_6bR       thrpt    5   5.647 ± 0.079  ops/s
PerftBenchmark.position_r1b1k1r1  thrpt    5   3.990 ± 0.067  ops/s
PerftBenchmark.position_r1bQk2r   thrpt    5  88.009 ± 4.822  ops/s
```


# Potential improvement with changes to boardstate

Notably running perft 6 took 8500ms~ from 11000ms

```
PerftBenchmark.position_2kr3r     thrpt    5  14.349 ± 0.667  ops/s
PerftBenchmark.position_6bR       thrpt    5   6.793 ± 0.272  ops/s
PerftBenchmark.position_r1b1k1r1  thrpt    5   5.119 ± 0.036  ops/s
PerftBenchmark.position_r1bQk2r   thrpt    5  88.411 ± 2.494  ops/s
```

# New suite of benchmarks, changed to non-concurrent perft

```
PerftBenchmark.position_bishops  thrpt   10    7.677 ± 0.122  ops/s
PerftBenchmark.position_knights  thrpt   10   24.895 ± 0.117  ops/s
PerftBenchmark.position_normal   thrpt   10    4.796 ± 0.074  ops/s
PerftBenchmark.position_pawns    thrpt   10  127.526 ± 0.271  ops/s
PerftBenchmark.position_queens   thrpt   10    0.543 ± 0.002  ops/s
PerftBenchmark.position_rooks    thrpt   10    2.983 ± 0.107  ops/s
```
