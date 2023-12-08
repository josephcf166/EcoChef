package com.example.ecochef.bottomnav

import com.example.ecochef.R

sealed class BottomNavItem(var title:String, var icon:Int, var route:String){
    object Search : BottomNavItem("Search", R.drawable.search_icon, "search")
    object Ingredients : BottomNavItem("Ingredients", R.drawable.ingredients_icon, "ingredients")
    object Profile : BottomNavItem("Profile", R.drawable.profile_icon, "profile")
}

