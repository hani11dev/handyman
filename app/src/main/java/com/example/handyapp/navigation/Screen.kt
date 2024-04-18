package com.example.handyapp.navigation

sealed class Screen(val route: String) {
 //   object Home: Screen(route = "home_screen/{id}")
    object Home: Screen(route = "home_screen")
    object Login: Screen(route = "login_screen")
    object Register: Screen(route = "register_screen")
    object RegisterInfo: Screen(route = "register_info_screen")
    object RegistrationSucceeded: Screen(route = "registration_succeeded_screen")
    object Waiting: Screen(route = "waiting_screen")
   object MyRequests: Screen(route = "My Requests")
   object DetailRequest: Screen(route = "Detail Request")
   object MyTasks: Screen(route = "My Tasks")
   object Notifications: Screen(route = "Notifications")
   object Settings: Screen(route = "settings")
    object Refused: Screen(route = "refused")
    object OneLastStepScreen: Screen(route = "one_last_step_screen")
    object FinalRegistrationScreen: Screen(route = "final_registration_screen")
    object FinishedSetupScreen: Screen(route = "finished_setup_screen")
    object BrowseJobsScreen: Screen (route = "jobs_screen")
    object JobsDetails: Screen (route = "jobs_details_screen")
    object BidScreen: Screen (route = "bid_screen")
    object ChatScreen: Screen (route = "chat_screen")
}