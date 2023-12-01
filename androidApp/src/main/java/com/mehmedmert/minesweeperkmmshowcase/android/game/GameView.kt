package com.mehmedmert.minesweeperkmmshowcase.android.game

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mehmedmert.minesweeperkmmshowcase.android.R

@Composable
fun GameView(
    gameViewStatus: GameViewStatus,
    onCellClicked: (row: Int, column: Int) -> Unit,
    onCellLongPressed: (row: Int, column: Int) -> Unit,
    onStartNewGameClicked: () -> Unit,
) {
    if (gameViewStatus.status != Status.Initial) {
        MinesView(
            gameViewStatus = gameViewStatus,
            onCellClicked = onCellClicked,
            onCellLongPressed = onCellLongPressed,
        )
    }
}

@Composable
private fun MinesView(
    gameViewStatus: GameViewStatus,
    onCellClicked: (row: Int, column: Int) -> Unit,
    onCellLongPressed: (row: Int, column: Int) -> Unit,
) {
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .verticalScroll(verticalScrollState)
            .horizontalScroll(horizontalScrollState)
    ) {
        Row {
            for (row in 0..<gameViewStatus.rows) {
                Column {
                    for (column in 0..<gameViewStatus.columns) {
                        val cellIndex = row * gameViewStatus.columns + column
                        CellView(
                            cell = gameViewStatus.cells[cellIndex],
                            onClicked = { onCellClicked(row, column) },
                            onLongPressed = { onCellLongPressed(row, column) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CellView(
    cell: Cell,
    onClicked: () -> Unit,
    onLongPressed: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .border(width = 0.125.dp, color = Color.LightGray)
    ) {
        when (cell) {
            is Cell.Closed, is Cell.Flagged -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .combinedClickable(
                        onClick = onClicked,
                        onLongClick = onLongPressed
                    )

            ) {
                ClosedCell(cell)
            }
            is Cell.Open, is Cell.Mined -> OpenCell(cell)
        }
    }
}

@Composable
private fun BoxScope.OpenCell(cell: Cell) {
    when (cell) {
        is Cell.Mined -> Image(
            painter = painterResource(id = R.drawable.twotone_dangerous_24),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center)
        )

        is Cell.Open -> Text(
            text = cell.minesAroundCount.toString(),
            modifier = Modifier.align(Alignment.Center)
        )
        else -> {}
    }
}

@Composable
private fun BoxScope.ClosedCell(
    cell: Cell
) {
    when (cell) {
        is Cell.Flagged -> Image(
            painter = painterResource(id = R.drawable.twotone_flag_24),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center)
        )

        is Cell.Closed -> Box {}
        else -> {}
    }
}

@Preview
@Composable
private fun GameViewPreview() {
    GameView(
        gameViewStatus = GameViewStatus(
            rows = 3,
            columns = 3,
            cells = listOf(
                Cell.Open(1),
                Cell.Closed,
                Cell.Flagged,
                Cell.Mined,
                Cell.Open(2),
                Cell.Open(3),
                Cell.Open(4),
                Cell.Open(5),
                Cell.Open(6)
            ),
            status = Status.Running,
            startTime = null,
            remainingMines = 3,
        ),
        onCellClicked = { _, _ -> },
        onCellLongPressed = { _, _ -> },
        onStartNewGameClicked = {},
    )
}
