package com.anafthdev.comdeo.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.anafthdev.comdeo.R

enum class SortVideoBy {
    Name,
    DateAdded,
    Duration;

    val localizedName: String
        @Composable
        get() = when (this) {
            Name -> stringResource(R.string.by_name)
            DateAdded -> stringResource(R.string.by_date_added)
            Duration -> stringResource(R.string.by_duration)
        }
}