package com.anafthdev.comdeo.foundation.common

import android.app.Activity
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

// Author: kafri8889

/**
 * @param onNavigationBarVisibilityChanged Called when navigation bar visibility changed
 * @param onStatusBarVisibilityChanged Called when status bar visibility changed
 * @param onSystemBarVisibilityChanged Called when navigation bar and status bar visibility changed
 */
@Composable
fun rememberSystemBarsControllerState(
	initialNavigationBarVisibility: SystemBarsVisibility = SystemBarsVisibility.Visible,
	initialStatusBarVisibility: SystemBarsVisibility = SystemBarsVisibility.Visible,
	onNavigationBarVisibilityChanged: (visibility: SystemBarsVisibility, state: SystemBarsControllerState) -> Unit = { _, _ -> },
	onStatusBarVisibilityChanged: (visibility: SystemBarsVisibility, state: SystemBarsControllerState) -> Unit = { _, _ -> },
	onSystemBarVisibilityChanged: (visibility: SystemBarsVisibility, state: SystemBarsControllerState) -> Unit = { _, _ -> },
): SystemBarsControllerState {
	return remember {
		SystemBarsControllerState(initialNavigationBarVisibility, initialStatusBarVisibility).apply {
			setOnVisibilityChangedListener(
				object : SystemBarsControllerState.OnVisibilityChangedListener {
					override fun onNavigationBarVisibilityChanged(visibility: SystemBarsVisibility) {
						onNavigationBarVisibilityChanged(visibility, this@apply)
					}

					override fun onStatusBarVisibilityChanged(visibility: SystemBarsVisibility) {
						onStatusBarVisibilityChanged(visibility, this@apply)
					}

					override fun onSystemBarVisibilityChanged(visibility: SystemBarsVisibility) {
						onSystemBarVisibilityChanged(visibility, this@apply)
					}
				}
			)
		}
	}
}

@Composable
fun installSystemBarsController(
	state: SystemBarsControllerState = rememberSystemBarsControllerState()
) {

	val mState by rememberUpdatedState(newValue = state)

	val context = LocalContext.current

	val insetsController = remember(context) {
		val window = run {
			// Get activity from context
			while (context is ContextWrapper) {
				if (context is Activity) return@run context
				return@run context.baseContext as Activity
			}

			null
		}?.window ?: return@remember null

		WindowCompat.getInsetsController(window, window.decorView)
	}

	DisposableEffect(
		state.isNavigationBarVisible,
		state.isStatusBarVisible,
		state.isSystemBarVisible
	) {
		insetsController?.apply {
			systemBarsBehavior = if (mState.isSystemBarVisible) WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
			else WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

			if (mState.isNavigationBarVisible) show(WindowInsetsCompat.Type.navigationBars())
			else hide(WindowInsetsCompat.Type.navigationBars())

			if (mState.isStatusBarVisible) show(WindowInsetsCompat.Type.statusBars())
			else hide(WindowInsetsCompat.Type.statusBars())
		}

		onDispose {}
	}
}

enum class SystemBarsVisibility {
	Visible,
	Gone;

	val isVisible: Boolean
		get() = this == Visible

	val isGone: Boolean
		get() = this == Gone
}

class SystemBarsControllerState(
	initialNavigationBarVisibility: SystemBarsVisibility,
	initialStatusBarVisibility: SystemBarsVisibility
) {

	private var onVisibilityChangedListener: OnVisibilityChangedListener? = null

	private var navigationBarVisibility by mutableStateOf(initialNavigationBarVisibility)
	private var statusBarVisibility by mutableStateOf(initialStatusBarVisibility)

	val isNavigationBarVisible: Boolean
		get() = navigationBarVisibility.isVisible

	val isStatusBarVisible: Boolean
		get() = statusBarVisibility.isVisible

	val isSystemBarVisible: Boolean
		get() = navigationBarVisibility.isVisible && statusBarVisibility.isVisible

	fun showNavigationBar() {
		navigationBarVisibility = SystemBarsVisibility.Visible

		onVisibilityChangedListener?.onNavigationBarVisibilityChanged(SystemBarsVisibility.Visible)
		if (isSystemBarVisible) onVisibilityChangedListener?.onSystemBarVisibilityChanged(SystemBarsVisibility.Visible)
	}

	fun showStatusBar() {
		statusBarVisibility = SystemBarsVisibility.Visible

		onVisibilityChangedListener?.onStatusBarVisibilityChanged(SystemBarsVisibility.Visible)
		if (isSystemBarVisible) onVisibilityChangedListener?.onSystemBarVisibilityChanged(SystemBarsVisibility.Visible)
	}

	fun showSystemBar() {
		navigationBarVisibility = SystemBarsVisibility.Visible
		statusBarVisibility = SystemBarsVisibility.Visible

		onVisibilityChangedListener?.onSystemBarVisibilityChanged(SystemBarsVisibility.Visible)
	}

	fun hideNavigationBar() {
		navigationBarVisibility = SystemBarsVisibility.Gone

		onVisibilityChangedListener?.onNavigationBarVisibilityChanged(SystemBarsVisibility.Gone)
		if (!isSystemBarVisible) onVisibilityChangedListener?.onSystemBarVisibilityChanged(SystemBarsVisibility.Gone)
	}

	fun hideStatusBar() {
		statusBarVisibility = SystemBarsVisibility.Gone

		onVisibilityChangedListener?.onStatusBarVisibilityChanged(SystemBarsVisibility.Gone)
		if (!isSystemBarVisible) onVisibilityChangedListener?.onSystemBarVisibilityChanged(SystemBarsVisibility.Gone)
	}

	fun hideSystemBar() {
		navigationBarVisibility = SystemBarsVisibility.Gone
		statusBarVisibility = SystemBarsVisibility.Gone

		onVisibilityChangedListener?.onSystemBarVisibilityChanged(SystemBarsVisibility.Gone)
	}

	fun setOnVisibilityChangedListener(listener: OnVisibilityChangedListener) {
		onVisibilityChangedListener = listener
	}

	interface OnVisibilityChangedListener {

		/**
		 * Called when navigation bar visibility changed
		 */
		fun onNavigationBarVisibilityChanged(visibility: SystemBarsVisibility)

		/**
		 * Called when status bar visibility changed
		 */
		fun onStatusBarVisibilityChanged(visibility: SystemBarsVisibility)

		/**
		 * Called when status bar and navigation bar visibility changed
		 */
		fun onSystemBarVisibilityChanged(visibility: SystemBarsVisibility)

	}

}
