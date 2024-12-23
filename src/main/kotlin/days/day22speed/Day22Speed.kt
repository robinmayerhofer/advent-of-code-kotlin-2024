package days.day22speed

import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.max

object Day22Speed {

    @JvmStatic
    private fun Long.next(): Long {
        val step1 = prune(mix(this, this shl 6))
        val step2 = prune(mix(step1, step1 shr 5))
        val step3 = prune(mix(step2, step2 shl 11))
        return step3
    }

    @JvmStatic
    private fun mix(a: Long, b: Long) = a xor b

    @JvmStatic
    private fun prune(a: Long) = a and 0xFFFFFF

    @JvmStatic
    private fun part2(input: List<String>): Int {
        val numberss = IntArray(2001)
        val diffs = IntArray(2000)

        val final = IntArray(130321)
        var ans = Int.MIN_VALUE

        val indices = IntArray(1997)
        val cur = IntArray(130321)

        for (i in input.indices) {
            val initial = input[i].toLong()
            var current = initial
            for (j in 0 until 2001) {
                numberss[j] = (current % 10).toInt()
                current = current.next()
                if (j >= 1) {
                    diffs[j - 1] = numberss[j] - numberss[j - 1]
                }
            }

            for (j in 1996 downTo 0) {
                val diff1 = diffs[j] + 9
                val diff2 = diffs[j + 1] + 9
                val diff3 = diffs[j + 2] + 9
                val diff4 = diffs[j + 3] + 9
                val index = diff1 * 6859 + diff2 * 361 + diff3 * 19 + diff4
                cur[index] = numberss[j + 4]
                indices[j] = index
            }

            for (index in indices) {
                final[index] += cur[index]
                cur[index] = 0
                ans = max(ans, final[index])
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
