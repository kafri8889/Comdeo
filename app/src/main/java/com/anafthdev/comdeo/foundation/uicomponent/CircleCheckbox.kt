package com.anafthdev.comdeo.foundation.uicomponent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp

private class SelectableProvider: PreviewParameterProvider<Boolean> {
	override val values: Sequence<Boolean>
		get() = sequenceOf(true, false)
}

@Preview
@Composable
private fun CircleCheckboxPreview(@PreviewParameter(SelectableProvider::class) checked: Boolean) {
	CircleCheckbox(
		checked = checked,
		onCheckedChange = {}
	)
}

@Composable
fun CircleCheckbox(
	checked: Boolean,
	onCheckedChange: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
	colors: CircleCheckboxColors = CircleCheckboxDefaults.colors()
) {

	val backgroundColor by colors.boxColor(checked)
	val checkMarkColor by colors.checkMarkColor(checked)
	val borderColor by colors.borderColor(checked)

	Box(
		contentAlignment = Alignment.Center,
		modifier = modifier
			.minimumInteractiveComponentSize()
			.clip(CircleShape)
			.clickable {
				onCheckedChange(!checked)
			}
			.size(24.dp)
			.background(backgroundColor)
			.border(
				width = 1.dp,
				color = borderColor,
				shape = CircleShape
			)
	) {
		AnimatedVisibility(
			visible = checked,
			enter = scaleIn(tween(256)),
			exit = scaleOut(tween(256))
		) {
			Icon(
				imageVector = Icons.Rounded.Check,
				contentDescription = null,
				tint = checkMarkColor,
				modifier = Modifier
					.size(16.dp)
			)
		}
	}
}

object CircleCheckboxDefaults {

	@Composable
	fun colors(
		checkedBoxColor: Color = MaterialTheme.colorScheme.primary,
		uncheckedBoxColor: Color = Color.Transparent,
		checkedBorderColor: Color = Color.Transparent,
		uncheckedBorderColor: Color = MaterialTheme.colorScheme.outline,
		uncheckedCheckmarkColor: Color = Color.Transparent,
		checkedCheckmarkColor: Color = MaterialTheme.colorScheme.contentColorFor(checkedBoxColor),
	): CircleCheckboxColors {
		return CircleCheckboxColors(
			uncheckedCheckmarkColor = uncheckedCheckmarkColor,
			checkedCheckmarkColor = checkedCheckmarkColor,
			uncheckedBorderColor = uncheckedBorderColor,
			checkedBorderColor = checkedBorderColor,
			uncheckedBoxColor = uncheckedBoxColor,
			checkedBoxColor = checkedBoxColor,
		)
	}
}

@Immutable
class CircleCheckboxColors(
	val checkedBoxColor: Color,
	val uncheckedBoxColor: Color,
	val checkedBorderColor: Color,
	val uncheckedBorderColor: Color,
	val checkedCheckmarkColor: Color,
	val uncheckedCheckmarkColor: Color,
) {

	@Composable
	fun boxColor(checked: Boolean): State<Color> {
		return animateColorAsState(
			targetValue = if (checked) checkedBoxColor else uncheckedBoxColor,
			animationSpec = tween(256)
		)
	}

	@Composable
	fun borderColor(checked: Boolean): State<Color> {
		return animateColorAsState(
			targetValue = if (checked) checkedBorderColor else uncheckedBorderColor,
			animationSpec = tween(256)
		)
	}

	@Composable
	fun checkMarkColor(checked: Boolean): State<Color> {
		return animateColorAsState(
			targetValue = if (checked) checkedCheckmarkColor else uncheckedCheckmarkColor,
			animationSpec = tween(256)
		)
	}
}
