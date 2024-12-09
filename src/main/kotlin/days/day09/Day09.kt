package days.day09

import days.day09.DiskSlot.Empty
import days.day09.DiskSlot.File
import utils.findAllDigits
import utils.measure
import utils.println
import utils.readInput
import utils.shouldLog
import utils.testFile

sealed interface DiskSlot {
    data class Empty(val size: Int) : DiskSlot
    data class File(val fileId: Int, val size: Int) : DiskSlot
}

fun main() {
    shouldLog = false

    fun expand(list: List<Int>): List<Int?> {
        val expanded = mutableListOf<Int?>()

        var fileId = 0
        var isFree = false
        for (amount in list) {
            if (isFree) {
                expanded.addAll(List(amount) { null })
            } else {
                expanded.addAll(List(amount) { fileId })
                fileId += 1
            }
            isFree = !isFree
        }

        return expanded.toList()
    }

    fun expand(l: List<DiskSlot>): List<Int?> =
        l.map { e ->
            when (e) {
                is Empty -> List(size = e.size) { null }
                is File -> List(size = e.size) { e.fileId }
            }
        }.flatten()

    fun List<Int?>.debugPrint() = joinToString(separator = "") { if (it == null) "." else "$it" }.println()

    fun List<DiskSlot>.debugPrint() =
        expand(this).debugPrint()

    fun fillUpEmptySpace(expanded: List<Int?>): List<Int> {
        val l = expanded.toMutableList()
        var indexBack = expanded.size - 1

        if (shouldLog) l.debugPrint()

        l.indices
            .filter { l[it] == null }
            .forEach { index ->
                while (expanded[indexBack] == null) {
                    indexBack -= 1
                }
                if (shouldLog) l.debugPrint()
                if (indexBack < index) return@forEach
                l[index] = expanded[indexBack]
                l[indexBack] = null
                indexBack -= 1
            }

        return l.filterNotNull()
    }

    fun part1(input: List<String>): Long {
        val ns = input[0].findAllDigits()
        val expanded = expand(ns)
        if (shouldLog) {
            println("Expanded $expanded")
        }
        val moved = fillUpEmptySpace(expanded)
        if (shouldLog) {
            println(" Moved $moved")
        }

        return moved.mapIndexed { index, n -> (index * n).toLong() }.sum()
    }

    fun move(compact: List<DiskSlot>): List<DiskSlot> {
        val l = compact.toMutableList()

        if (shouldLog) l.debugPrint()

        for (index in l.indices) {
            if (l[index] !is Empty) {
                continue
            }

            val emptySize = (l[index] as Empty).size
            var indexBack = compact.size - 1
            while (true) {
                while (l[indexBack] is Empty) {
                    indexBack -= 1
                }
                if (indexBack < index) {
                    break
                }
                val size = (l[indexBack] as File).size
                if (size > emptySize) {
                    if (shouldLog) println("A")
                    indexBack -= 1
                    continue
                } else if (size == emptySize) {
                    l[index] = l[indexBack]
                    l[indexBack] = Empty(size)
                    if (shouldLog) l.debugPrint()
                    break
                } else {
                    // size < emptySize
                    l[index] = l[indexBack]
                    l.add(index + 1, Empty(emptySize - size))
                    indexBack += 1
                    l[indexBack] = Empty(size)
                    indexBack -= 1
                    if (shouldLog) l.debugPrint()
                    break
                }

            }
            if (shouldLog) l.debugPrint()

        }

        return l
    }

    fun part2(input: List<String>): Long {
        val ns = input[0].findAllDigits()
        val compact: List<DiskSlot> = ns.mapIndexed { index, digit ->
            if (index % 2 == 0) {
                File(fileId = index / 2, size = digit)
            } else {
                Empty(size = digit)
            }
        }
        val moved = move(compact)
        val expanded = expand(moved)
        return expanded
            .mapIndexedNotNull { index, n ->
                if (n == null) { null }
                else { (index * n).toLong() }
            }
            .sum()
    }


    testFile(
        "Part 1 Test 1",
        "Day09_test",
        {
            part1(it)
        },
        1928,
        filterBlank = false,
    )
    shouldLog = false
    measure {
        val input = readInput("Day09").filter(String::isNotBlank)
        part1(input)
    }
        .println()

    shouldLog = false
    testFile(
        "Part 2 Test 1",
        "Day09_test",
        ::part2,
        2858,
        filterBlank = false,
    )
    shouldLog = false
    measure {
        val input2 = readInput("Day09").filter(String::isNotBlank)
        part2(input2)
    }
        .println()
}
