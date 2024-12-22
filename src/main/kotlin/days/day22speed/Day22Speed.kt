package days.day22speed

import kotlin.io.path.Path
import kotlin.io.path.readLines

object Day22Speed {

    private fun Long.next(): Long {
        val step1 = prune(mix(this, this * 64))
        val step2 = prune(mix(step1, step1 / 32))
        val step3 = prune(mix(step2, step2 * 2048))
        return step3
    }

    private fun mix(a: Long, b: Long) = a xor b

    private fun prune(a: Long) = a % 16777216

    @JvmStatic
    fun part2(input: List<String>): Int {
        val numberss = Array(input.size) { IntArray(2000) }
        val diffs = Array(input.size) { IntArray(1999) }

        for (i in input.indices) {
            val initial = input[i].toLong()
            var current = initial
            for (j in 0 until 2000) {
                numberss[i][j] = (current % 10).toInt()
                current = current.next()
                if (j >= 1) {
                    diffs[i][j - 1] = numberss[i][j] - numberss[i][j - 1]
                }
            }
        }

        val final = IntArray(130321)
        val size = input.size
        var ans = 0
        val factor = 6859

        val cur = IntArray(130321)
        for (i in input.indices) {
            cur.fill(0)
            for (j in 1995 downTo 0) {
                val diff1 = diffs[i][j] + 9
                val diff2 = diffs[i][j + 1] + 9
                val diff3 = diffs[i][j + 2] + 9
                val diff4 = diffs[i][j + 3] + 9
                val index = diff1 * factor + diff2 * 361 + diff3 * 19 + diff4
                cur[index] = numberss[i][j + 4]
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
        val input = Path(args[0]).readLines()
        print(part2(input))
    }
}
