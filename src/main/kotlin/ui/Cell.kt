package ui

import androidx.compose.runtime.*
import org.jetbrains.compose.web.dom.*
import model.Cell

@Composable
fun GameCell(
    cell: Cell,
    isWinning: Boolean,
    animate: Boolean
) {
    val classes = buildList {
        add(AppStyleSheet.cell)
        when (cell) {
            Cell.EMPTY -> add(AppStyleSheet.cellEmpty)
            Cell.RED -> add(AppStyleSheet.cellRed)
            Cell.YELLOW -> add(AppStyleSheet.cellYellow)
        }
        if (isWinning) add(AppStyleSheet.cellWinning)
        if (animate) add(AppStyleSheet.dropAnimation)
    }

    key(animate, cell) {
        Div({
            classes(*classes.toTypedArray())
        })
    }
}