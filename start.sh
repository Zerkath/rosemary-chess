#!/bin/sh

java \
  -Xms2G \
  -Xmx8G \
  -XX:G1HeapWastePercent=50 \
  -jar build/libs/rosemary-chess.jar
