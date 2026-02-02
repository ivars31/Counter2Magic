package com.example.counter2magic.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.counter2magic.ui.screens.addevent.AddEventScreen
import com.example.counter2magic.ui.screens.eventdetail.EventDetailScreen
import com.example.counter2magic.ui.screens.home.HomeScreen
import com.example.counter2magic.ui.screens.settings.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route,
        modifier = modifier
    ) {
        composable(NavRoutes.Home.route) {
            HomeScreen(
                onAddEvent = {
                    navController.navigate(NavRoutes.AddEvent.route)
                },
                onEventClick = { eventId ->
                    navController.navigate(NavRoutes.EventDetail.createRoute(eventId))
                },
                onSettingsClick = {
                    navController.navigate(NavRoutes.Settings.route)
                }
            )
        }

        composable(NavRoutes.AddEvent.route) {
            AddEventScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onEventCreated = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = NavRoutes.EventDetail.route,
            arguments = listOf(
                navArgument("eventId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getLong("eventId") ?: return@composable
            EventDetailScreen(
                eventId = eventId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onEventDeleted = {
                    navController.popBackStack()
                }
            )
        }

        composable(NavRoutes.Settings.route) {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
