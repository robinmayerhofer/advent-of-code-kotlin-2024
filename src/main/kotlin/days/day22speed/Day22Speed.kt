package days.day22speed

import utils.measure
import kotlin.io.path.Path
import kotlin.io.path.readLines

object Day22Speed {

    @JvmStatic
    private inline fun Long.next(): Long {
        val step1 = prune(mix(this, this * 64))
        val step2 = prune(mix(step1, step1 / 32))
        val step3 = prune(mix(step2, step2 * 2048))
        return step3
    }

    @JvmStatic
    private inline fun mix(a: Long, b: Long) = a xor b

    @JvmStatic
    private inline fun prune(a: Long) = a % 16777216

    @JvmStatic
    private val secretCount = 2000

    @JvmStatic
    fun part2(input: List<String>): Int {
        val numberss = Array(input.size) { IntArray(secretCount) }
        val diffs = Array(input.size) { IntArray(secretCount - 1) }

        for (i in input.indices) {
            val initial = input[i].toLong()
            var current = initial
            for (j in 0 until secretCount) {
                numberss[i][j] = (current % 10).toInt()
                current = current.next()
                if (j >= 1) {
                    diffs[i][j-1] = numberss[i][j] - numberss[i][j - 1]
                }
            }
        }

        val final = IntArray(19 * 19 * 19 * 19)
        val size = input.size
        var ans = 0
        val factor = 19 * 19 * 19

        for (i in input.indices) {
            val cur = IntArray(19 * 19 * 19 * 19)
            for (j in 1995 downTo 0) {
                val diff1 = diffs[i][j] + 9
                val diff2 = diffs[i][j + 1] + 9
                val diff3 = diffs[i][j + 2] + 9
                val diff4 = diffs[i][j + 3] + 9
                val index = diff1 * factor + diff2 * 19 * 19 + diff3 * 19 + diff4
                cur[index] = (numberss[i][j + 4] % 10).toInt()
            }
            for (index in cur.indices) {
                final[index] += cur[index]
                if (i == size - 1) {
                    ans = maxOf(ans, final[index])
                }
            }
        }

        return ans
    }

    @JvmStatic
    fun main(args: Array<String>) {
        measure {
            val input = Path(args[0]).readLines()
            print(part2(input))
        }
    }
}
