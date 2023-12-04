package com.anafthdev.comdeo.foundation.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import com.anafthdev.comdeo.R
import com.anafthdev.comdeo.data.model.Video
import com.anafthdev.comdeo.util.DateUtil
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@Composable
fun VideoItem(
    video: Video,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .padding(8.dp)
        ) {
            VideoThumbnail(
                video = video,
                maxWidth = this@BoxWithConstraints.maxWidth
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .align(Alignment.Top)
                    .weight(1f)
            ) {
                Text(
                    text = video.displayName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = DateUtil.formatMedium(video.dateAdded),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Light
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun VideoThumbnail(
    video: Video,
    maxWidth: Dp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(maxWidth / 3.6f)
            .aspectRatio(1.4f / 1f)
            .clip(RoundedCornerShape(16))
            .background(Color.LightGray)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_video),
            contentDescription = null,
            tint = Color.DarkGray,
            modifier = Modifier
                .zIndex(1f)
        )

        GlideImage(
            model = video.path.toUri(),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
                .zIndex(2f)
        )
    }
}
