package com.mehmedmert.minesweeperkmmshowcase.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.mehmedmert.minesweeperkmmshowcase.android.game.GameView
import com.mehmedmert.minesweeperkmmshowcase.android.game.GameViewModel
import com.mehmedmert.minesweeperkmmshowcase.android.game.GameViewStatus
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val gameViewModel by viewModel<GameViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val gameViewStatus = gameViewModel.gameViewStatus.collectAsState(initial = GameViewStatus())
                    GameView(
                        gameViewStatus = gameViewStatus.value,
                        onCellClicked = gameViewModel::onOpenCell,
                        onCellDoubleClicked = gameViewModel::onToggleFlag,
                        onStartNewGameClicked = gameViewModel::onStartNewGame,
                    )
                }
            }
        }
    }
}
