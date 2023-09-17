# Rosemary Chess Engine
Started out as a project for Artificial Intelligence and Robotics course work in Karelia University of Applied Sciences. During the course most of the programming was done as pair programming, commits do not reflect the contribution of participants.

## Technology
- Java version 17+ (originally 11)
- Junit
- Gradle

To work with GUI's the jar needed to be turned into a exe, this was done using http://launch4j.sourceforge.net/


## Conventional commits

New commits after 30/05/2022 are going to be done with conventional commits formatting

## How to get started

### Installation
Only a installation of jdk 17+ needed.

You can install a version of java on linux / macos via sdkman https://sdkman.io/

### Running

The project already has a gradle wrapper installed, only requires that you have a valid installation of java

```
./gradlew build
```

### Testing

```
# run test with
./gradlew test

# Jacoco test results in
/build/jacocoHtml/index.html
```

## Other info
The program communicates with chess GUI's using UCI protocol.

http://wbec-ridderkerk.nl/html/UCIProtocol.html

## Misc sources
Bible of making a chess engine: https://www.chessprogramming.org/Main_Page
