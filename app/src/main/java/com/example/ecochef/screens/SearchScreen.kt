package com.example.ecochef.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import androidx.core.app.ComponentActivity
import com.example.ecochef.R
import com.example.ecochef.ingredients

@Composable
fun SearchScreen(componentActivity: ComponentActivity){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.teal_700))
            .wrapContentSize(Alignment.Center)
    ){
        var ingredientNames = ArrayList<String>()
        for (ingredient in ingredients) {
            val prefs = componentActivity.getSharedPreferences("ingredients", Context.MODE_PRIVATE)
            val selected = prefs.getBoolean(ingredient.name, false)
            if (selected) {
                ingredientNames.add(ingredient.name)
            }
        }
        Text(
            text = "Search Screen",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 40.sp
        )
        Text(ingredientNames.toString())
    }
}
