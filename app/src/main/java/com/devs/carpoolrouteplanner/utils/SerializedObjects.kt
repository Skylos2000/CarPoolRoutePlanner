package com.devs.carpoolrouteplanner.utils

import kotlinx.serialization.Serializable

/*
 * This file is to contain all the Serializable objects used to communicate between the frontend and backend.
 * It is meant to be copied between both repositories.
 */

@Serializable
data class SerializedUser(val userId: Int, val email: String, val username: String, val groups: List<SerializedGroup>)

@Serializable
data class SerializedGroup(val gid: Int, val label: String, val groupLeaderUid: Int)

@Serializable
data class GroupDestination(val destinationId: Int, val gid: Int, val lat: Double, val long: Double, val label: String, var orderNum: Int)
