package utils

data class Position(val column: Int, val row: Int) : Comparable<Position> {
    override fun compareTo(other: Position): Int =
        compareValuesBy(
            this, other,
            { it.column },
            { it.row },
        )
}

enum class Direction(val deltaX: Int, val deltaY: Int) {
    NORTH(deltaX = 0, deltaY = -1),
    SOUTH(deltaX = 0, deltaY = 1),
    EAST(deltaX = 1, deltaY = 0),
    WEST(deltaX = -1, deltaY = 0),
    DIAGONAL1FORWARD(deltaX = 1, deltaY = 1),
    DIAGONAL1BACKWARD(deltaX = -1, deltaY = -1),
    DIAGONAL2FORWARD(deltaX = -1, deltaY = 1),
    DIAGONAL2BACKWARD(deltaX = 1, deltaY = -1),
    ;

    fun reverse() = when (this) {
        NORTH -> SOUTH
        SOUTH -> NORTH
        EAST -> WEST
        WEST -> EAST
        else -> TODO()
    }

    fun turnRight() = when (this) {
        NORTH -> EAST
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
        else -> TODO()
    }
}

fun Position.travel(direction: Direction, steps: Int = 1): Position =
    copy(column = column + direction.deltaX * steps, row = row + direction.deltaY * steps)
