package com.anafthdev.comdeo.ui.home

import androidx.lifecycle.SavedStateHandle
import com.anafthdev.comdeo.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): BaseViewModel<HomeState, HomeAction>(
    savedStateHandle = savedStateHandle,
    defaultState = HomeState()
) {

    override fun onAction(action: HomeAction) {

    }
}