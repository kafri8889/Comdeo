package com.anafthdev.comdeo.ui.change_video_name

import android.app.Activity
import android.app.RecoverableSecurityException
import android.content.Context
import android.content.IntentSender
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anafthdev.comdeo.R
import com.anafthdev.comdeo.data.model.Video
import com.anafthdev.comdeo.foundation.theme.ComdeoTheme
import com.anafthdev.comdeo.foundation.uicomponent.DragHandle
import com.anafthdev.comdeo.util.FileUtils
import timber.log.Timber

@Preview
@Composable
private fun ChangeVideoNameScreenPreview() {
	ComdeoTheme {
		Box(
			modifier = Modifier
				.clip(RoundedCornerShape(topStartPercent = 36, topEndPercent = 36))
				.background(MaterialTheme.colorScheme.surface)
		) {
			ChangeVideoNameScreenContent(
				state = ChangeVideoNameState(),
				onNameChanged = {},
				onSave = {},
				modifier = Modifier
					.padding(16.dp)
			)
		}
	}
}

@Composable
fun ChangeVideoNameScreen(
	viewModel: ChangeVideoNameViewModel,
	onNavigateUp: () -> Unit
) {

	val context = LocalContext.current

	val state by viewModel.state.collectAsStateWithLifecycle()

	val renameLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.StartIntentSenderForResult(),
		onResult = { activityResult ->
			// if user grant permission to rename video for SDK_INT >= Q.
			// if you not recalling "tryRenameVideo" function, the video name will not change
			if (activityResult.resultCode == Activity.RESULT_OK) {
				Timber.i("Permission granted")
				tryRenameVideo(
					context = context,
					newFileName = state.name,
					video = state.video!!,
					onLaunchIntentSender = {},
					onSuccess = {
						viewModel.onAction(ChangeVideoNameAction.Scan)
						onNavigateUp()
					}
				)
			}
		}
	)

	ChangeVideoNameScreenContent(
		state = state,
		onNameChanged = { name ->
			viewModel.onAction(ChangeVideoNameAction.UpdateName(name))
		},
		onSave = {
			tryRenameVideo(
				context = context,
				newFileName = state.name,
				video = state.video!!,
				onLaunchIntentSender = { intentSender ->
					renameLauncher.launch(
						IntentSenderRequest.Builder(intentSender)
							.setFillInIntent(null)
							.build()
					)
				},
				onSuccess = {
					viewModel.onAction(ChangeVideoNameAction.Scan)
					onNavigateUp()
				}
			)
		},
		modifier = Modifier
			.padding(16.dp)
	)

}

@Composable
private fun ChangeVideoNameScreenContent(
	state: ChangeVideoNameState,
	modifier: Modifier = Modifier,
	onNameChanged: (String) -> Unit,
	onSave: () -> Unit
) {

	val focusManager = LocalFocusManager.current

	val textFieldFocusRequester = remember { FocusRequester() }

	var textFieldValue by remember { mutableStateOf(TextFieldValue()) }

	LaunchedEffect(textFieldFocusRequester) {
		textFieldFocusRequester.requestFocus()
	}

	LaunchedEffect(state.name != textFieldValue.text) {
		textFieldValue = textFieldValue.copy(
			text = state.name,
			// Move cursor to last character
			selection = if (state.name.isNotBlank()) TextRange(0, state.name.length)
			else TextRange.Zero
		)
	}

	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = modifier
	) {
		DragHandle(
			modifier = Modifier
				.fillMaxWidth(0.16f)
		)

		Spacer(modifier = Modifier.height(16.dp))

		BasicTextField(
			value = textFieldValue,
			maxLines = 3,
			onValueChange = { newTextFieldValue ->
				textFieldValue = newTextFieldValue

				if (newTextFieldValue.text != state.name) onNameChanged(newTextFieldValue.text)
			},
			textStyle = MaterialTheme.typography.bodyMedium.copy(
				color = MaterialTheme.colorScheme.onSurface
			),
			keyboardOptions = KeyboardOptions(
				imeAction = ImeAction.Done
			),
			keyboardActions = KeyboardActions(
				onDone = {
					focusManager.clearFocus(true)
				}
			),
			cursorBrush = Brush.verticalGradient(
				listOf(
					MaterialTheme.colorScheme.onSurface,
					MaterialTheme.colorScheme.onSurface
				)
			),
			decorationBox = { innerTextField ->
				Column {
					Row(
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.spacedBy(8.dp),
						modifier = Modifier
							.fillMaxWidth()
					) {
						Box(
							modifier = Modifier
								.weight(1f)
						) {
							innerTextField()

							if (state.name.isBlank()) {
								Text(
									text = stringResource(id = R.string.write_video_name),
									style = MaterialTheme.typography.bodyMedium.copy(
										color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.48f)
									)
								)
							}
						}

						IconButton(
							onClick = {
								onNameChanged("")
							}
						) {
							Icon(
								painter = painterResource(id = R.drawable.ic_close),
								contentDescription = null
							)
						}
					}

					HorizontalDivider(
						color = MaterialTheme.colorScheme.outline
					)
				}
			},
			modifier = Modifier
				.fillMaxWidth()
				.focusRequester(textFieldFocusRequester)
		)

		Spacer(modifier = Modifier.height(8.dp))

		Button(
			onClick = onSave,
			enabled = state.name.isNotBlank(),
			modifier = Modifier
				.fillMaxWidth()
				.navigationBarsPadding()
		) {
			Text(stringResource(id = R.string.save))
		}
	}
}

private fun tryRenameVideo(
	context: Context,
	newFileName: String,
	video: Video,
	onSuccess: () -> Unit,
	onLaunchIntentSender: (intentSender: IntentSender) -> Unit
) {
	val fileExt = video.displayName.substringAfterLast(".")
	val mNewFileName = "${newFileName}.$fileExt"

	try {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) FileUtils.renameFile(context, video.path.toUri(), mNewFileName)
		else FileUtils.renameFile(context, video.path.toUri(), mNewFileName) {
			"${MediaStore.Video.Media._ID} = ${video.id}"
		}

		onSuccess()
	} catch (e: Exception) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			if (e is RecoverableSecurityException) onLaunchIntentSender(e.userAction.actionIntent.intentSender)
		} else {
			e.printStackTrace()
		}
	}
}
