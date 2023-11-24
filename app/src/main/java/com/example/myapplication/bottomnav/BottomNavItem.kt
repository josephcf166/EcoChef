package com.example.myapplication.bottomnav

import com.example.myapplication.R

sealed class BottomNavItem(var title:String, var icon:Int, var route:String){
    object Search : BottomNavItem("Search", R.drawable.search_icon, "search")
    object Ingredients : BottomNavItem("Ingredients", R.drawable.carrot_icon, "ingredients")
    object Profile : BottomNavItem("Profile", R.drawable.profile_icon, "profile")
}

