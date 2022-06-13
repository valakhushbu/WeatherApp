package com.example.weatherui.util

sealed class AuthState<T>(val data: T? =null,val message:String? = null){
    class Loading<T> : AuthState<T>()
    class Success<T>(data: T? = null) : AuthState<T>(data = data)
    class AuthError<T>(message: String? = null) : AuthState<T>(message = message)
}
