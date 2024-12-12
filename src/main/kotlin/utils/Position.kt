package utils

import utils.Direction.DOWN
import utils.Direction.LEFT
import utils.Direction.RIGHT
import utils.Direction.UP

data class Position(val column: Int, val row: Int) : Comparable<Position> {
    override fun compareTo(other: Position): Int =
        compareValuesBy(
            this, other,
            { it.column },
            { it.row },
        )
}

fun Set<Position>.area() = size

fun areaOf(positions: Set<Position>) = positions.area()

fun Set<Position>.perimeter(): Int {
    var per = 0
    for (position in this) {
        if (position.travel(UP) !in this) per += 1
        if (position.travel(DOWN) !in this) per += 1
        if (position.travel(LEFT) !in this) per += 1
        if (position.travel(RIGHT) !in this) per += 1
    }
    return per
}

fun perimiterOf(positions: Set<Position>) = positions.perimeter()

fun Position.travelLeft() = travel(LEFT)
fun Position.travelRight() = travel(RIGHT)
fun Position.travelUp() = travel(UP)
fun Position.travelDown() = travel(DOWN)

fun Set<Position>.edges(): Int {
    val rMax = this.maxOf { it.row }
    val rMin = this.minOf { it.row }
    val cMax = this.maxOf { it.column }
    val cMin = this.minOf { it.column }

    var edgeCount = 0

    fun checkEdge(pos: Position, travelFunc: (Position) -> Position, onEdge: Boolean): Boolean {
        return if (travelFunc(pos) !in this) {
            if (!onEdge) {
                edgeCount += 1
            }
            true
        } else {
            false
        }
    }

    for (c in cMin..cMax) {
        var onEdgeLeft = false
        var onEdgeRight = false
        for (r in rMin..rMax) {
            val pos = Position(column = c, row = r)
            if (pos in this) {
                onEdgeLeft = checkEdge(pos, Position::travelLeft, onEdgeLeft)
                onEdgeRight = checkEdge(pos, Position::travelRight, onEdgeRight)
            } else {
                onEdgeLeft = false
                onEdgeRight = false
            }
        }
    }

    for (r in rMin..rMax) {
        var onEdgeUp = false
        var onEdgeDown = false
        for (c in cMin..cMax) {
            val pos = Position(column = c, row = r)
            if (pos in this) {
                onEdgeUp = checkEdge(pos, Position::travelUp, onEdgeUp)
                onEdgeDown = checkEdge(pos, Position::travelDown, onEdgeDown)
            } else {
                onEdgeUp = false
                onEdgeDown = false
            }
        }
    }

    return edgeCount
}

fun edgesOf(positions: Set<Position>) = positions.edges()

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
