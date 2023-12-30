package com.anafthdev.comdeo.foundation.uicomponent

import android.view.ViewGroup.LayoutParams
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@UnstableApi
@Immutable
data class VideoPlayerOptions(
	val layoutParams: LayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT),
	val resizeMode: Int = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH,
	val useController: Boolean = false,
)

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
	player: Player,
	modifier: Modifier = Modifier,
	options: VideoPlayerOptions = VideoPlayerOptions()
) {

	AndroidView(
		modifier = modifier,
		factory = { ctx ->
			PlayerView(ctx).apply {
				this.player = player
				useController = options.useController
				layoutParams = options.layoutParams
				resizeMode = options.resizeMode
			}
		},
		onRelease = {
			player.release()
		}
	)
}
