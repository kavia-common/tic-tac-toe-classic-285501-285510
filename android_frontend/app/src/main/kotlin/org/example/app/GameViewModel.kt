package org.example.app

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

/**
 * Representation of players.
 */
enum class Player(val symbol: String) { X("X"), O("O") }

/**
 * PUBLIC_INTERFACE
 * GameStatus indicates current game state.
 */
enum class GameStatus {
    Playing, XWon, OWon, Draw
}

/**
 * PUBLIC_INTERFACE
 * GameViewModel holds the board, current player, and status. It exposes actions for tapping cells and resetting.
 * Uses SavedStateHandle to persist simple state across configuration changes.
 */
class GameViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    var board: List<Player?>
        private set
    var currentPlayer: Player
        private set
    var gameStatus: GameStatus
        private set
    var winningLine: Set<Int>
        private set

    init {
        val savedBoard = savedStateHandle.get<List<String>?>("board")
        val savedPlayer = savedStateHandle.get<String?>("currentPlayer")
        val savedStatus = savedStateHandle.get<String?>("gameStatus")
        val savedWinLine = savedStateHandle.get<Set<Int>?>("winningLine")

        board = savedBoard?.map { it.toPlayerOrNull() } ?: List(9) { null }
        currentPlayer = savedPlayer?.toPlayer() ?: Player.X
        gameStatus = savedStatus?.toGameStatus() ?: GameStatus.Playing
        winningLine = savedWinLine ?: emptySet()
    }

    /**
     * PUBLIC_INTERFACE
     * Handle a tap on the board at [index]. Ignores taps on non-empty cells or if game has ended.
     */
    fun onCellTap(index: Int) {
        if (index !in 0..8) return
        if (gameStatus != GameStatus.Playing) return
        if (board[index] != null) return

        val newBoard = board.toMutableList()
        newBoard[index] = currentPlayer
        board = newBoard

        // Evaluate
        val win = checkWin(newBoard, currentPlayer)
        if (win != null) {
            gameStatus = if (currentPlayer == Player.X) GameStatus.XWon else GameStatus.OWon
            winningLine = win.toSet()
        } else if (newBoard.all { it != null }) {
            gameStatus = GameStatus.Draw
        } else {
            // Toggle player
            currentPlayer = if (currentPlayer == Player.X) Player.O else Player.X
        }

        persist()
    }

    /**
     * PUBLIC_INTERFACE
     * Reset the game to initial state.
     */
    fun resetGame() {
        board = List(9) { null }
        currentPlayer = Player.X
        gameStatus = GameStatus.Playing
        winningLine = emptySet()
        persist()
    }

    private fun persist() {
        savedStateHandle["board"] = board.map { it?.symbol ?: "" }
        savedStateHandle["currentPlayer"] = currentPlayer.symbol
        savedStateHandle["gameStatus"] = gameStatus.name
        savedStateHandle["winningLine"] = winningLine
    }

    /**
     * PUBLIC_INTERFACE
     * Check if [player] has a winning combination on the [board]. Returns the winning indices or null.
     */
    fun checkWin(board: List<Player?>, player: Player): List<Int>? {
        val lines = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // rows
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // columns
            listOf(0, 4, 8), listOf(2, 4, 6) // diagonals
        )
        for (line in lines) {
            if (line.all { board[it] == player }) return line
        }
        return null
    }
}

private fun String.toPlayer() = if (this == "X") Player.X else Player.O
private fun String.toPlayerOrNull(): Player? = when (this) {
    "X" -> Player.X
    "O" -> Player.O
    else -> null
}
private fun String.toGameStatus(): GameStatus = when (this) {
    "Playing" -> GameStatus.Playing
    "XWon" -> GameStatus.XWon
    "OWon" -> GameStatus.OWon
    "Draw" -> GameStatus.Draw
    else -> GameStatus.Playing
}
