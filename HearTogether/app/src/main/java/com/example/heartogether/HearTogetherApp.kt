package com.example.heartogether

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
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
import com.example.heartogether.ui.home.CategoryScreen
import com.example.heartogether.ui.home.LessonsScreen
import com.example.heartogether.ui.home.MispronounceScreen
import com.example.heartogether.ui.speech.SpeechScreen

// Enum for screens
enum class HearTogetherScreen(@StringRes val title: Int, val iconRes: Int) {
    Home(title = R.string.home, iconRes = R.drawable.icons8_home_96),
    DictionarySign(title = R.string.dictionarysign, iconRes = R.drawable.icons8_dictionary_96),
    Lessons(title = R.string.lessons, iconRes = R.drawable.icons8_convert_96),
    Profile(title = R.string.profile, iconRes = R.drawable.ic_launcher_background),
    Login(title = R.string.login, iconRes = R.drawable.ic_launcher_background),
    Register(title = R.string.register, iconRes = R.drawable.ic_launcher_background),
    ForgotPassword(title = R.string.forgot_password, iconRes = R.drawable.ic_launcher_background),
    Verification(title = R.string.verification, iconRes = R.drawable.ic_launcher_background),
    NewPassword(title = R.string.new_password, iconRes = R.drawable.ic_launcher_background),
    Mispronounce(title = R.string.mispronounce, iconRes = R.drawable.ic_launcher_background),
    Category(title = R.string.category, iconRes = R.drawable.ic_launcher_background),
    Speech(title = R.string.speech, iconRes = R.drawable.icons8_convert_96)

}


// Main application composable
@Composable
fun HearTogetherApp(mService: AudioService?) {
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
                        },
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
                        onBackButtonClicked = { navController.popBackStack() }
                    )
                }
                composable(HearTogetherScreen.Home.name) {
                    HomeScreen(
                        onCard2Clicked =
                        {
                            navController.navigate(HearTogetherScreen.Mispronounce.name)
                        },
                        onLogoutButtonClicked =
                        {
                            navController.navigate(HearTogetherScreen.Login.name) }
                        ,
                        onCard1Clicked =
                        {
                            navController.navigate(HearTogetherScreen.Category.name)
                        },
                        onProfileButtonClicked = {
                            navController.navigate(HearTogetherScreen.Profile.name)
                        }
                    )
                }
                composable(HearTogetherScreen.Category.name){
                    CategoryScreen(
                        onBackClick = {navController.popBackStack()},
                        onCategoryClick = {navController.navigate(HearTogetherScreen.Lessons.name)}
                    )
                }
                composable(HearTogetherScreen.Lessons.name){
                    LessonsScreen(
                        onBackButtonClicked = {navController.popBackStack()},
                        onOptionSelected = {}
                    )
                }
                composable(HearTogetherScreen.DictionarySign.name) { SignDictionaryScreen() }
                composable(HearTogetherScreen.Mispronounce.name) {
                    MispronounceScreen(
                        onBackButtonClicked = { navController.popBackStack() },
                        mService = mService
                    )
                }
                composable(HearTogetherScreen.Speech.name) {
                    SpeechScreen()
                }
            }
        }
    }
}

private val bottomNavItems = listOf(
    HearTogetherScreen.Home,
    HearTogetherScreen.Speech,
    HearTogetherScreen.DictionarySign
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
                icon = {
                    Image(
                        painter = painterResource(id = screen.iconRes),
                        contentDescription = screen.name
                    )
                },
            )
        }
    }
}





