package com.example.ecochef.topbar

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.ecochef.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTopBar(
    componentActivity: ComponentActivity,
    navController: NavHostController,
    onRecipePage: MutableState<Boolean>
) {

    val pacifico = FontFamily(
        Font(R.font.pacifico, FontWeight.Normal),
    )

    TopAppBar(
        modifier = Modifier
            .height(64.dp)
            .clip(shape = RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp)),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(colorResource(R.color.spotify_green).toArgb()),
            titleContentColor = Color(colorResource(R.color.white).toArgb()),
        ),
        title = {
            var ecoPadding = 0.dp
            var chefPadding = 32.dp

            if(onRecipePage.value) {
                Icon(
                    painter = painterResource(R.drawable.ecochef_logo_transparent),
                    contentDescription = "logo",
                    tint = Color.Unspecified
                )
                ecoPadding = 60.dp
                chefPadding = 92.dp
            }
            Text(
                modifier = Modifier
                    .padding(top = 8.dp, start = ecoPadding),
                text = "Eco",
                fontFamily = pacifico,
                color = Color(0xff6dc853)

            )
            Text(
                modifier = Modifier
                    .padding(top = 8.dp, start = chefPadding),
                text = "Chef",
                fontFamily = pacifico
            )
        },
        navigationIcon = {
            if(onRecipePage.value) {
                IconButton(
                    modifier = Modifier
                        .padding(top = 8.dp),

                    onClick = { navController.navigate("search")  }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back Button",
                        tint = Color.White
                    )
                }
            } else {
                Icon(
                    painter = painterResource(R.drawable.ecochef_logo_transparent),
                    contentDescription = "logo",
                    tint = Color.Unspecified
                )
            }
        },
        actions = {
            TopBarActions(navController)
        }

    )
}

@Composable
fun TopBarActions(navController: NavHostController) {
    GotoProfileScreen(navController)
}

@Composable
fun GotoProfileScreen(navController: NavHostController){
    IconButton(
        modifier = Modifier
            .padding(top=6.dp),
        onClick = {
            navController.navigate("profile"){
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }
        }
    ) {
        Icon(
            modifier = Modifier
                .size(32.dp),
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Profile Screen Button",
            tint = Color(colorResource(R.color.mint_white).toArgb())

        )
    }
}

