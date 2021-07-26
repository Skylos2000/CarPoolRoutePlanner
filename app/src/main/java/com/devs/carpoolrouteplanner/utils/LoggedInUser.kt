package com.devs.carpoolrouteplanner.utils

data class LoggedInUser(val uid:String = "",val name:String = "" )

data class LoginResult(val success:Boolean = false,val message:String = "No message", val user: LoggedInUser = LoggedInUser())
