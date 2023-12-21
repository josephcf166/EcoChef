package com.example.ecochef.screens

import android.annotation.SuppressLint
import android.content.Context
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ecochef.R
import com.example.ecochef.ingredients
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

@Composable
fun SearchScreen(componentActivity: ComponentActivity){
    val urls = mutableListOf<String>(
        "https://www.bbc.co.uk/food/search?q=bread",
        "https://www.bbc.co.uk/food/search?q=salmon",
    )

    var recipes by remember { mutableStateOf<List<Recipe>>(emptyList()) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        var ingredientNames = ArrayList<String>()
        for (ingredient in ingredients) {
            val prefs = componentActivity.getSharedPreferences("ingredients", Context.MODE_PRIVATE)
            val selected = prefs.getBoolean(ingredient.Iname, false)
            if (selected) {
                ingredientNames.add(ingredient.Iname)
            }
        }
        Text(
            text = "Search Screen",
            fontSize = 40.sp
        )
        Text(ingredientNames.toString())
    }

    Column (
        modifier = Modifier.verticalScroll(rememberScrollState())
                            .background(colorResource(id = R.color.teal_700))
    ) {
//                    IngredientButtons(ingredients)
        LaunchedEffect(Unit) {
            MainScope().launch {
                withContext(Dispatchers.IO) {
                    val fetchedRecipes = GetRecipe(urls)
                    // Update the recipes on the main thread
                    withContext(Dispatchers.Main) {
                        recipes = fetchedRecipes
                    }
                }
            }
        }

        for(i in recipes.indices step 2) {

            var nextRecipe: Recipe? = null

            var r: Recipe = recipes[i]
            if(i+1 in recipes.indices){
                nextRecipe = recipes[i+1]
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp, top = 8.dp, bottom = 8.dp)
                    .fillMaxWidth()
            ) {

                RecipeItem(recipe = r)

                if(nextRecipe != null) {
                    RecipeItem(recipe = nextRecipe)
                }
            }

//                            if(r.ingredients != null){
//                                for(ing in r.ingredients){
//                                    Text(modifier = Modifier.padding(top = 2.dp) ,text = ing)
//                                }
//                            }
//                            if(r.subRecipes != null){
//                                for (recipe in r.subRecipes){
//                                    Text(modifier = Modifier.padding(top = 20.dp) ,text = recipe.name, fontWeight = FontWeight.Bold)
//                                    if(recipe.ingredients != null){
//                                        for(subIng in recipe.ingredients){
//                                            Text(modifier = Modifier.padding(top = 2.dp) ,text = subIng)
//                                        }
//                                    }
//                                }
//                            }
        }
    }
}

data class Recipe(var name: String, val ingredients: List<String>?, val subRecipes: List<Recipe>?, val imageURL: String?)

@Composable
fun RecipeItem(recipe: Recipe, modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .wrapContentSize()
            .width(LocalConfiguration.current.screenWidthDp.dp / 2 - 6.dp)
            .defaultMinSize(minHeight = LocalConfiguration.current.screenHeightDp.dp / 3f - 32.dp)
            .padding(start = 4.dp, end = 4.dp)
            .clip(shape = RoundedCornerShape(16.dp))
            .background(color = Color(0xFFE6E0E9))
            .clickable { },
    ) {
        Column {

            if(recipe.imageURL != null){
                AsyncImage(
                    model = recipe.imageURL,
                    contentDescription = "Translated description of what the image contains"
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.placeholder),
                    contentDescription = "image description"
                )
            }

            Text(
                text = recipe.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                style = LocalTextStyle.current.merge(
                    TextStyle(
                        lineHeight = 20.sp
                    )
                ),
                modifier = Modifier
                    .padding(8.dp)
            )

            Text(
                text = "blah blah blah",
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize()
            )
        }
    }
}

fun GetRecipe(urls: List<String>): MutableList<Recipe> {
    var recipes: MutableList<Recipe> = mutableListOf<Recipe>()

    for (url in urls) {
        try {
            val document = Jsoup.connect(url).get()

            val recipeElements = document.select(".promo__main_course")

            for (element in recipeElements) {
                val recipeImage = element.selectFirst("div.promo__image")?.selectFirst("img")?.absUrl("data-src")
                val recipeURL = element.absUrl("href")
                val recipeDocument = Jsoup.connect(recipeURL).get()

                val ingredientsElements = recipeDocument.select("div.recipe-ingredients-wrapper")

                val subRecipes = mutableListOf<Recipe>()

                var subRecipeName = String()
                var ingredients = listOf<String>()

                for (elem in ingredientsElements[0].children()){
                    var subIngredients = listOf<String>()

                    if(elem.tagName().equals("h3")){
                        subRecipeName = elem.text()
                    }

                    if(elem.tagName().equals("ul") && subRecipeName == ""){
                        ingredients = elem.select("li")
                            .map { it.text() }
                    }

                    if(elem.tagName().equals("ul") && subRecipeName != ""){
                        subIngredients = elem.select("li")
                            .map { it.text() }

                        subRecipes.add(Recipe(subRecipeName, subIngredients, null, null))

                        subRecipeName = ""
                        subIngredients = listOf()
                    }
                }
                recipes.add(Recipe(recipeDocument.select("h1.content-title__text").text(), ingredients, subRecipes, recipeImage))
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    return recipes
}
