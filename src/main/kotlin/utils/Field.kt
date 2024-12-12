package utils

import utils.Direction.DOWN
import utils.Direction.LEFT
import utils.Direction.RIGHT
import utils.Direction.UP

typealias Field = Array<CharArray>

fun inputToField(input: List<String>): Field {
    val rows = input.size
    val columns = input.firstOrNull()?.length ?: 0

    val field = Array(rows) { CharArray(columns) }

    input.forEachIndexed { row, line ->
        line.forEachIndexed { column, character ->
            field[row][column] = character
        }
    }
    return field
}

fun Field.isValidPosition(position: Position): Boolean =
    if (position.column < 0 || position.row < 0) {
        false
    } else if (position.column >= this[0].size || position.row >= this.size) {
        false
    } else {
        true
    }


operator fun Field.get(position: Position) =
    this[position.row][position.column]

fun Field.getSafe(position: Position) =
    if (isValidPosition(position)) {
        this[position.row][position.column]
    } else {
        null
    }


fun Field.get(row: Int, column: Int) =
    this[row][column]

fun Field.find(matches: (Char) -> Boolean): Position {
    for (r in indices) {
        for (c in this[r].indices) {
            if (matches(get(row = r, column = c))) {
                return Position(column = c, row = r)
            }
        }
    }
    error("Not found.")
}

fun Field.findAll(matches: (Char) -> Boolean): List<Position> {
    val all = mutableListOf<Position>()
    for (r in indices) {
        for (c in this[r].indices) {
            if (matches(get(row = r, column = c))) {
                all.add(Position(column = c, row = r))
            }
        }
    }
   return all
}

operator fun Field.set(position: Position, newValue: Char) {
    this[position.row][position.column] = newValue
}

fun Field.set(row: Int, column: Int, newValue: Char) {
    this[row][column] = newValue
}

/**
 * 1 2 3    1 4 7
 * 4 5 6 => 2 5 8
 * 7 8 9    3 6 9
 */
fun transpose(xs: Field): Field {
    val cols = xs[0].size
    val rows = xs.size
    return Array(cols) { j ->
        CharArray(rows) { i ->
            xs[i][j]
        }
    }
}

/**
 * 1 2 3    3 6 9
 * 4 5 6 => 2 5 8
 * 7 8 9    1 4 7
 */
fun rotateMinus90(field: Field): Field {
    val ys = field.size
    val xs = field[0].size

    return Array(xs) { x ->
        CharArray(ys) { y ->
            field[y][xs - x - 1]
        }
    }
}

/**
 * 1 2 3    7 4 1
 * 4 5 6 => 8 5 2
 * 7 8 9    9 6 3
 */
fun rotate90(field: Field): Field {
    val ys = field.size
    val xs = field[0].size

    return Array(xs) { x ->
        CharArray(ys) { y ->
            field[ys - y - 1][x]
        }
    }
}

fun Field.deepCopy(): Field =
    Field(size) { r ->
        CharArray(this[0].size) { c ->
            this[r][c]
        }
    }


fun Field.println() {
    println(this.joinToString("\n") { it.joinToString("") })
}

fun Field.floodFill(position: Position, found: MutableSet<Position>, char: Char): Set<Position> {
    if (!isValidPosition(position)) {
        return found
    }
    if (position in found) {
        return found
    }
    if (get(position) != char) {
        return found
    }

    found.add(position)

    floodFill(position.travel(UP), found, char)
    floodFill(position.travel(DOWN), found, char)
    floodFill(position.travel(LEFT), found, char)
    floodFill(position.travel(RIGHT), found, char)

    return found
}

fun Field.floodFill(position: Position) = floodFill(position, mutableSetOf(), get(position))
