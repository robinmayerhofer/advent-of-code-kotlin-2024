package days.day01

import org.openjdk.jmh.annotations.*
import utils.readInput
import java.util.concurrent.TimeUnit.MICROSECONDS
import java.util.concurrent.TimeUnit.SECONDS

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(MICROSECONDS)
@Warmup(iterations = 1, time = 5, timeUnit = SECONDS)
@Measurement(iterations = 1, time = 5, timeUnit = SECONDS, batchSize = 1)
@Fork(value = 1)
@State(Scope.Benchmark)
class Day01Benchmark {
    @Benchmark
    fun day1Part1() {
        part1(readInput("Day01"))
    }

    @Benchmark
    fun day1Part2() {
        part2(readInput("Day01"))
    }

}
