package com.anafthdev.comdeo.foundation.uicomponent

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@Composable
fun ObserveLifecycle(
	onCreate: () -> Unit = {},
	onStart: () -> Unit = {},
	onResume: () -> Unit = {},
	onPause: () -> Unit = {},
	onStop: () -> Unit = {},
	onDestroy: () -> Unit = {},
	onAny: () -> Unit = {},
) {

	val owner = LocalLifecycleOwner.current

	val rememberedOnCreate by rememberUpdatedState(newValue = onCreate)
	val rememberedOnStart by rememberUpdatedState(newValue = onStart)
	val rememberedOnResume by rememberUpdatedState(newValue = onResume)
	val rememberedOnPause by rememberUpdatedState(newValue = onPause)
	val rememberedOnStop by rememberUpdatedState(newValue = onStop)
	val rememberedOnDestroy by rememberUpdatedState(newValue = onDestroy)
	val rememberedOnAny by rememberUpdatedState(newValue = onAny)

	DisposableEffect(owner) {
		val observer = LifecycleEventObserver { _, event ->
			when (event) {
				Lifecycle.Event.ON_CREATE -> rememberedOnCreate()
				Lifecycle.Event.ON_START -> rememberedOnStart()
				Lifecycle.Event.ON_RESUME -> rememberedOnResume()
				Lifecycle.Event.ON_PAUSE -> rememberedOnPause()
				Lifecycle.Event.ON_STOP -> rememberedOnStop()
				Lifecycle.Event.ON_DESTROY -> rememberedOnDestroy()
				Lifecycle.Event.ON_ANY -> rememberedOnAny()
			}
		}

		owner.lifecycle.addObserver(observer)

		onDispose { owner.lifecycle.removeObserver(observer) }
	}

}
