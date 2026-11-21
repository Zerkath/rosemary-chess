default:
    @just --list

# Print JVM and object shapes with JOL (edit scripts/shape.sc to inspect more objects)
shape:
  ./gradlew -q publishToMavenLocal
  scala-cli run scripts/shape.worksheet.sc --server=false --ttl 0s \
    --java-opt -Djdk.attach.allowAttachSelf=true \
    --java-opt -XX:+EnableDynamicAgentLoading

jmh:
  java -jar build/libs/rosemary-chess-jmh.jar \
    -rf csv -rff docs/results.csv -prof "async:output=jfr;dir=/tmp/jfr;event=cpu"
