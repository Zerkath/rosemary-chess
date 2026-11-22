default:
    @just --list

# Scratch directory for benchmark CSVs and throwaway worktrees
bench_dir := "/tmp/rosemary-bench"

# Print JVM and object shapes with JOL (edit scripts/shape.sc to inspect more objects)
shape:
  ./gradlew -q publishToMavenLocal
  scala-cli run scripts/shape.worksheet.sc --server=false --ttl 0s \
    --java-opt -Djdk.attach.allowAttachSelf=true \
    --java-opt -XX:+EnableDynamicAgentLoading

bencher:
  bencher run \
    --project rosemary-chess-b7bd4eb-47ri4zy3q0zn3 \
    --file /tmp/rosemary-bench/result.json "java -jar build/libs/rosemary-chess-jmh.jar -rf json -rff /tmp/rosemary-bench/result.json"

# Usage: just summarize [profile] (defaults to the last `just jmh` profile)
summarize profile="/tmp/rosemary-profile.pb.gz":
  #!/usr/bin/env bash
  set -euo pipefail

  echo "==> Hottest methods ({{profile}})"
  trace_processor --quiet --no-progress query --format csv \
    {{quote(profile)}} -f scripts/hottestMethods.sql \
    | bat --plain --language csv

jmh:
  #!/usr/bin/env bash
  set -euo pipefail

  jfr_dir=/tmp/jfr
  profile=/tmp/rosemary-profile.pb.gz

  # Avoid aggregating profiles left over from earlier runs.
  find "$jfr_dir" -mindepth 1 -delete 2>/dev/null || true

  java -jar build/libs/rosemary-chess-jmh.jar \
    -rf csv -rff /tmp/rosemary-results.csv \
    -prof "async:output=jfr;dir=$jfr_dir;event=cpu"

  # Aggregate every benchmark/fork profile into one pprof trace.
  jfrconv -o pprof "$jfr_dir"/*/jfr-cpu.jfr "$profile"

  # Report on the profile; re-run without benchmarking via `just summarize [profile]`.
  just summarize "$profile"

# Run the perft benchmark for a git ref in an isolated worktree, writing $bench_dir/<name>.csv
bench ref name:
    #!/usr/bin/env bash
    set -euo pipefail

    ref={{quote(ref)}}
    name={{quote(name)}}
    bench_dir={{quote(bench_dir)}}

    csv="$bench_dir/$name.csv"
    slug=$(printf '%s' "$ref" | sed 's#[/: ]#-#g; s#[^A-Za-z0-9._-]##g')
    worktree="$bench_dir/worktrees/$slug-$$"

    mkdir -p "$bench_dir/worktrees"
    trap 'git worktree remove --force "$worktree" >/dev/null 2>&1 || true' EXIT

    echo "==> Benchmarking $ref in $worktree"
    git worktree add --detach "$worktree" "$ref"
    (cd "$worktree" && ./gradlew --quiet jmhJar)
    java -jar -Xmx4G -Xms2G "$worktree/build/libs/rosemary-chess-jmh.jar" \
        .Perft -rf csv -rff "$csv"
    echo "==> Wrote $csv"

# Usage: just compare <base-ref> <head-ref> [output-name]
compare base head output="comparison": (bench base "base") (bench head "head")
    uv run scripts/comparePerft.py \
        --output {{quote(output + ".png")}} \
        --label {{quote("base=" + base)}} \
        --label {{quote("head=" + head)}} \
        {{quote(bench_dir + "/base.csv")}} \
        {{quote(bench_dir + "/head.csv")}}

# Remove benchmark CSVs and any leftover worktrees
bench-clean:
    rm -rf {{quote(bench_dir)}}
