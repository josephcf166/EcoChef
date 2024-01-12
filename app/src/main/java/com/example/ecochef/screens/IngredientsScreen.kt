package com.example.ecochef.screens

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Divider
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ComponentActivity
import com.example.ecochef.Ingredients
import com.example.ecochef.R
import com.example.ecochef.getIngredientsList

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun IngredientsScreen(componentActivity: ComponentActivity, onRecipePage: MutableState<Boolean>){
    onRecipePage.value = false
    Log.d("MyTag3", "vrever")
    var showDialog by remember { mutableStateOf(false) }
    var isAll by remember { mutableStateOf(true) }
    var textInput by remember { mutableStateOf("") }
    var ingredients = getIngredientsList()
    val selectedIngredients: SharedPreferences = remember { componentActivity.getSharedPreferences("ingredients", Context.MODE_PRIVATE) }
    val customIngredients: SharedPreferences = remember { componentActivity.getSharedPreferences("custom", Context.MODE_PRIVATE) }
    val yourIngredients: MutableList<Ingredients> = remember { mutableStateListOf<Ingredients>() }
    val allIngredients: MutableList<Ingredients> = remember { mutableStateListOf<Ingredients>() }
    var searchedIngredients: MutableList<Ingredients> = remember { mutableStateListOf<Ingredients>() }
    val mutableList = mutableListOf<String>()
    LaunchedEffect(key1 = Unit) {

        for ((key, value) in customIngredients.all) {
            val ing = Ingredients(key, custom = true)
            allIngredients.add(ing)
            searchedIngredients.add(ing)
        }

        for (ing in ingredients) {
            allIngredients.add(ing)
            searchedIngredients.add(ing)
        }
    }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.mint_white))
            //.wrapContentSize(Alignment.Center)
    ){
        var text by remember { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
            if (isAll){
            DockedSearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                query = text,
                onQueryChange = {

                    text = it

                    searchedIngredients.clear()
                    allIngredients.forEach() {
                        if (text.lowercase() in it.Iname.lowercase()) {
                            Log.d("lag4", "found")
                            searchedIngredients.add(it)
                        }
                    }

                    yourIngredients.clear()
                    allIngredients.forEach() {
                        if (text.lowercase() in it.Iname.lowercase()) {
                            Log.d("lag4", "found")
                            yourIngredients.add(it)
                        }
                    }

                    if (text == "") {
                        keyboardController?.hide()
                    }
                },
                onSearch = { keyboardController?.hide() },

                active = false,
                onActiveChange = { },
                placeholder = { Text("Ingredient Search", color = Color.Black) },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                trailingIcon = {
                    if (text.isNotEmpty()) {
                        IconButton(onClick = { text = "";keyboardController?.hide() }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = "Clear search"
                            )
                        }
                    }
                },
            ) {}
            }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Spacer to push the text to the center
            Spacer(modifier = Modifier.weight(1f))

            // Centered Text
            Text(
                text = if (isAll) "All Ingredients" else "Your Ingredients",
                fontSize = 30.sp,
                color = Color.Black
            )

            // Another Spacer to push the switch and text to the right
            Spacer(modifier = Modifier.weight(0.2f))

            // Switch and Text grouped together on the right
            Switch(
                checked = isAll,
                onCheckedChange = { isAll = !isAll },
                modifier = Modifier.height(10.dp)
            )
            Text(
                text = if (isAll) "All" else "Your",
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier.padding(end=7.dp)
            )
        }
        Divider(
            color = colorResource(R.color.black),
            thickness = 1.dp,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 5.dp)
        )
        if (!isAll) {
            yourIngredients.clear()
            allIngredients.forEach() {
                val selected =
                    remember { mutableStateOf(selectedIngredients.getBoolean(it.Iname, false)) }
                if (selected.value) {
                    yourIngredients.add(it)
                }
            }
            LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(3), content = {
                items(yourIngredients) { ingredient ->
                    ingredientCard(
                        ingredient = ingredient,
                        selectedIngredients = selectedIngredients,
                        componentActivity = componentActivity,
                        allIngredients,
                        searchedIngredients,
                        isAll,
                        yourIngredients
                    )
                }
            }

            )

        }
        else {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { showDialog = true },
                    // Set the button width to 66% of the screen's width
                    modifier = Modifier
                        .fillMaxWidth(0.66f)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(Color(colorResource(id = R.color.lime_green).toArgb()))
                ) {
                    Text(
                        "+ Ingredient",
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 18.sp,
                            color = Color.Black
                        ),
                        modifier = Modifier.padding(0.dp)
                    )
                }
            }

            if (showDialog) {
                Dialog(onDismissRequest = { showDialog = false }) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(R.color.ash_grey), // Use theme's surface color, or choose your own
                        shape = MaterialTheme.shapes.medium // Optional: Adds rounded corners
                    ) {
                        Column(
                            modifier = Modifier
                                .background(Color.White) // Set an opaque background color
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Add custom ingredient",
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(8.dp),
                                fontSize = 23.sp, color = Color.Black
                            )
                            TextField(
                                value = textInput,
                                onValueChange = { textInput = it },
                                label = { Text("Ingredient name", color = Color.Black) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                Button(onClick = {
                                    allIngredients.add(0, Ingredients(textInput, custom = true))
                                    searchedIngredients.add(
                                        0,
                                        Ingredients(textInput, custom = true)
                                    )
                                    showDialog = false
                                    updateCustomIngredients(componentActivity, textInput)
                                }, modifier = Modifier.padding(end = 8.dp)) {
                                    Text("Add", color = Color.Black)
                                }
                                Button(onClick = { showDialog = false }) {
                                    Text("Close", color = Color.Black)
                                }
                            }
                        }
                    }
                }
            }

            LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(3), content = {
                items(searchedIngredients) { ingredient ->
                    ingredientCard(
                        ingredient = ingredient,
                        selectedIngredients = selectedIngredients,
                        componentActivity = componentActivity,
                        allIngredients,
                        searchedIngredients,
                        isAll,
                        yourIngredients
                    )
                }
            })
        }



    }
}

fun updateIngredients (activity: ComponentActivity, name: String, boolean: Boolean) {
    val prefs = activity.getSharedPreferences("ingredients", Context.MODE_PRIVATE)
    val editor = prefs.edit()
    editor.putBoolean(name, boolean)
    editor.apply()
}

fun updateCustomIngredients (activity: ComponentActivity, name: String) {
    val prefs = activity.getSharedPreferences("custom", Context.MODE_PRIVATE)
    val editor = prefs.edit()
    editor.putString(name, name)
    editor.apply()
}

fun removeCustom(activity: ComponentActivity, name: String) {
    val prefs = activity.getSharedPreferences("custom", Context.MODE_PRIVATE)
    val editor = prefs.edit()
    editor.remove(name)
    editor.apply()
    val prefs1 = activity.getSharedPreferences("ingredients", Context.MODE_PRIVATE)
    val editor1 = prefs1.edit()
    editor1.remove(name)
    editor1.apply()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ingredientCard(
    ingredient: Ingredients,
    selectedIngredients: SharedPreferences,
    componentActivity: ComponentActivity,
    allIngredients: MutableList<Ingredients>,
    searchedIngredients: MutableList<Ingredients>,
    isAll : Boolean,
    yourIngredients: MutableList<Ingredients>
) {
    val selected =
        remember { mutableStateOf(selectedIngredients.getBoolean(ingredient.Iname, false)) }
    Log.d("MyTag",  "${ingredient.Iname}: ${selectedIngredients.getBoolean(ingredient.Iname, false)}")
    selected.value = selectedIngredients.getBoolean(ingredient.Iname, false)
    Card(modifier = Modifier.padding(5.dp),
        colors = cardColors(
            containerColor =
                when {
                    selected.value -> colorResource(id = R.color.lime_green)
                    else -> Color(0xFFE6E0E9)
                }
        ),
        border = BorderStroke(

                when {
                    selected.value -> 5.dp
                    else -> 0.dp
                }

            ,
                when {
                    selected.value -> colorResource(id = R.color.spotify_green)
                    else -> colorResource(id = R.color.mint_white)
                }
        ),

        onClick = {

            selected.value = !selected.value
            Log.d("MyTag", "${ingredient.Iname}: ${selected.value}")
            if (!selected.value && !isAll){
                yourIngredients.remove(ingredient)
            }
            updateIngredients(activity = componentActivity, ingredient.Iname, selected.value)
        }) {

        if (ingredient.custom) {
            Button(onClick = {
                yourIngredients.remove(ingredient)
                searchedIngredients.remove(ingredient)
                removeCustom(activity = componentActivity, ingredient.Iname)
            },
                modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("remove", color=Color.Black)
            }
        }
            Image(
                painter = painterResource(id = ingredient.imageInt),
                contentDescription = ingredient.Iname,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .padding(5.dp)
                    .size(120.dp)
                    .padding(bottom = 0.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

        Text(
            text = ingredient.Iname,
            fontSize = 15.sp,
            fontStyle = FontStyle(600),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp), color=Color.Black
        )

    }
}