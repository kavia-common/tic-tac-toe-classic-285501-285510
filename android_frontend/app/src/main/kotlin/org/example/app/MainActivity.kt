package org.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.example.app.ui.theme.OceanTheme

/**
 * PUBLIC_INTERFACE
 * MainActivity is the single-activity entry point hosting the Tic Tac Toe game using Jetpack Compose.
 * It sets the theme, provides the GameViewModel, and renders the GameScreen.
 */
class MainActivity : ComponentActivity() {

    private val vm: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OceanTheme {
                Scaffold(
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        GameScreen()
                    }
                }
            }
        }
    }
}

/**
 * PUBLIC_INTERFACE
 * GameScreen renders the entire screen: background gradient, player indicator, board, result, and reset button.
 */
@Composable
fun GameScreen(viewModel: GameViewModel = viewModel()) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.06f),
            MaterialTheme.colorScheme.background
        )
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header / Player Indicator
        PlayerIndicator(
            currentPlayer = viewModel.currentPlayer,
            status = viewModel.gameStatus
        )

        // Board
        Board(
            board = viewModel.board,
            winnerLine = viewModel.winningLine,
            onCellTap = { index -> viewModel.onCellTap(index) },
            enabled = viewModel.gameStatus == GameStatus.Playing
        )

        // Result + Reset
        ResultBar(status = viewModel.gameStatus)
        ResetButton(onClick = { viewModel.resetGame() })
    }
}

/**
 * PUBLIC_INTERFACE
 * PlayerIndicator shows the current player or the ended state.
 */
@Composable
fun PlayerIndicator(currentPlayer: Player, status: GameStatus) {
    val text = when (status) {
        GameStatus.Playing -> "Current: ${currentPlayer.symbol}"
        GameStatus.XWon -> "Winner: X"
        GameStatus.OWon -> "Winner: O"
        GameStatus.Draw -> "It's a Draw"
    }
    val color = when (status) {
        GameStatus.Playing -> MaterialTheme.colorScheme.primary
        GameStatus.XWon, GameStatus.OWon -> MaterialTheme.colorScheme.secondary
        GameStatus.Draw -> MaterialTheme.colorScheme.onSurface
    }
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
        modifier = Modifier
            .padding(top = 8.dp)
            .wrapContentHeight()
    )
}

/**
 * PUBLIC_INTERFACE
 * Board lays out a centered 3x3 grid. Cells are square with rounded corners and subtle elevation.
 */
@Composable
fun Board(
    board: List<Player?>,
    winnerLine: Set<Int>,
    onCellTap: (Int) -> Unit,
    enabled: Boolean
) {
    val cellShape = RoundedCornerShape(14.dp)
    val borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)

    // Using LazyVerticalGrid for concise 3x3 grid
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .padding(vertical = 16.dp)
            .size(320.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(4.dp)
    ) {
        itemsIndexed(board) { index, cell ->
            val isWinnerCell = winnerLine.contains(index)
            val cardColors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
            val elevation = if (isWinnerCell) 10.dp else 4.dp
            val scale by animateFloatAsState(
                targetValue = if (isWinnerCell) 1.05f else 1.0f,
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                label = "winnerScale"
            )
            ElevatedCard(
                modifier = Modifier
                    .aspectRatio(1f)
                    .scale(scale)
                    .clickable(
                        enabled = enabled && cell == null,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onCellTap(index) },
                shape = cellShape,
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = elevation),
                colors = cardColors
            ) {
                Box(
                    modifier = Modifier
                        .background(borderColor.copy(alpha = 0.0f))
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Cell(cell)
                }
            }
        }
    }
}

/**
 * PUBLIC_INTERFACE
 * Cell renders X or O with Ocean colors and a small tap animation.
 */
@Composable
fun Cell(value: Player?) {
    val display = value?.symbol ?: ""
    val targetScale = if (value == null) 0.9f else 1f
    val scale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = tween(180),
        label = "cellScale"
    )
    val color: Color = when (value) {
        Player.X -> MaterialTheme.colorScheme.primary
        Player.O -> MaterialTheme.colorScheme.secondary
        null -> MaterialTheme.colorScheme.onSurface
    }
    Text(
        text = display,
        color = color,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.scale(scale)
    )
}

/**
 * PUBLIC_INTERFACE
 * ResultBar shows a status line with a subtle fade transition.
 */
@Composable
fun ResultBar(status: GameStatus) {
    val text = when (status) {
        GameStatus.Playing -> "Make your move"
        GameStatus.XWon -> "X Wins! Great game."
        GameStatus.OWon -> "O Wins! Great game."
        GameStatus.Draw -> "Draw. Try again!"
    }
    val color = when (status) {
        GameStatus.Playing -> MaterialTheme.colorScheme.primary
        GameStatus.XWon, GameStatus.OWon -> MaterialTheme.colorScheme.secondary
        GameStatus.Draw -> MaterialTheme.colorScheme.onSurface
    }
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(250)),
        exit = fadeOut(animationSpec = tween(250))
    ) {
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

/**
 * PUBLIC_INTERFACE
 * ResetButton clears the board; contained primary button with ripple.
 */
@Composable
fun ResetButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(top = 8.dp)
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        )
    ) {
        Text(
            text = "Reset",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}
