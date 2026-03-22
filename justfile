default:
    @just --list

jmh:
  java -jar build/libs/rosemary-chess-jmh.jar \
    -rf csv -rff docs/results.csv -prof "async:output=jfr;dir=/tmp/jfr;event=cpu"
