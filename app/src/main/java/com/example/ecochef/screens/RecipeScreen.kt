package com.example.ecochef.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ecochef.R
import com.example.ecochef.recipescraper.Recipe

@Composable
fun RecipeScreen(navController: NavController, recipe: Recipe?, onRecipePage: MutableState<Boolean>) {
    onRecipePage.value = true
    if (recipe == null) {
        Text(text = "No recipe information found", color = Color.Red)
    } else {
        Surface(color = Color(colorResource(id = R.color.mint_white).toArgb())) {
            Column(
                modifier = Modifier
                    .padding(5.dp) // Padding inside the card
                    .fillMaxSize()
                    .background(color = Color(colorResource(id = R.color.mint_white).toArgb()))
            ) {

                // Title Text

                Text(
                    text = recipe.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = Color(colorResource(id = R.color.spotify_green).toArgb()),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                // Image Section
                if (recipe.imageURL != null) {
                    AsyncImage(
                        model = recipe.imageURL,
                        contentDescription = "Translated description of what the image contains",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth() // Adjusted to fillMaxWidth
                            .padding(5.dp)
                            .clip(
                                shape = RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = 16.dp,
                                    bottomEnd = 16.dp
                                )
                            )
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.placeholder),
                        contentDescription = "image description",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth() // Adjusted to fillMaxWidth
                            .padding(5.dp)
                            .clip(
                                shape = RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = 16.dp,
                                    bottomEnd = 16.dp
                                )
                            )
                    )
                }
                Row {
                    Column {
                        Text(
                            text = "\uD83D\uDD2APreparation time: overnight",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 8.dp),
                            color = Color.Black
                        )

                        // Cooking Time
                        Text(
                            text = "\uD83C\uDF73Cooking time: 2 hours",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 8.dp),
                            color = Color.Black
                        )
                    }

                    // Serves
                    Text(
                        text = "\uD83C\uDF7DServes: 6-8",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .padding(start = 10.dp),
                        color = Color.Black
                    )
                }

                Divider(color = colorResource(R.color.black),
                    thickness = 1.dp,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 22.dp, bottom = 0.dp))

                // Ingredients Text
                Text(
                    text = "Ingredients",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally) // Align to start
                        .padding(bottom = 10.dp, top = 0.dp),
                    color = Color(colorResource(id = R.color.spotify_green).toArgb()),
                    textAlign = TextAlign.Center
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .align(Alignment.CenterHorizontally),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE6E0E9))
                ) {
                    // Description Text
                    for (ingredient in recipe.ingredients!!) {
                        Text(
                            text = "• $ingredient",
                            fontSize = 16.sp,
                            modifier = Modifier
                                .align(Alignment.Start) // Align to start
                                .padding(8.dp),
                            color = Color.Black
                        )
                    }

                    for (subRecipe in recipe.subRecipes!!) {
                        Text(
                            text = "${subRecipe.name}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally) // Align to start
                                .padding(8.dp),
                            color = Color.Black
                        )
                        for (ingredient in subRecipe.ingredients!!) {
                            Text(
                                text = "• $ingredient",
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .align(Alignment.Start) // Align to start
                                    .padding(8.dp),
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}