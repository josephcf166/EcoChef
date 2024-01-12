package com.example.ecochef.screens

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.currentCompositionLocalContext
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ComponentActivity
import coil.compose.AsyncImage
import com.example.ecochef.Ingredients
import com.example.ecochef.R
import com.example.ecochef.getIngredientsList
import com.google.android.material.search.SearchBar
import java.time.format.TextStyle
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun IngredientsScreen(componentActivity: ComponentActivity){
    Log.d("MyTag3", "vrever")
    var showDialog by remember { mutableStateOf(false) }
    var textInput by remember { mutableStateOf("") }
    var ingredients = getIngredientsList()
    val selectedIngredients: SharedPreferences = remember { componentActivity.getSharedPreferences("ingredients", Context.MODE_PRIVATE) }
    val customIngredients: SharedPreferences = remember { componentActivity.getSharedPreferences("custom", Context.MODE_PRIVATE) }
    val yourIngredients: MutableList<Ingredients> = remember { mutableStateListOf<Ingredients>() }
    val allIngredients: MutableList<Ingredients> = remember { mutableStateListOf<Ingredients>() }
    var searchedIngredients: MutableList<Ingredients> = remember { mutableStateListOf<Ingredients>() }
    val mutableList = mutableListOf<String>()
    LaunchedEffect(key1 = Unit) {
        allIngredients.forEach() {
            mutableList.add(it.Iname)
        }
        for ((key, value) in selectedIngredients.all) {
            if (value == true) {
                if (key in mutableList) {
                    ingredients.find { it.Iname == key }?.let { ingredient ->
                        if (ingredient !in yourIngredients) {
                            yourIngredients.add(ingredient)
                        }
                    }
                }
                else {
                    yourIngredients.add(Ingredients(key, custom=true))
                }
            }
        }

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
            .background(colorResource(id = R.color.ash_grey))
            .wrapContentSize(Alignment.Center)
    ){
        /*
        Row {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Filter ingredients") },
                modifier = Modifier.padding(start=8.dp).width(round(LocalConfiguration.current.screenWidthDp * 0.85).dp)
            )
            Button(onClick = { searchQuery = "" }, colors = ButtonDefaults.buttonColors(Color.Transparent)) {
                Text(text = "X", style = androidx.compose.ui.text.TextStyle(fontSize = 18.sp, color = Color.Black))
            }
        }*/
        var text by remember { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current

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

                if (text == "") {
                    keyboardController?.hide()
                }
                            },
            onSearch = { keyboardController?.hide() },

            active = false,
            onActiveChange = {  },
            placeholder = { Text("Ingredient Search", color=Color.Black) },
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
        ) {
            /*
            recentSearches.forEach {
                Row (
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .height(50.dp)
                ) {
                    Icon(modifier = Modifier.padding(all = 7.dp),
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "History Icon")
                    Text(text = it, modifier = Modifier.padding(all = 7.dp))
                }
            }
            */

        }
        Text(
            text = "Your Ingredients",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 30.sp, color=Color.Black
        )
        Divider(color = colorResource(R.color.black),
            thickness = 1.dp,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 5.dp))
        LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(3), modifier = Modifier.heightIn(min=0.dp, max=120.dp), content = {items(yourIngredients) {
            ingredient ->
            ingredientCard(
                ingredient = ingredient,
                selectedIngredients = selectedIngredients,
                componentActivity = componentActivity,
                yourIngredients = yourIngredients,
                allIngredients = allIngredients,
                searchedIngredients = searchedIngredients,
                isAll = false
            )
        }})
        Text(
            text = "All Ingredients",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 30.sp, color=Color.Black
        )
        Divider(color = colorResource(R.color.black),
            thickness = 1.dp,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 5.dp))

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
            ){
                Text("+ Ingredient", style = androidx.compose.ui.text.TextStyle(fontSize = 18.sp, color = Color.Black), modifier = Modifier.padding(0.dp))
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
                            fontSize = 23.sp, color=Color.Black
                        )
                        TextField(
                            value = textInput,
                            onValueChange = { textInput = it },
                            label = { Text("Ingredient name", color=Color.Black) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Button(onClick = {
                                allIngredients.add(0, Ingredients(textInput, custom = true))
                                searchedIngredients.add(0,Ingredients(textInput, custom = true))
                                showDialog = false
                                updateCustomIngredients(componentActivity, textInput)
                            }, modifier = Modifier.padding(end=8.dp)) {
                                Text("Add", color=Color.Black)
                            }
                            Button(onClick = { showDialog = false }) {
                                Text("Close", color=Color.Black)
                            }
                        }
                    }
                }
            }
        }

        LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(3), content = {items(searchedIngredients) {
                ingredient ->
            ingredientCard(ingredient = ingredient, selectedIngredients = selectedIngredients, componentActivity = componentActivity, yourIngredients = yourIngredients, allIngredients, searchedIngredients,true)
        } })



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
    yourIngredients: MutableList<Ingredients>,
    allIngredients: MutableList<Ingredients>,
    searchedIngredients: MutableList<Ingredients>,
    isAll: Boolean,
) {
    val selected =
        remember { mutableStateOf(selectedIngredients.getBoolean(ingredient.Iname, false)) }
    Log.d("MyTag", "your: $isAll - ${ingredient.Iname}: ${selectedIngredients.getBoolean(ingredient.Iname, false)}")
    selected.value = selectedIngredients.getBoolean(ingredient.Iname, false)
    Card(modifier = Modifier.padding(5.dp),
        colors = cardColors(
            containerColor = if (isAll) {
                when {
                    selected.value -> colorResource(id = R.color.lime_green)
                    else -> Color(0xFFE6E0E9)
                }
            }
            else {
                colorResource(id = R.color.lime_green)
            }
        ),
        border = BorderStroke(

            if (isAll) {
                when {
                    selected.value -> 5.dp
                    else -> 0.dp
                }
                       }
            else {
                 5.dp
                 }
            ,if (isAll){
                when {
                    selected.value -> colorResource(id = R.color.spotify_green)
                    else -> colorResource(id = R.color.mint_white)
                }
            }
            else{
                colorResource(id = R.color.spotify_green)
            }
        ),

        onClick = {

            selected.value = !selected.value
            Log.d("MyTag", "$isAll ${ingredient.Iname}: ${selected.value}")
            if (selected.value && ingredient !in yourIngredients) {
                yourIngredients.add(ingredient)
                updateIngredients(activity = componentActivity, ingredient.Iname, selected.value)
            }
            else {//if (ingredient in yourIngredients && !selected.value) {
                yourIngredients.remove(ingredient)
                updateIngredients(activity = componentActivity, ingredient.Iname, selected.value)
            }
        }) {

        if (isAll && ingredient.custom) {
            Button(onClick = {
                allIngredients.remove(ingredient)
                searchedIngredients.remove(ingredient)
                yourIngredients.remove(ingredient)
                removeCustom(activity = componentActivity, ingredient.Iname)
            },
                modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("remove", color=Color.Black)
            }
        }
        
        if (isAll) {
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
        }

        Text(
            text = ingredient.Iname,
            fontSize = 15.sp,
            fontStyle = FontStyle(600),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 10.dp, top = 10.dp), color=Color.Black
        )

    }
}