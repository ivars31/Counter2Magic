package com.example.counter2magic.ui.navigation

sealed class NavRoutes(val route: String) {
    data object Home : NavRoutes("home")
    data object AddEvent : NavRoutes("add_event")
    data object EventDetail : NavRoutes("event_detail/{eventId}") {
        fun createRoute(eventId: Long) = "event_detail/$eventId"
    }
    data object Settings : NavRoutes("settings")
}
