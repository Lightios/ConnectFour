package storage

import kotlinx.browser.localStorage
import model.*

class GameStorageImpl : GameStorage {

    private val key = "connect_four_state"

    override fun save(state: GameState) {
        // only save mid-game states — no point saving a finished game
        if (state.status != GameStatus.Playing) {
            clear()
            return
        }

        val boardStr = state.board.joinToString("|") { row ->
            row.joinToString(",") { cell ->
                when (cell) {
                    Cell.EMPTY -> "0"
                    Cell.RED -> "1"
                    Cell.YELLOW -> "2"
                }
            }
        }

        val currentPlayer = when (state.currentPlayer) {
            Player.RED -> "R"
            Player.YELLOW -> "Y"
        }

        val config = "${state.config.rows};${state.config.cols};${state.config.winCondition}"
        val lastMove = state.lastMove?.let { "${it.first};${it.second}" } ?: "null"

        localStorage.setItem(key, "$config|$currentPlayer|$lastMove|$boardStr")
    }

    override fun load(): GameState? {
        val raw = localStorage.getItem(key) ?: return null
        return try {
            // format: "rows;cols;win|player|lastMoveRow;lastMoveCol|row0|row1|..."
            val parts = raw.split("|")
            if (parts.size < 4) return null

            val configParts = parts[0].split(";")
            val config = GameConfig(
                rows = configParts[0].toInt(),
                cols = configParts[1].toInt(),
                winCondition = configParts[2].toInt()
            )

            val currentPlayer = when (parts[1]) {
                "R" -> Player.RED
                "Y" -> Player.YELLOW
                else -> return null
            }

            val lastMove = if (parts[2] == "null") null else {
                val moveParts = parts[2].split(";")
                moveParts[0].toInt() to moveParts[1].toInt()
            }

            val board = parts.drop(3).map { rowStr ->
                rowStr.split(",").map { cell ->
                    when (cell) {
                        "1" -> Cell.RED
                        "2" -> Cell.YELLOW
                        else -> Cell.EMPTY
                    }
                }
            }

            if (board.size != config.rows || board.any { it.size != config.cols }) return null

            GameState(
                board = board,
                currentPlayer = currentPlayer,
                status = GameStatus.Playing,
                config = config,
                lastMove = lastMove
            )
        } catch (e: Exception) {
            // corrupted storage — discard silently
            clear()
            null
        }
    }

    override fun clear() {
        localStorage.removeItem(key)
    }
}