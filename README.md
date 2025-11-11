# Rosemary Chess Engine
Started out as a project for Artificial Intelligence and Robotics course work in Karelia University of Applied Sciences. During the course most of the programming was done as pair programming, commits do not reflect the contribution of participants.

To work with GUI's the jar needed to be turned into a exe, this was done using http://launch4j.sourceforge.net/

## Conventional commits

New commits after 30/05/2022 are going to be done with conventional commits formatting

## How to get started

You can install a version of java on linux / macos via sdkman https://sdkman.io/

Tested with JDK 25. Using gradle wrapper e.g. `./gradlew build`

### Benchmarking

Manually running

```
./gradlew jmhJar
java -jar -Xmx4G -Xms2G build/libs/rosemary-chess-jmh.jar .Perft
```

Updating the file [analysis.png](./docs/analysis.jpg) with new chart data, note runs currently checked out branch against main.
```
./scripts/updateChart.sh
```

## Other info
The program communicates with chess GUI's using UCI protocol.

http://wbec-ridderkerk.nl/html/UCIProtocol.html

## Misc sources
Bible of making a chess engine: https://www.chessprogramming.org/Main_Page
