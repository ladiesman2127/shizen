package jp.aqua.shizen.dictionary.deck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import jp.aqua.shizen.AppViewModelProvider
import jp.aqua.shizen.dictionary.deck.presentation.DeckScreen
import jp.aqua.shizen.dictionary.deck.presentation.DeckViewModel
import jp.aqua.shizen.ui.theme.ShizenTheme

class DeckActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShizenTheme {
                val viewModel: DeckViewModel by viewModels<DeckViewModel> {
                    AppViewModelProvider.Factory
                }
                val uiState by viewModel.uiState.collectAsState()
                val context = LocalContext.current
                DeckScreen(
                    context = context,
                    viewModel = viewModel,
                    uiState = uiState,
                    onNavigateUp = {
                        finish()
                    }
                )
            }
        }
    }
}