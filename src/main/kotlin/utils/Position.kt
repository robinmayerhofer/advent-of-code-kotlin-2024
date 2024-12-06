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
    UP(deltaX = 0, deltaY = -1),
    DOWN(deltaX = 0, deltaY = 1),
    RIGHT(deltaX = 1, deltaY = 0),
    LEFT(deltaX = -1, deltaY = 0),
    DOWN_RIGHT(deltaX = 1, deltaY = 1),
    UP_LEFT(deltaX = -1, deltaY = -1),
    DOWN_LEFT(deltaX = -1, deltaY = 1),
    UP_RIGHT(deltaX = 1, deltaY = -1),
    ;

    companion object {
        val NORTH = UP
        val SOUTH = DOWN
        val EAST = RIGHT
        val WEST = LEFT
        val DIAGONAL1FORWARD = DOWN_RIGHT
        val DIAGONAL1BACKWARD = UP_LEFT
        val DIAGONAL2FORWARD = DOWN_LEFT
        val DIAGONAL2BACKWARD = UP_RIGHT
    }

    fun reverse() = when (this) {
        UP -> DOWN
        DOWN -> UP
        RIGHT -> LEFT
        LEFT -> RIGHT

        DOWN_RIGHT -> UP_LEFT
        UP_LEFT -> DOWN_RIGHT

        DOWN_LEFT -> UP_RIGHT
        UP_RIGHT -> DOWN_LEFT
    }

    fun turnRight() = when (this) {
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP

        DOWN_RIGHT -> DOWN_LEFT
        DOWN_LEFT -> UP_LEFT
        UP_LEFT -> UP_RIGHT
        UP_RIGHT -> DOWN_RIGHT
    }

    fun turnLeft() = when (this) {
        UP -> LEFT
        LEFT -> DOWN
        DOWN -> RIGHT
        RIGHT -> UP

        DOWN_RIGHT -> UP_RIGHT
        UP_RIGHT -> UP_LEFT
        UP_LEFT -> DOWN_LEFT
        DOWN_LEFT -> DOWN_RIGHT
    }
}

fun Position.travel(direction: Direction, steps: Int = 1): Position =
    copy(column = column + direction.deltaX * steps, row = row + direction.deltaY * steps)
