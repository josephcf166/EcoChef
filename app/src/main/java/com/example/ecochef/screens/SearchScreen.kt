package com.example.ecochef.screens

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import coil.compose.AsyncImage
import com.example.ecochef.R
import com.example.ecochef.getIngredientsList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

var page = 1
@Composable
fun SearchScreen(componentActivity: ComponentActivity){

    var recipeSelectedHandler = remember { mutableStateOf<Boolean>(false) }
    var recipeSelected = remember { mutableStateOf<Recipe?>(null) }
    var recipes = remember { mutableStateOf<Map<Int, List<Recipe>>>(emptyMap()) }

    var isLoading by remember { mutableStateOf(false) }

    var ingredientNames = ArrayList<String>()
    for (ingredient in getIngredientsList()) {
        val prefs = componentActivity.getSharedPreferences("ingredients", Context.MODE_PRIVATE)
        val selected = prefs.getBoolean(ingredient.Iname, false)
        if (selected) {
            ingredientNames.add(ingredient.Iname)
        }
    }

    var urlString = "https://www.bbc.co.uk/food/search?q="
    ingredientNames.forEach {
        urlString = "$urlString$it%2C+"
    }
    urlString = urlString.dropLast(4)
    Log.d("SearchDebug", "$urlString")

    LoadNextRecipePage(urlString, recipes)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.mint_white))
    ) {

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .background(colorResource(id = R.color.mint_white))
        ) {

            val finalRecipes = mutableListOf<Recipe>()

            for (p in 1..page){
                recipes.value[p]?.let { finalRecipes.addAll(it) }
            }


            if (!recipeSelectedHandler.value) {

                if (finalRecipes != null) {
                    for (i in finalRecipes.indices step 2) {

                        var nextRecipe: Recipe? = null

                        var r: Recipe = finalRecipes[i]
                        if (i + 1 in finalRecipes.indices) {
                            nextRecipe = finalRecipes[i + 1]
                        }

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier
                                .padding(start = 4.dp, end = 4.dp, top = 8.dp, bottom = 8.dp)
                                .fillMaxWidth()
                        ) {

                            RecipeItem(
                                recipe = r,
                                recipeSelectedHandler = recipeSelectedHandler,
                                recipeSelected = recipeSelected
                            )

                            if (nextRecipe != null) {
                                RecipeItem(
                                    recipe = nextRecipe,
                                    recipeSelectedHandler = recipeSelectedHandler,
                                    recipeSelected = recipeSelected
                                )
                            }
                        }
                    }
                }
                }
                else {
                    recipeScreen(recipe = recipeSelected.value)
                }

                isLoading = false

                if(recipes.value.isNotEmpty()){

                    var buttonSize by remember { mutableStateOf(DpSize.Zero) }
                    val density = LocalDensity.current

                    if(recipes.value[page-1]?.isNotEmpty() == true){
                        OutlinedButton(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .then(
                                    if (buttonSize != DpSize.Zero) Modifier.size(buttonSize) else Modifier
                                )
                                .onSizeChanged { newSize ->
                                    if (buttonSize == DpSize.Zero) {
                                        buttonSize = with(density) {
                                            newSize
                                                .toSize()
                                                .toDpSize()
                                        }
                                    }
                                },
                            onClick = { isLoading = isLoading.not() }) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .aspectRatio(1f)
                                )
                                LoadNextRecipePage(urlString, recipes)
                            } else {
                                Text(text = "More Recipes...")
                            }
                        }
                    }
                }
//
//              if(r.ingredients != null){
//                for(ing in r.ingredients){
//                    Text(modifier = Modifier.padding(top = 2.dp) ,text = ing)
//                }
//            }
//            if(r.subRecipes != null){
//                for (recipe in r.subRecipes){
//                    Text(modifier = Modifier.padding(top = 20.dp) ,text = recipe.name, fontWeight = FontWeight.Bold)
//                    if(recipe.ingredients != null){
//                        for(subIng in recipe.ingredients){
//                            Text(modifier = Modifier.padding(top = 2.dp) ,text = subIng)
//                        }
//                    }
//                }
//            }

        }
    }
}

data class Recipe(var name: String, val ingredients: List<String>?, val subRecipes: List<Recipe>?, val imageURL: String?)
@Composable
fun LoadNextRecipePage(url: String, recipes: MutableState<Map<Int, List<Recipe>>>){
    LaunchedEffect(Unit) {
        MainScope().launch {
            withContext(Dispatchers.IO) {
                var fetchedRecipes = recipes.value.toMutableMap()

                val foundRecipes = GetRecipe("${url}&page=$page")
                fetchedRecipes[page] = foundRecipes
                page++

                // Update the recipes on the main thread
                withContext(Dispatchers.Main) {
                    recipes.value = fetchedRecipes
                }
            }
        }
    }
}

@Composable
fun RecipeItem(recipe: Recipe, modifier: Modifier = Modifier, recipeSelectedHandler: MutableState<Boolean>, recipeSelected: MutableState<Recipe?>){
    Box(
        modifier = modifier
            .wrapContentSize()
            .width(LocalConfiguration.current.screenWidthDp.dp / 2 - 6.dp)
            .defaultMinSize(minHeight = LocalConfiguration.current.screenHeightDp.dp / 3f - 32.dp)
            .padding(start = 4.dp, end = 4.dp)
            .clip(shape = RoundedCornerShape(16.dp))
            .background(color = Color(0xFFE6E0E9))
            .clickable {
                recipeSelectedHandler.value = true
                recipeSelected.value = recipe
            },
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

fun GetRecipe(url: String): MutableList<Recipe> {
    var recipes: MutableList<Recipe> = mutableListOf<Recipe>()

    try {
        val document = Jsoup.connect(url).get()

        val recipeElements = document.select(".promo")

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

    return recipes
}