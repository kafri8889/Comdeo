package com.anafthdev.comdeo.ui.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeState(
    val s: String = ""
): Parcelable
