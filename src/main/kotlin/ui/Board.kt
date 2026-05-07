package ui

import androidx.compose.runtime.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.css.*
import model.*

@Composable
fun Board(
    state: GameState,
    onColumnClick: (Int) -> Unit
) {
    val winningCells = (state.status as? GameStatus.Won)?.winningCells ?: emptySet()
    val isPlaying = state.status == GameStatus.Playing

    val gridCols = "repeat(${state.config.cols}, minmax(0, 1fr))"

    Div({ classes(AppStyleSheet.boardWrapper) }) {
        Div({
            classes(AppStyleSheet.columnHints)
            style {
                gridTemplateColumns(gridCols)
            }
        }) {
            for (col in 0 until state.config.cols) {
                val isColumnFull = state.board[0][col] != Cell.EMPTY

                Button({
                    classes(AppStyleSheet.columnHint)
                    if (!isPlaying || isColumnFull) {
                        attr("disabled", "true")
                        style { property("cursor", "not-allowed") }
                    }
                    onClick { if (isPlaying && !isColumnFull) onColumnClick(col) }
                }) {
                    if (isPlaying && !isColumnFull) {
                        Text("▼")
                    }
                }
            }
        }

        Div({
            classes(AppStyleSheet.board)
            style { property("grid-template-columns", gridCols) }
        }) {
            for (row in 0 until state.config.rows) {
                for (col in 0 until state.config.cols) {
                    val cell = state.board[row][col]
                    val isWinning = (row to col) in winningCells
                    val isLastMove = state.lastMove == (row to col)

                    GameCell(
                        cell = cell,
                        isWinning = isWinning,
                        animate = isLastMove && cell != Cell.EMPTY
                    )
                }
            }
        }
    }
}