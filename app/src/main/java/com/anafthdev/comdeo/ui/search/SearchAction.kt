package com.anafthdev.comdeo.ui.search

sealed interface SearchAction {
    data class SetQuery(val query: String): SearchAction
}