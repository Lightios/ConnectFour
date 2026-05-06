package game

import model.*

object GameEngine {
    fun createBoard(config: GameConfig): List<List<Cell>> =
        List(config.rows) { List(config.cols) { Cell.EMPTY } }

    fun dropPiece(state: GameState, col: Int): GameState {
        if (state.status != GameStatus.Playing) return state
        if (col < 0 || col >= state.config.cols) return state

        val row = findDropRow(state.board, col) ?: return state  // column full

        val newBoard = state.board.mapIndexed { r, rowList ->
            rowList.mapIndexed { c, cell ->
                if (r == row && c == col) state.currentPlayer.toCell() else cell
            }
        }

        val status = resolveStatus(newBoard, row, col, state.currentPlayer, state.config)

        return state.copy(
            board = newBoard,
            currentPlayer = if (status == GameStatus.Playing) state.currentPlayer.other() else state.currentPlayer,
            status = status,
            lastMove = row to col
        )
    }

    fun initialState(config: GameConfig): GameState = GameState(
        board = createBoard(config),
        currentPlayer = Player.RED,
        status = GameStatus.Playing,
        config = config
    )

    private fun findDropRow(board: List<List<Cell>>, col: Int): Int? {
        // scan from bottom up, return first empty row
        for (row in board.indices.reversed()) {
            if (board[row][col] == Cell.EMPTY) return row
        }
        return null  // column is full
    }

    private fun resolveStatus(
        board: List<List<Cell>>,
        row: Int,
        col: Int,
        player: Player,
        config: GameConfig
    ): GameStatus {
        val winningCells = findWinningCells(board, row, col, player, config.winCondition)
        if (winningCells != null) return GameStatus.Won(player, winningCells)
        if (isDraw(board)) return GameStatus.Draw
        return GameStatus.Playing
    }

    private fun findWinningCells(
        board: List<List<Cell>>,
        row: Int,
        col: Int,
        player: Player,
        n: Int
    ): Set<Pair<Int, Int>>? {
        val directions = listOf(
            0 to 1,   // horizontal
            1 to 0,   // vertical
            1 to 1,   // diagonal ↘
            1 to -1   // diagonal ↙
        )
        val cell = player.toCell()

        for ((dr, dc) in directions) {
            val line = collectLine(board, row, col, dr, dc, cell)
            if (line.size >= n) return line.take(n).toSet()
        }
        return null
    }

    private fun collectLine(
        board: List<List<Cell>>,
        row: Int,
        col: Int,
        dr: Int,
        dc: Int,
        cell: Cell
    ): List<Pair<Int, Int>> {
        // collect matching cells in both directions along the axis, centered on (row, col)
        val forward = collectDirection(board, row, col, dr, dc, cell)
        val backward = collectDirection(board, row, col, -dr, -dc, cell)
        return backward.reversed() + listOf(row to col) + forward
    }

    private fun collectDirection(
        board: List<List<Cell>>,
        startRow: Int,
        startCol: Int,
        dr: Int,
        dc: Int,
        cell: Cell
    ): List<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()
        var r = startRow + dr
        var c = startCol + dc
        while (r in board.indices && c in board[0].indices && board[r][c] == cell) {
            result.add(r to c)
            r += dr
            c += dc
        }
        return result
    }

    private fun isDraw(board: List<List<Cell>>): Boolean =
        board.first().all { it != Cell.EMPTY }  // top row full = no more moves possible
}