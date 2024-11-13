package com.example.cenitapp.data.model

sealed class UserState {
    object Loading : UserState()
    object ClearError : UserState()
    object NotAuthenticated : UserState()
    object Success : UserState()
    object Error : UserState()
    object Authenticated : UserState()
}