package com.example.mobilecomputingfinalproject

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(state: MessageState,
               onEvent: (MessageEvent) -> Unit,
               context: Context
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.MainScreen.route){
        composable(
            route = Screen.MainScreen.route
        ){
            MainScreen(navController, state = state, onEvent = onEvent)
        }
        composable(
            route = Screen.CameraScreen.route
        ){
            CameraScreen(navController, onEvent = onEvent, context = context)
        }
    }

}