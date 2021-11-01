package com.devs.carpoolrouteplanner.utils

data class LoginResult(val success: Boolean, val user: LoggedInUser?)
data class LoggedInUser(val uid: Int, val username: String)
