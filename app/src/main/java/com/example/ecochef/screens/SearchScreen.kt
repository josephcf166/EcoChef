package com.example.ecochef.screens

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.datastore.preferences.core.Preferences
import coil.compose.AsyncImage
import com.example.ecochef.R
import com.example.ecochef.getIngredientsList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import kotlin.math.round

val searchCache = HashMap<String, List<Recipe>>()

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(componentActivity: ComponentActivity){

    var recipeSelectedHandler = remember { mutableStateOf<Boolean>(false) }
    var recipeSelected = remember { mutableStateOf<Recipe?>(null) }
    var recipes = remember { mutableStateOf<Map<Int, List<Recipe>>>(emptyMap()) }

    var page = remember { mutableIntStateOf(1) }
    var isLoading by remember { mutableStateOf(false) }
    var loadingRecipes by remember { mutableStateOf(true) }


    var ingredientNames = ArrayList<String>()
    for (ingredient in getIngredientsList()) {
        val prefs = componentActivity.getSharedPreferences("ingredients", Context.MODE_PRIVATE)
        val selected = prefs.getBoolean(ingredient.Iname, false)
        if (selected) {
            ingredientNames.add(ingredient.Iname)
        }
    }

    var urlString = "https://www.bbc.co.uk/food/search?q="
    ingredientNames.forEachIndexed { index, ingredient ->
        urlString += if (index == ingredientNames.size - 1) {
            ingredient
        } else {
            "$ingredient%2C+"
        }
    }
    var pref = loadPrefSelection(componentActivity)
    var allergies = loadAllergySelections(componentActivity)
    urlString = if (pref != "no_preference") {
        Log.d("SearchDebug", "$pref")
        "$urlString&diets=$pref,"
    } else {
        "$urlString&diets="
    }
    allergies.forEachIndexed { index, allergy ->
        urlString += if (index == allergies.size - 1) {
            allergy
        } else {
            "$allergy,"
        }
    }
    urlString.dropLast(1)
    Log.d("SearchDebug", "$urlString")

    LoadNextRecipePage(urlString, page, recipes)

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

            for (p in 1..page.value){
                recipes.value[p]?.let { finalRecipes.addAll(it) }
            }

            if (!recipeSelectedHandler.value) {

                var text by remember { mutableStateOf("") }
                var active by remember { mutableStateOf(false) }
                var recentSearches = remember { mutableStateListOf<String>() }

                DockedSearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    query = text,
                    onQueryChange = { text = it },
                    onSearch = {
                        if(text.trim() != "") {
                            if (!recentSearches.contains(text)) {
                                if (recentSearches.size == 5) {
                                    recentSearches.remove(recentSearches.last())
                                }
                                recentSearches.add(0, text)
                            }
                        }
                        active = false },

                    active = active,
                    onActiveChange = { active = it },
                    placeholder = { Text("Recipe Search") },
                    leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                ) {
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
                }

                if(loadingRecipes){
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(top = round(LocalConfiguration.current.screenHeightDp / 2.5).dp)
                            .width(48.dp)
                            .align(Alignment.CenterHorizontally)
                            .fillMaxHeight()
                            .aspectRatio(1f)
                    )
                }

                if (finalRecipes.isNotEmpty()) {
                    loadingRecipes = false

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

                isLoading = false

                if(recipes.value.isNotEmpty()){

                    var buttonSize by remember { mutableStateOf(DpSize.Zero) }
                    val density = LocalDensity.current

                    if(recipes.value[page.value-1]?.isNotEmpty() == true){
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
                                LoadNextRecipePage(urlString, page, recipes)
                            } else {
                                Text(text = "More Recipes...")
                            }
                        }
                    }
                }
            }
            else {
                recipeScreen(recipe = recipeSelected.value)
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
fun LoadNextRecipePage(url: String, page: MutableState<Int>, recipes: MutableState<Map<Int, List<Recipe>>>){
    LaunchedEffect(Unit) {
        MainScope().launch {
            withContext(Dispatchers.IO) {
                var fetchedRecipes = recipes.value.toMutableMap()

                val searchURL = "${url}&page=${page.value}"

                val foundRecipes = searchCache.getOrPut(searchURL) {GetRecipe(searchURL)}
                fetchedRecipes[page.value] = foundRecipes
                page.value++

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

private fun loadPrefSelection(activity: ComponentActivity) : String {
    val sharedPref = activity.getSharedPreferences("myPref", Context.MODE_PRIVATE)
    val selectedOption = sharedPref.getString("selectedOption", "No Preferences")
    if (selectedOption != null) {
        return selectedOption.replace(" ", "_").lowercase()
    }
    return ""
}

private fun loadAllergySelections(activity: ComponentActivity): List<String> {
    val selectedAllergiesList = mutableListOf<String>()
    val sharedAllergy = activity.getSharedPreferences("myAllergy", Context.MODE_PRIVATE)

    // Retrieve all entries from the SharedPreferences
    val allPreferences = sharedAllergy.all

    // Iterate through the preferences and add selected options to the list then return the list of selected allergies
    for ((name, checked) in allPreferences) {
        if (checked is Boolean && checked) {
            val formattedName = name.replace(" ", "_").lowercase()
            selectedAllergiesList.add(formattedName)
        }
    }
    return selectedAllergiesList
}

