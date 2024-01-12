package com.example.ecochef.screens

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ecochef.R
import kotlin.math.round
import com.example.ecochef.recipescraper.LoadNextRecipePage
import com.example.ecochef.recipescraper.Recipe
import com.example.ecochef.recipescraper.getFinalSearchURL

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    componentActivity: ComponentActivity,
    navController: NavController,
    onRecipePage: MutableState<Boolean>
){

    var recipeSelectedHandler = remember { mutableStateOf<Boolean>(false) }
    var recipeSelected = remember { mutableStateOf<Recipe?>(null) }
    var recipes = remember { mutableStateOf<Map<Int, List<Recipe>>>(emptyMap()) }

    var page = remember { mutableIntStateOf(1) }
    var isLoading by remember { mutableStateOf(false) }
    var loadingRecipes = remember { mutableStateOf(true) }


    var urlString = getFinalSearchURL(componentActivity)

    LoadNextRecipePage(urlString, page, recipes, loadingRecipes)

    onRecipePage.value = false

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
            val allRecipes = mutableListOf<Recipe>()

            for (p in 1..page.value) {
                recipes.value[p]?.let { allRecipes.addAll(it) }
            }

            // Declare finalRecipes as a mutableStateOf
            var finalRecipes by remember { mutableStateOf(allRecipes) }

            if (!recipeSelectedHandler.value) {
                var text by remember { mutableStateOf("") }
                var active by remember { mutableStateOf(false) }
                var recentSearches = remember { mutableStateListOf<String>() }
                val keyboardController = LocalSoftwareKeyboardController.current
                if (text == ""){
                    finalRecipes = allRecipes
                }
                DockedSearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    query = text,
                    onQueryChange = { newText ->
                        text = newText
                        // Filter the list of recipes based on the entered text
                        val filteredRecipes: List<Recipe> = allRecipes.filter { recipe ->
                            recipe.name.contains(newText, ignoreCase = true)
                        }
                        finalRecipes = filteredRecipes.toMutableList()
                    },
                    onSearch = {
                        active = false
                        keyboardController?.hide()
                    },
                    active = false,
                    onActiveChange = { isActive ->
                        active = isActive
                        if (!isActive) {
                            // Clear the text and reset the filtered recipes when the search bar is deactivated
                            text = ""
                            finalRecipes = allRecipes
                        }
                    },
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


                if(loadingRecipes.value){
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(top = round(LocalConfiguration.current.screenHeightDp / 2.5).dp)
                            .width(48.dp)
                            .align(Alignment.CenterHorizontally)
                            .fillMaxHeight()
                            .aspectRatio(1f)
                    )
                }

                val roboto = FontFamily(
                    Font(R.font.roboto, FontWeight.Normal),
                    Font(R.font.roboto_bold, FontWeight.Bold),
                )

                if((finalRecipes.isEmpty()) and (!loadingRecipes.value)){
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = (LocalConfiguration.current.screenHeightDp / 3).dp),
                        text = "No Recipes Found...",
                        fontFamily = roboto,
                        fontSize = 24.sp,
                    )
                }

                if (finalRecipes.isNotEmpty()) {
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
                                .padding(12.dp)
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
                RecipeScreen(navController, recipeSelected.value, onRecipePage)
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
                    .padding(8.dp),
                color = Color.Black
            )

            Text(
                text = "blah blah blah",
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                color = Color.Black
            )
        }
    }
}
