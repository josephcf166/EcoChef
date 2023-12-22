package com.example.ecochef.bottomnav

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ecochef.screens.IngredientsScreen
import com.example.ecochef.screens.ProfileScreen
import com.example.ecochef.screens.SearchScreen
import com.example.ecochef.R

@Composable
fun BottomNavigationBar(componentActivity: ComponentActivity) {
    BottomNavigation {
        val navController = rememberNavController()

        val items = listOf(
            BottomNavItem.Search,
            BottomNavItem.Profile,
            BottomNavItem.Ingredients
        )
        Scaffold(
            bottomBar = {
                BottomNavigation {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    items.forEach { screen ->
                        BottomNavigationItem(
                            modifier = Modifier
                                .background(colorResource(id = R.color.dark_green)), // color = nav bg color
                            icon = { Icon(painter = painterResource(screen.icon), "yes") },
                            label = { Text(text=screen.title,color= colorResource(id = R.color.mint_white)) }, // colour = nav text color
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
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
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(navController = navController, startDestination = "profile",modifier = Modifier
                .padding(innerPadding)) {
                composable("profile") { ProfileScreen(componentActivity) }
                composable("search") { SearchScreen(componentActivity) }
                composable("ingredients") { IngredientsScreen(componentActivity) }
            }

        }
    }
}