package com.example.heartogether

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.heartogether.profile.ProfileScreen
import com.example.heartogether.services.AudioService
import com.example.heartogether.ui.account.ForgotPasswordScreen
import com.example.heartogether.ui.account.LoginScreen
import com.example.heartogether.ui.account.NewPasswordScreen
import com.example.heartogether.ui.account.RegisterScreen
import com.example.heartogether.ui.account.VerificationScreen
import com.example.heartogether.ui.home.HomeScreen
import com.example.heartogether.ui.dictionary.SignDictionaryScreen
import com.example.heartogether.ui.home.MispronounceScreen

// Enum for screens
enum class HearTogetherScreen(@StringRes val title: Int, val icon: ImageVector) {
    Home(title = R.string.home, icon = Icons.Filled.Home),
    DictionarySign(title = R.string.dictionarysign, icon = Icons.Filled.Search),
    Lessons(title = R.string.lessons, icon = Icons.Filled.AccountBox),
    Profile(title = R.string.profile, icon = Icons.Filled.AccountCircle),
    Login(title = R.string.login, icon = Icons.Filled.AccountBox),
    Register(title = R.string.register, icon = Icons.Filled.AccountBox),
    ForgotPassword(title = R.string.forgot_password, icon = Icons.Default.Info),
    Verification(title = R.string.verification, icon = Icons.Default.Info),
    NewPassword(title = R.string.new_password, icon = Icons.Default.Lock),
    Mispronounce(title = R.string.mispronounce, icon = Icons.Default.Lock),
}

// Main application composable
@Composable
fun HearTogetherApp(mService : AudioService?) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                items = bottomNavItems // Sử dụng danh sách đã lọc
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
                composable(route = HearTogetherScreen.Login.name) {
                    LoginScreen(
                        onLoginButtonClicked = {
                            navController.navigate(HearTogetherScreen.Home.name)
                        },
                        onRegisterButtonClicked = {
                            navController.navigate(HearTogetherScreen.Register.name)
                        },
                        onForgotPasswordButtonClicked = {
                            navController.navigate(HearTogetherScreen.ForgotPassword.name)
                        }
                    )
                }
                composable(route = HearTogetherScreen.Register.name) {
                    RegisterScreen(
                        onRegisterButtonClicked = {
                            navController.navigate(HearTogetherScreen.Login.name)
                        },
                        onLoginButtonClicked = {
                            navController.navigate(HearTogetherScreen.Login.name)
                        }
                    )
                }
                composable(route = HearTogetherScreen.ForgotPassword.name) {
                    ForgotPasswordScreen(
                        onBackButtonClicked = { navController.popBackStack() },
                        onVerifyButtonClicked = {
                            navController.navigate(HearTogetherScreen.Login.name)
                        },
                        onRegisterButtonClicked = {
                            navController.navigate(HearTogetherScreen.Register.name)
                        }
                    )
                }
                composable(route = HearTogetherScreen.Verification.name) {
                    VerificationScreen(
                        onBackButtonClicked = { navController.popBackStack() },
                        onVerifyCodeButtonClicked = {
                            navController.navigate(HearTogetherScreen.NewPassword.name)
                        },
                        onResendButtonClicked = { /* Handle resend code button click */ }
                    )
                }
                composable(route = HearTogetherScreen.NewPassword.name) {
                    NewPasswordScreen(
                        onVerifyButtonClicked = {
                            navController.navigate(HearTogetherScreen.Login.name)
                        }
                    )
                }
                composable(route = HearTogetherScreen.Profile.name) {
                    ProfileScreen(
                        onBackButtonClicked = {navController.popBackStack()}
                    )
                }
                composable(HearTogetherScreen.Home.name) { HomeScreen(onCardClicked = { navController.navigate(HearTogetherScreen.Mispronounce.name) }) }
                composable(HearTogetherScreen.DictionarySign.name) { SignDictionaryScreen() }
                composable(HearTogetherScreen.Mispronounce.name) {
                    MispronounceScreen(
                    onBackButtonClicked = {navController.popBackStack()},
                    mService = mService
                ) }
            }
        }
    }
}
private val bottomNavItems = listOf(
    HearTogetherScreen.Home,
    HearTogetherScreen.DictionarySign,
    HearTogetherScreen.Lessons
)
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
fun LessonsScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Lessons Screen", modifier = Modifier.fillMaxSize())
    }
}


