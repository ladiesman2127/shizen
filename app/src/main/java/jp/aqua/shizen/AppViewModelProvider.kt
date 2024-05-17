package jp.aqua.shizen

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.media3.exoplayer.ExoPlayer
import jp.aqua.shizen.dictionary.deck.presentation.DeckViewModel
import jp.aqua.shizen.dictionary.presentation.DictionaryViewModel
import jp.aqua.shizen.listen.presentation.ListenViewModel
import jp.aqua.shizen.listen.player.PlayerViewModel
import jp.aqua.shizen.listen.video.presentation.VideoViewModel
import jp.aqua.shizen.read.presentation.ReadViewModel
import jp.aqua.shizen.read.book.presentation.BookViewModel
import jp.aqua.shizen.read.reader.presentation.ReaderViewModel
import org.readium.r2.shared.ExperimentalReadiumApi

object AppViewModelProvider {

    @OptIn(ExperimentalReadiumApi::class)
    val Factory = viewModelFactory {
        initializer {
            ReadViewModel(
                application().bookRepository,
            )
        }
        initializer {
            BookViewModel(
                createSavedStateHandle(),
                application().bookRepository,
                application().readerRepository
            )
        }
        initializer {
            ListenViewModel(
                application().videoRepository
            )
        }
        initializer {
            VideoViewModel(
                createSavedStateHandle(),
                application().videoRepository
            )
        }
        initializer {
            ReaderViewModel(
                application().readerRepository,
            )
        }

        initializer {
            PlayerViewModel(
                createSavedStateHandle(),
                ExoPlayer.Builder(application()).build(),
                application().videoRepository
            )
        }

        initializer {
            DictionaryViewModel(
                application().deckRepository
            )
        }

        initializer {
            DeckViewModel(
                createSavedStateHandle(),
                application().deckRepository
            )
        }
    }
}

fun CreationExtras.application(): Application =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)