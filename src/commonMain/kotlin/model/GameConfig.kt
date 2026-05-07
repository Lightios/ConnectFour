package model

data class GameConfig(
    val rows: Int = 10,
    val cols: Int = 10,
    val winCondition: Int = 4
)