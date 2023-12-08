package com.example.ecochef.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecochef.Ingredient
import com.example.ecochef.R

@Composable
fun IngredientsScreen(){
    var ingredients = listOf<Ingredient>(Ingredient.salmon, Ingredient.bread, Ingredient.chicken)
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.teal_700))
            .wrapContentSize(Alignment.Center)
    ){
        Text(
            text = "Your Ingredients",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 40.sp
        )
        Divider(color = colorResource(R.color.black),
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 20.dp).padding( bottom = 5.dp))
        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 150.dp), content = {items(ingredients) {
                ingredient ->
            Card (modifier = Modifier.padding(5.dp) ){
                Image(painter = painterResource(id = ingredient.imageID),
                    contentDescription = ingredient.name,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.padding(5.dp).size(200.dp).padding(bottom = 0.dp))
                Text(text = ingredient.name,
                    fontSize = 20.sp,
                    fontStyle = FontStyle(700),
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 4.dp))
            }

        } })



    }
}