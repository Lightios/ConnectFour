package model

data class GameState(
    val board: List<List<Cell>>,
    val currentPlayer: Player,
    val status: GameStatus,
    val config: GameConfig,
    val lastMove: Pair<Int, Int>? = null  // row to col of last dropped piece
)

sealed class GameStatus {
    object Playing : GameStatus()
    data class Won(val player: Player, val winningCells: Set<Pair<Int, Int>>) : GameStatus()
    object Draw : GameStatus()
}