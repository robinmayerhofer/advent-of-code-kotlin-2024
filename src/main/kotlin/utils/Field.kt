package utils

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
