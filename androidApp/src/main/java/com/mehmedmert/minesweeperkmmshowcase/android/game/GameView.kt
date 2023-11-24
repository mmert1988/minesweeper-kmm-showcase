package com.mehmedmert.minesweeperkmmshowcase.android.game

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun GameView(
    gameViewStatus: GameViewStatus,
    onCellClicked: (row: Int, column: Int) -> Unit,
    onCellDoubleClicked: (row: Int, column: Int) -> Unit,
    onStartNewGameClicked: () -> Unit,
) {
    Box {
        Text(text = "Halloo")
    }
}
