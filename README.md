# Rosemary Chess Engine

Started out as a project for Artificial Intelligence and Robotics course work in Karelia University of Applied Sciences.
During the course most of the programming was done as pair programming, commits do not reflect the contribution of participants.

To work with GUI's the jar needed to be turned into a exe, this was done using http://launch4j.sourceforge.net/

## Conventional commits

New commits after 30/05/2022 are going to be done with conventional commits formatting

## How to get started

You can install a version of java on linux / macos via sdkman https://sdkman.io/

Tested with JDK 25. Using gradle wrapper e.g. `./gradlew build`

### Benchmarking

Manually running

```bash
./gradlew jmhJar
java --enable-native-access=ALL-UNNAMED --sun-misc-unsafe-memory-access=allow \
  -jar -Xmx4G -Xms2G build/libs/rosemary-chess-jmh.jar .Perft
```

Compare two git refs without switching branches. Each ref is built and benchmarked in
an isolated git worktree, the raw JMH results are written to `/tmp/rosemary-bench`, and
the chart is rendered to `comparison.png`:

```bash
just compare main my-branch
```

The results can be re-plotted without re-running the benchmarks:

```bash
uv run scripts/comparePerft.py \
  --label base=main --label head=my-branch \
  /tmp/rosemary-bench/base.csv /tmp/rosemary-bench/head.csv
```

Remove the benchmark CSVs and any leftover worktrees with `just bench-clean`.

### Inspecting object layouts

Uses [Java Object Layout](https://github.com/openjdk/jol) via the [scripts/shape.sc](./scripts/shape.sc) Scala CLI script.

```bash
just shape
```

Edit `scripts/shape.sc` to add the classes or instances you want to inspect. Requires [Scala CLI](https://scala-cli.virtuslab.org/). The engine is resolved from the local Maven repository, so run `./gradlew publishToMavenLocal` first (`just shape` does this automatically).

## Other info
The program communicates with chess GUI's using UCI protocol.

http://wbec-ridderkerk.nl/html/UCIProtocol.html

## Misc sources
Bible of making a chess engine: https://www.chessprogramming.org/Main_Page
