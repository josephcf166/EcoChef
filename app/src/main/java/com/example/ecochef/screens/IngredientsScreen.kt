package com.example.ecochef.screens

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ComponentActivity
import coil.compose.AsyncImage
import com.example.ecochef.R
import com.example.ecochef.ingredients

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientsScreen(componentActivity: ComponentActivity){
    var ingredients = ingredients
    val prefs = componentActivity.getSharedPreferences("ingredients", Context.MODE_PRIVATE)
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.ash_grey))
            .wrapContentSize(Alignment.Center)
    ){
        Text(
            text = "Your Ingredients",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 40.sp
        )
        Divider(color = colorResource(R.color.black),
            thickness = 1.dp,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 5.dp))
        LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(3), content = {items(ingredients) {
                ingredient ->
            val selected = remember { mutableStateOf(prefs.getBoolean(ingredient.Iname, false)) }
            Card (modifier = Modifier.padding(5.dp),
                colors = cardColors( containerColor = when {
                    selected.value -> colorResource(id = R.color.lime_green)
                    else -> colorResource(id = R.color.mint_white)
                }),
                border = BorderStroke(when {
                        selected.value -> 5.dp
                        else -> 0.dp }, when{
                    selected.value -> colorResource(id = R.color.dark_green)
                    else -> colorResource(id = R.color.mint_white)
                }),
                onClick = {selected.value = selected.value != true}){
                AsyncImage(model = ingredient.imageLink,
                    contentDescription = ingredient.Iname,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .padding(5.dp)
                        .size(120.dp)
                        .padding(bottom = 0.dp)
                        .clip(RoundedCornerShape(8.dp)))

                Text(text = ingredient.Iname,
                    fontSize = 15.sp,
                    fontStyle = FontStyle(600),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 20.dp))
                updateIngredients(activity = componentActivity, ingredient.Iname, selected.value)
            }
        } })



    }
}

fun updateIngredients (activity: ComponentActivity, name: String, boolean: Boolean) {
    val prefs = activity.getSharedPreferences("ingredients", Context.MODE_PRIVATE)
    val editor = prefs.edit()
    editor.putBoolean(name, boolean)
    editor.apply()
}

fun Modifier.conditional(
    condition: Boolean,
    ifTrue: Modifier.() -> Modifier,
    ifFalse: (Modifier.() -> Modifier)? = null
): Modifier {
    return if (condition) {
        then(ifTrue(Modifier))
    } else if (ifFalse != null) {
        then(ifFalse(Modifier))
    } else {
        this
    }
}