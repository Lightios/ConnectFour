package model

data class GameConfig(
    val rows: Int = 6,
    val cols: Int = 7,
    val winCondition: Int = 4
)