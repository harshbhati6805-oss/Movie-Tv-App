package com.harsh.tmdbtvapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.harsh.tmdbtvapp.uii.home.HomeScreen
import com.harsh.tmdbtvapp.movieViewModel.MovieViewModel
import com.harsh.tmdbtvapp.navigation.NavRoutes

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {


            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = NavRoutes.HOME
            ) {

                composable(NavRoutes.HOME) {

                    val movieVM: MovieViewModel = viewModel()

                    HomeScreen(viewModel = movieVM)
                }
            }
        }
    }
}