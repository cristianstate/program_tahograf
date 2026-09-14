package com.fitness.myapplication.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable
    data object Dashboard : Destination

    @Serializable
    data object History : Destination

    @Serializable
    data class EditLog(val logId: Int? = null) : Destination
}
