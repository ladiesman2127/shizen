package jp.aqua.shizen

import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import jp.aqua.shizen.dictionary.presentation.DictionaryViewModel
import jp.aqua.shizen.listen.presentation.ListenViewModel
import jp.aqua.shizen.navigation.RootNavHost
import jp.aqua.shizen.read.presentation.ReadViewModel
import jp.aqua.shizen.ui.theme.ShizenTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShizenTheme {
                RootNavHost()
            }
        }
    }

}


