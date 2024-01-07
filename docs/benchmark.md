# Benchmarks

Benchmarks can be ran with `./gradlew jmh`


# Result of 07.01.2024 - Using arrays instead of HashSets
```
Benchmark                          Mode  Cnt   Score   Error  Units
PerftBenchmark.position_2kr3r     thrpt    5  10.773 ± 0.535  ops/s
PerftBenchmark.position_r1b1k1r1  thrpt    5   3.971 ± 0.074  ops/s
PerftBenchmark.position_r1bQk2r   thrpt    5  88.351 ± 2.123  ops/s
```
