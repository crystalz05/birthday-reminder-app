package com.tyro.birthdayreminder.auth

sealed class UiEvent {
    data class ShowSnackBar(val message: String): UiEvent()
    data class Navigate(val route: String): UiEvent()
    object NavigateBack: UiEvent()
}