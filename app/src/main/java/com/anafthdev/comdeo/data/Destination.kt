package com.anafthdev.comdeo.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import androidx.navigation.navArgument

object DestinationRoute {
	const val CHANGE_VIDEO_NAME = "change_video_name"
	const val VIDEO_INFO = "video_info"
	const val SEARCH = "search"
	const val VIDEO = "video"
	const val HOME = "home"
}

/**
 * Key for argument
 */
object DestinationArgument {
	const val ARG_VIDEO_ID = "video_id"
}

data class Destination(
	val route: String,
	val arguments: List<NamedNavArgument> = emptyList(),
	val deepLinks: List<NavDeepLink> = emptyList(),
	@StringRes val title: Int? = null,
	@StringRes val subtitle: Int? = null,
	@DrawableRes val icon: Int? = null
) {
	/**
	 * if you want to navigate to another screen with arguments, use this
	 * @param value {key: value}
	 */
	fun createRoute(vararg value: Pair<Any, Any?>): Destination {
		var mRoute = route

		value.forEach { (key, value) ->
			mRoute = mRoute.replace("{$key}", value.toString())
		}

		return Destination(mRoute, arguments)
	}

	companion object {
		/**
		 * if you want to create screen route with arguments, for example:
		 *```
		 * "$ROUTE?" +
		 * "$ARG_1={$ARG_1}&" +
		 * "$ARG_2={$ARG_2}"
		 * ```
		 *
		 * with [buildRoute]:
		 * ```
		 * Destination.buildRoute(
		 *     ROUTE,
		 *     ARG_1,
		 *     ARG_2
		 * )
		 * ```
		 */
		fun buildRoute(
			route: String,
			vararg args: String
		): String {
			return StringBuilder().apply {
				append("$route${if (args.isNotEmpty()) "?" else ""}")
				for (i in args.indices) {
					append("${args[i]}={${args[i]}}")
					if (i != args.lastIndex) append("&")
				}
			}.toString()
		}
	}
}

object Destinations {

	val home = Destination(
		route = DestinationRoute.HOME
	)

	val search = Destination(
		route = DestinationRoute.SEARCH
	)

	/**
	 * Required arguments:
	 * - [DestinationArgument.ARG_VIDEO_ID]
	 */
	val video = Destination(
		route = Destination.buildRoute(DestinationRoute.VIDEO, DestinationArgument.ARG_VIDEO_ID),
		arguments = listOf(
			navArgument(DestinationArgument.ARG_VIDEO_ID) {
				type = NavType.LongType
				defaultValue = -1
			}
		)
	)

	/**
	 * Required arguments:
	 * - [DestinationArgument.ARG_VIDEO_ID]
	 */
	val videoInfo = Destination(
		route = Destination.buildRoute(DestinationRoute.VIDEO_INFO, DestinationArgument.ARG_VIDEO_ID),
		arguments = listOf(
			navArgument(DestinationArgument.ARG_VIDEO_ID) {
				type = NavType.LongType
				defaultValue = -1
			}
		)
	)

	/**
	 * Required arguments:
	 * - [DestinationArgument.ARG_VIDEO_ID]
	 */
	val changeVideoName = Destination(
		route = Destination.buildRoute(DestinationRoute.CHANGE_VIDEO_NAME, DestinationArgument.ARG_VIDEO_ID),
		arguments = listOf(
			navArgument(DestinationArgument.ARG_VIDEO_ID) {
				type = NavType.LongType
				defaultValue = -1
			}
		)
	)
}
