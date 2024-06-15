package rosemary;

import java.util.*;
import org.openjdk.jmh.annotations.*;

@Fork(1)
@OutputTimeUnit(java.util.concurrent.TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 10, time = 250, timeUnit = java.util.concurrent.TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 1000, timeUnit = java.util.concurrent.TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class CollectionBenchmark {

    @Benchmark
    public void arrayListAppend() {
        final ArrayList<Long> result = new ArrayList<>(500);
        for (long i = 0; i < 500; i++) {
            result.add(i);
        }
    }

    @Benchmark
    public void linkedListAppend() {
        final LinkedList<Long> result = new LinkedList<>();
        for (long i = 0; i < 500; i++) {
            result.add(i);
        }
    }

    @Benchmark
    public void arrayListPrune() {
        final ArrayList<Long> result = new ArrayList<>(500);
        for (long i = 0; i < 500; i++) {
            result.add(i);
        }

        Iterator<Long> iter = result.iterator();
        while (iter.hasNext()) {
            long x = iter.next();
            if (x % 5 == 0) {
                iter.remove();
            }
        }
    }

    @Benchmark
    public void arrayListCopyPrune() {
        final ArrayList<Long> result = new ArrayList<>(500);
        for (long i = 0; i < 500; i++) {
            result.add(i);
        }

        Iterator<Long> iter = result.iterator();
        final ArrayList<Long> result_new = new ArrayList<>(500);
        while (iter.hasNext()) {
            long x = iter.next();
            if (x % 5 != 0) {
                result_new.add(x);
            }
        }
    }

    @Benchmark
    public void linkedListPrune() {
        final LinkedList<Long> result = new LinkedList<>();
        for (long i = 0; i < 500; i++) {
            result.add(i);
        }

        Iterator<Long> iter = result.iterator();
        while (iter.hasNext()) {
            long x = iter.next();
            if (x % 5 == 0) {
                iter.remove();
            }
        }
    }
}
