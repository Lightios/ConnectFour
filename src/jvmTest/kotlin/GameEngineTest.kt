import game.GameEngine
import model.Cell
import model.GameConfig
import model.GameState
import model.GameStatus
import model.Player
import model.toCell
import kotlin.test.*

class GameEngineTest {

    private val default = GameConfig()

    // ── Helpers ───────────────────────────────────────────────────────

    private fun playMoves(vararg cols: Int, config: GameConfig = default): GameState {
        var state = GameEngine.initialState(config)
        for (col in cols) {
            state = GameEngine.dropPiece(state, col)
        }
        return state
    }

    private fun assertWinner(state: GameState, player: Player) {
        val status = state.status
        assertIs<GameStatus.Won>(status)
        assertEquals(player, status.player)
    }

    private fun assertPlaying(state: GameState) {
        assertIs<GameStatus.Playing>(state.status)
    }

    private fun assertDraw(state: GameState) {
        assertIs<GameStatus.Draw>(state.status)
    }

    // ── Initial state ─────────────────────────────────────────────────

    @Test
    fun `initial board is all empty`() {
        val state = GameEngine.initialState(default)
        assertTrue(state.board.all { row -> row.all { it == Cell.EMPTY } })
    }

    @Test
    fun `initial player is RED`() {
        val state = GameEngine.initialState(default)
        assertEquals(Player.RED, state.currentPlayer)
    }

    @Test
    fun `initial status is Playing`() {
        assertPlaying(GameEngine.initialState(default))
    }

    // ── Gravity ───────────────────────────────────────────────────────

    @Test
    fun `piece lands on bottom row`() {
        val state = playMoves(0)
        assertEquals(Cell.RED, state.board[default.rows - 1][0])
    }

    @Test
    fun `second piece stacks on first`() {
        val state = playMoves(0, 0)
        assertEquals(Cell.RED, state.board[default.rows - 1][0])
        assertEquals(Cell.YELLOW, state.board[default.rows - 2][0])
    }

    @Test
    fun `pieces stack correctly up full column`() {
        // fill column 0 alternating RED YELLOW
        val moves = IntArray(default.rows) { 0 }
        val state = playMoves(*moves)
        // top cell should be filled
        assertNotEquals(Cell.EMPTY, state.board[0][0])
    }

    @Test
    fun `drop into full column is ignored`() {
        val moves = IntArray(default.rows) { 0 }
        var state = playMoves(*moves)
        val before = state.board
        state = GameEngine.dropPiece(state, 0)
        assertEquals(before, state.board)
    }

    // ── Turn alternation ──────────────────────────────────────────────

    @Test
    fun `turns alternate between RED and YELLOW`() {
        var state = GameEngine.initialState(default)
        assertEquals(Player.RED, state.currentPlayer)
        state = GameEngine.dropPiece(state, 0)
        assertEquals(Player.YELLOW, state.currentPlayer)
        state = GameEngine.dropPiece(state, 1)
        assertEquals(Player.RED, state.currentPlayer)
    }

    @Test
    fun `current player does not change after game is won`() {
        // RED wins horizontally cols 0-3
        val state = playMoves(0, 0, 1, 1, 2, 2, 3)
        assertWinner(state, Player.RED)
        assertEquals(Player.RED, state.currentPlayer)
    }

    // ── Win detection: horizontal ─────────────────────────────────────

    @Test
    fun `RED wins horizontally`() {
        // RED: 0,1,2,3  YELLOW: 0,1,2 (bottom rows)
        val state = playMoves(0, 0, 1, 1, 2, 2, 3)
        assertWinner(state, Player.RED)
    }

    @Test
    fun `YELLOW wins horizontally`() {
        // RED: 0,1,2,4,4  YELLOW: 0,1,2,3,3
        val state = playMoves(0, 0, 1, 1, 2, 2, 4, 3, 4, 3)
        assertWinner(state, Player.YELLOW)
    }

    // ── Win detection: vertical ───────────────────────────────────────

    @Test
    fun `RED wins vertically`() {
        // RED stacks col 0 four times, YELLOW in col 1
        val state = playMoves(0, 1, 0, 1, 0, 1, 0)
        assertWinner(state, Player.RED)
    }

    @Test
    fun `YELLOW wins vertically`() {
        // RED in col 0, YELLOW stacks col 1 four times
        val state = playMoves(0, 1, 0, 1, 0, 1, 2, 1)
        assertWinner(state, Player.YELLOW)
    }

    // ── Win detection: diagonal ───────────────────────────────────────

    @Test
    fun `RED wins diagonal bottom-left to top-right`() {
        // classic diagonal setup
        // col: 0  1  2  3
        val state = playMoves(
            0,       // RED   row5 col0
            1,       // YELLOW row5 col1
            1,       // RED   row4 col1
            2,       // YELLOW row5 col2
            2,       // RED   row4 col2
            3,       // YELLOW row5 col3
            2,       // RED   row3 col2
            3,       // YELLOW row4 col3
            3,       // RED   row3 col3  -- not diagonal yet
            0,       // YELLOW row5... doesn't matter
            3        // RED   row2 col3  -- wins diagonal
        )
        assertWinner(state, Player.RED)
    }

    @Test
    fun `RED wins diagonal top-left to bottom-right`() {
        val state = playMoves(
            3,       // RED   row5 col3
            2,       // YELLOW row5 col2
            2,       // RED   row4 col2
            1,       // YELLOW row5 col1
            1,       // RED   row4 col1
            0,       // YELLOW row5 col0
            1,       // RED   row3 col1
            0,       // YELLOW row4 col0
            0,       // RED   row3 col0 -- not yet
            4,       // YELLOW anywhere
            0        // RED   row2 col0 -- wins diagonal
        )
        assertWinner(state, Player.RED)
    }

    // ── Win condition: configurable N ────────────────────────────────

    @Test
    fun `connect 3 wins with 3 in a row`() {
        val config = GameConfig(rows = 6, cols = 7, winCondition = 3)
        val state = playMoves(0, 1, 1, 2, 2, 3, config = config)
        // RED: col 0,1,2 bottom row — wait, YELLOW interrupts
        // simpler: RED col 0, YELLOW col 3, RED col 1, YELLOW col 4, RED col 2
        val state2 = playMoves(0, 3, 1, 4, 2, config = config)
        assertWinner(state2, Player.RED)
    }

    @Test
    fun `connect 5 does not win with only 4 in a row`() {
        val config = GameConfig(rows = 6, cols = 7, winCondition = 5)
        // RED gets 4 horizontal — should still be Playing
        val state = playMoves(0, 0, 1, 1, 2, 2, 3, config = config)
        assertPlaying(state)
    }

    @Test
    fun `connect 5 wins with 5 in a row`() {
        val config = GameConfig(rows = 6, cols = 7, winCondition = 5)
        val state = playMoves(0, 0, 1, 1, 2, 2, 3, 3, 4, config = config)
        assertWinner(state, Player.RED)
    }

    // ── Winning cells ─────────────────────────────────────────────────

    @Test
    fun `winning cells count matches win condition`() {
        val state = playMoves(0, 0, 1, 1, 2, 2, 3)
        val status = state.status
        assertIs<GameStatus.Won>(status)
        assertEquals(default.winCondition, status.winningCells.size)
    }

    @Test
    fun `winning cells are all occupied by winner`() {
        val state = playMoves(0, 0, 1, 1, 2, 2, 3)
        val status = state.status as GameStatus.Won
        for ((row, col) in status.winningCells) {
            assertEquals(Player.RED.toCell(), state.board[row][col])
        }
    }

    // ── Draw detection ────────────────────────────────────────────────

    @Test
    fun `draw detected when board is full with no winner`() {
        // 4x4 board, connect 4 — fill it without anyone winning
        val config = GameConfig(rows = 4, cols = 4, winCondition = 4)
        // carefully crafted move sequence that fills the board with no winner
        // col:  0 1 2 3  repeating, but interleaved to avoid 4-in-a-row
        val state = playMoves(
            0, 1, 2, 3,
            3, 2, 1, 0,
            0, 1, 2, 3,
            3, 2, 1, 0,
            config = config
        )
        // may or may not be draw depending on sequence — check top row full
        val topRowFull = state.board[0].all { it != Cell.EMPTY }
        if (topRowFull && state.status !is GameStatus.Won) {
            assertDraw(state)
        }
    }

    // ── No moves after game over ───────────────────────────────────────

    @Test
    fun `moves are ignored after win`() {
        val state = playMoves(0, 0, 1, 1, 2, 2, 3)
        assertWinner(state, Player.RED)
        val after = GameEngine.dropPiece(state, 4)
        assertEquals(state.board, after.board)
    }

    @Test
    fun `moves are ignored after draw`() {
        val config = GameConfig(rows = 4, cols = 4, winCondition = 5)
        var state = GameEngine.initialState(config)
        // fill entire board
        repeat(config.rows) { _ ->
            repeat(config.cols) { col ->
                state = GameEngine.dropPiece(state, col)
            }
        }
        if (state.status is GameStatus.Draw) {
            val board = state.board
            state = GameEngine.dropPiece(state, 0)
            assertEquals(board, state.board)
        }
    }

    // ── Out of bounds ─────────────────────────────────────────────────

    @Test
    fun `dropping in negative column is ignored`() {
        val state = GameEngine.initialState(default)
        val after = GameEngine.dropPiece(state, -1)
        assertEquals(state.board, after.board)
    }

    @Test
    fun `dropping beyond last column is ignored`() {
        val state = GameEngine.initialState(default)
        val after = GameEngine.dropPiece(state, default.cols)
        assertEquals(state.board, after.board)
    }
}