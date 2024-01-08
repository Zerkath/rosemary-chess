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
PerftBenchmark.position_2kr3r     thrpt    5  15.107 ± 0.826  ops/s
PerftBenchmark.position_6bR       thrpt    5   7.169 ± 0.174  ops/s
PerftBenchmark.position_r1b1k1r1  thrpt    5   5.434 ± 0.478  ops/s
PerftBenchmark.position_r1bQk2r   thrpt    5  86.771 ± 4.516  ops/s
```
