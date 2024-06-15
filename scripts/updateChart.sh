#!/bin/sh

TARGET_BRANCH=main
STARTING_BRANCH=$(git branch --show-current)

git pull
./gradlew build jmhJar

java -jar -Xmx4G -Xms2G build/libs/rosemary-chess-jmh.jar .Perft -rf csv -rff new.csv
mv new.csv docs/data/new.csv

git checkout "$TARGET_BRANCH"
git pull
./gradlew build jmhJar
java -jar -Xmx4G -Xms2G build/libs/rosemary-chess-jmh.jar .Perft -rf csv -rff old.csv
mv old.csv docs/data/old.csv
git checkout "$STARTING_BRANCH"
cd docs
Rscript ./comparePerft.R
