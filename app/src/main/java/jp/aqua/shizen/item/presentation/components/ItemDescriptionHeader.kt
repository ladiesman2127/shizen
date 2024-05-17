package jp.aqua.shizen.item.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Clock
import compose.icons.evaicons.outline.Globe
import compose.icons.evaicons.outline.Person
import jp.aqua.shizen.R
import jp.aqua.shizen.item.presentation.ItemUiState
import org.joda.time.LocalDate

@Composable
fun ItemDescriptionHeader(
    modifier: Modifier = Modifier,
    uiState: ItemUiState,
    innerPadding: PaddingValues,
    updateCover: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(300.dp)
            .fillMaxWidth()
    ) {
        AsyncImage(
            model = uiState.cover,
            contentDescription = stringResource(R.string.book_cover),
            modifier = Modifier
                .fillMaxSize()
                .blur(radius = 2.dp), // TODO
            contentScale = ContentScale.FillWidth,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to MaterialTheme.colorScheme.background.copy(alpha = 0f),
                        1f to MaterialTheme.colorScheme.background
                    )
                )
        )
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ItemCard(
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable { updateCover() },
                title = uiState.title,
                cover = uiState.cover,
                selected = false,
            )
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    uiState.title,
                    Modifier.fillMaxWidth(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge
                )
                uiState.authors?.let {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            modifier = Modifier.padding(end = 10.dp),
                            imageVector = EvaIcons.Outline.Person,
                            contentDescription = null
                        )
                        Text(
                            text = it.joinToString(
                                prefix = "[",
                                separator = ",",
                                postfix = "]"
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
                uiState.language.let {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            modifier = Modifier.padding(end = 10.dp),
                            imageVector = EvaIcons.Outline.Globe,
                            contentDescription = null
                        )
                        Text(
                            text = it,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.padding(end = 10.dp),
                        imageVector = EvaIcons.Outline.Clock,
                        contentDescription = null
                    )
                    Text(
                        text = LocalDate(uiState.creation).toString(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}
