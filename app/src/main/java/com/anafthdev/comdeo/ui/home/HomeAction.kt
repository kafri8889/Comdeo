package com.anafthdev.comdeo.ui.home

sealed interface HomeAction {
    data class SortVideoBy(val sortVideoBy: com.anafthdev.comdeo.data.SortVideoBy): HomeAction
}