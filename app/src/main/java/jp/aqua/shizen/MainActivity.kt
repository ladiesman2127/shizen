package jp.aqua.shizen

// Импорт необходимых библиотек
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import jp.aqua.shizen.navigation.RootNavHost
import jp.aqua.shizen.ui.theme.ShizenTheme

class MainActivity : ComponentActivity() {
    // Перезапись методы родительского класса
    override fun onCreate(savedInstanceState: Bundle?) {
        // Вызов методы родительского класса
        super.onCreate(savedInstanceState)
        // Включение режима edge-to-edge для отображения приложения по всему экрану
        enableEdgeToEdge()
        // Установка содержания
        setContent {
            // Определение темы приложения
            ShizenTheme {
                // Вызов главного Root хоста навигации
                RootNavHost()
            }
        }
    }
}




