package com.devs.carpoolrouteplanner.utils

data class LoggedInUser(val uid:String = "",val name:String = "",val username:String = "",val password:String="" )

data class LoginResult(val success:Boolean = false,val message:String = "No message", val user: LoggedInUser = LoggedInUser())
