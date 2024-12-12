package com.example.heartogether.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heartogether.R
import com.example.heartogether.ui.theme.NotoSans
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onCard1Clicked: () -> Unit = {},
    onCard2Clicked: () -> Unit = {},
    userName: String = "Dat",
    onProfileButtonClicked: () -> Unit = {},
    onLogoutButtonClicked: () -> Unit = {},
    onSettingsButtonClicked: () -> Unit = {},
) {
    AppScaffoldWithDrawer(
        onProfileClicked = onProfileButtonClicked,
        onSettingsClicked = onSettingsButtonClicked,
        onLogoutClicked = onLogoutButtonClicked
    ) { onOpenDrawer ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onOpenDrawer) {
                    Image(
                        painter = painterResource(id = R.drawable.img_4),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Home",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontFamily = NotoSans,
                    modifier = Modifier.padding( end = 40.dp) // Thêm padding sang trái
                )

                Spacer(modifier = Modifier.weight(1f))
            }


            Spacer(modifier = Modifier.height(32.dp))

            // Greeting message
            Text(
                text = buildAnnotatedString {
                    append("Nice to meet you, ")
                    withStyle(style = SpanStyle(color = Color(0xFF9AD983))) {
                        append(userName)
                    }
                    withStyle(style = SpanStyle(color = Color(0xFF9AD983))) {
                        append("!")
                    }
                },
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Let’s workout today!",
                style = TextStyle(
                    fontSize = 24.sp,
                    color = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(30.dp))

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                LessonCard(
                    title = "Training Sign Language",
                    description = "Study sign language!",
                    backgroundColor = Color(0xFF9AD983),
                    imageRes = R.drawable.img,
                    onCardClicked = onCard1Clicked,
                )
                LessonCard(
                    title = "Training Your Pronunciation",
                    description = "Improve your pronunciation!",
                    backgroundColor = Color(0xFF9AD983),
                    imageRes = R.drawable.img_2,
                    onCardClicked = onCard2Clicked
                )
            }
        }
    }
}

@Composable
fun LessonCard(
    title: String,
    description: String,
    backgroundColor: Color,
    imageRes: Int,
    onCardClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp) // Adjust the height of the card to suit content
            .clickable { onCardClicked() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = "Lesson Icon",
                    Modifier.size(150.dp),
            )

            Spacer(modifier = Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis // Prevent text overflow
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis // Prevent text overflow
                )
            }
        }
    }
}



@Composable
fun AppScaffoldWithDrawer(
    onProfileClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
    content: @Composable (onOpenDrawer: () -> Unit) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(320.dp)
                    .background(Color.Transparent),
                shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
            ) {
                DrawerContent(
                    onProfileClicked = {
                        scope.launch { drawerState.close() }
                        onProfileClicked()
                    },
                    onSettingsClicked = {
                        scope.launch { drawerState.close() }
                        onSettingsClicked()
                    },
                    onLogoutClicked = {
                        scope.launch { drawerState.close() }
                        onLogoutClicked()
                    }
                )
            }
        }
    ) {
        content { scope.launch { drawerState.open() } }
    }
}

@Composable
fun DrawerContent(
    onProfileClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
//    userDataViewModel: UserDataViewModel = hiltViewModel()
) {
    val username = "dat"//userDataViewModel.userName.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_4), // Thay R.drawable.hip bằng ảnh profile
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)

            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = username,
                    style = androidx.compose.ui.text.TextStyle(
                        fontFamily = NotoSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
                Text(
                    text = "Tài khoản miễn phí",
                    style = androidx.compose.ui.text.TextStyle(
                        fontFamily = NotoSans,
                        fontSize = 16.sp
                    ),
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Drawer Options
        TextButton(
            onClick = onProfileClicked
        ) {
            Row {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Hồ sơ",
                    style = androidx.compose.ui.text.TextStyle(
                        fontFamily = NotoSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = Color.Black
                )
            }
        }

        TextButton(
            onClick = onLogoutClicked
        ) {
            Row {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Logout",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Đăng xuất",
                    style = androidx.compose.ui.text.TextStyle(
                        fontFamily = NotoSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = Color.Black
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
