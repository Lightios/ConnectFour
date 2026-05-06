package model

enum class Cell {
    EMPTY, RED, YELLOW;
}

fun Player.toCell(): Cell = when (this) {
    Player.RED -> Cell.RED
    Player.YELLOW -> Cell.YELLOW
}