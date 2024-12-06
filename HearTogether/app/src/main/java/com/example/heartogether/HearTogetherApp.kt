package com.example.heartogether

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.heartogether.ui.screen.home.HomeScreen
import com.example.heartogether.ui.screen.signdictionary.SignDictionaryScreen

// Enum for screens
enum class HearTogetherScreen(@StringRes val title: Int, val icon: ImageVector) {
    Home(title = R.string.home, icon = Icons.Filled.Home),
    DictionarySign(title = R.string.dictionarysign, icon = Icons.Filled.Search),
    Lessons(title = R.string.lessons, icon = Icons.Filled.AccountBox),
    Profile(title = R.string.profile, icon = Icons.Filled.AccountCircle)
}

// Main application composable
@Composable
fun HearTogetherApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                items = HearTogetherScreen.values().toList()
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = HearTogetherScreen.Home.name
            ) {
                composable(HearTogetherScreen.Home.name) { HomeScreen() }
                composable(HearTogetherScreen.DictionarySign.name) { SignDictionaryScreen() }
                composable(HearTogetherScreen.Lessons.name) { LessonsScreen() }
                composable(HearTogetherScreen.Profile.name) { ProfileScreen() }
            }
        }
    }
}

// Bottom navigation bar composable
@Composable
fun BottomNavigationBar(
    navController: androidx.navigation.NavController,
    items: List<HearTogetherScreen>
) {
    val currentDestination by navController.currentBackStackEntryAsState()
    val selectedRoute = currentDestination?.destination?.route

    NavigationBar(containerColor = Color.White) {
        items.forEach { screen ->
            NavigationBarItem(
                selected = selectedRoute == screen.name,
                onClick = {
                    navController.navigate(screen.name) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(screen.icon, contentDescription = screen.name) },
            )
        }
    }
}

// Sample screens


@Composable
fun DictionarySignScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Dictionary Sign Screen", modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun LessonsScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Lessons Screen", modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun ProfileScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Profile Screen", modifier = Modifier.fillMaxSize())
    }
}
