package com.example.ecochef.recipescraper

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import com.example.ecochef.getIngredientsList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

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

            val subRecipes = mutableListOf<SubRecipe>()

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

                    subRecipes.add(SubRecipe(subRecipeName, subIngredients))

                    subRecipeName = ""
                }
            }

            var recipeInstructionsDoc = recipeDocument.select("ol.recipe-method__list")
            var recipeInstructions = recipeInstructionsDoc[0].children().map {
                it.text()
            }

            recipes.add(
                Recipe(
                    name = recipeDocument.select("h1.content-title__text").text(),
                    ingredients = ingredients,
                    instructions = recipeInstructions,
                    description = recipeDocument.select("p.recipe-description__text").text(),
                    prepTime = recipeDocument.select("p.recipe-metadata__prep-time").eachText()[0],
                    cookTime = recipeDocument.select("p.recipe-metadata__cook-time").eachText()[0],
                    numOfServings = recipeDocument.select("p.recipe-metadata__serving").eachText()[0],
                    subRecipes = subRecipes,
                    imageURL = recipeImage
                ))
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }

    return recipes
}

val searchCache = HashMap<String, List<Recipe>>()
@Composable
fun LoadNextRecipePage(url: String, page: MutableState<Int>, recipes: MutableState<Map<Int, List<Recipe>>>, loading: MutableState<Boolean>? = null){
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
                    if(loading != null){
                        loading.value = false
                    }
                }
            }
        }
    }
}


private fun loadPrefSelection(activity: ComponentActivity) : String {
    val sharedPref = activity.getSharedPreferences("myPref", Context.MODE_PRIVATE)
    val selectedOption = sharedPref.getString("selectedOption", "No Preference")
    if (selectedOption != null) {
        return selectedOption.replace(" ", "_").lowercase()
    }
    return ""
}

private fun loadAllergySelections(componentActivity: ComponentActivity): List<String> {
    val selectedAllergiesList = mutableListOf<String>()
    val sharedAllergy = componentActivity.getSharedPreferences("myAllergy", Context.MODE_PRIVATE)

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

private fun loadIngredients(componentActivity: ComponentActivity) : List<String> {
    var ingredientNames = ArrayList<String>()
    for (ingredient in getIngredientsList()) {
        val prefs = componentActivity.getSharedPreferences("ingredients", Context.MODE_PRIVATE)
        val selected = prefs.getBoolean(ingredient.Iname, false)
        if (selected) {
            ingredientNames.add(ingredient.Iname)
        }
    }
    return ingredientNames
}

fun getFinalSearchURL(componentActivity: ComponentActivity): String{
    var urlString = "https://www.bbc.co.uk/food/search?q="

    var ingredientNames = loadIngredients(componentActivity)
    var pref = loadPrefSelection(componentActivity)
    var allergies = loadAllergySelections(componentActivity)

    // add all the ingredients to the url
    ingredientNames.forEachIndexed { index, ingredient ->
        urlString += if (index == ingredientNames.size - 1) {
            ingredient
        } else {
            "$ingredient%2C+"
        }
    }

    // add all the preferences to the url
    urlString = if (pref != "no_preference" && pref != "") {
        Log.d("SearchDebug", "$pref")
        "$urlString&diets=$pref,"
    } else {
        "$urlString&diets="
    }

    // add all the allergies to the url
    allergies.forEachIndexed { index, allergy ->
        urlString += if (index == allergies.size - 1) {
            allergy
        } else {
            "$allergy,"
        }
    }

    Log.d("SearchDebug", "$urlString")

    return urlString
}