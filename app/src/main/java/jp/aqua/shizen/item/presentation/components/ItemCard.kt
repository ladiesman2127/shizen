package jp.aqua.shizen.item.presentation.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import jp.aqua.shizen.R

@Composable
fun ItemCard(
    modifier: Modifier = Modifier,
    title: String,
    cover: String?,
    labeled: Boolean = false,
    selected: Boolean,
) {
    Card(
        modifier = modifier
            .aspectRatio(2 / 3f)
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        shape = RoundedCornerShape(5.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = cover,
                contentScale = ContentScale.Crop,
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(25.dp)
                    )
                },
                contentDescription = stringResource(R.string.book_cover),
                onError = { Log.i("Cover image", it.result.throwable.message.toString()) }
            )
            if (labeled) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                0f to Color.Black.copy(alpha = 0.0f),
                                0.4f to Color.Black.copy(alpha = 0.5f),
                                1f to Color.Black.copy(alpha = 0.8f),
                            )
                        )
                        .padding(5.dp),
                    text = title,
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            if (selected) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5F))
                        .border(3.dp, color = MaterialTheme.colorScheme.errorContainer)
                        .clip(RoundedCornerShape(5.dp))
                )
            }
        }
    }
}
