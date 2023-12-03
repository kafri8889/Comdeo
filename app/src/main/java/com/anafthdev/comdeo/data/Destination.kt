package com.anafthdev.comdeo.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink

object DestinationRoute {
    const val HOME = "home"
}

/**
 * Key for argument
 */
object DestinationArgument {
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
     * @param value {key: value}
     */
    fun createRoute(vararg value: Pair<Any, Any?>): Destination {
        var mRoute = route

        value.forEach { (key, value) ->
            mRoute = mRoute.replace("{$key}", value.toString())
        }

        return Destination(mRoute, arguments)
    }
}

object Destinations {

    val home = Destination(
        route = DestinationRoute.HOME
    )
}
