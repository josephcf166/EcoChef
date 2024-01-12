package com.example.ecochef.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ecochef.R
import com.example.ecochef.recipescraper.Recipe

@Composable
fun recipeScreen(recipe: Recipe?) {
    if (recipe == null) {
        Text(text = "No recipe information found", color = Color.Red)
    } else {
        Card(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize(),  // Adjusted to fillMaxSize for full screen
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE6E0E9))
        ) {
            Column(modifier = Modifier
                .padding(5.dp) // Padding inside the card
                .fillMaxSize()) {

                // Title Text
                Text(
                    text = recipe.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
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
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.placeholder),
                        contentDescription = "image description",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth() // Adjusted to fillMaxWidth
                            .padding(5.dp)
                    )
                }
                Row {
                    Column {
                        Text(
                            text = "\uD83D\uDD2APreparation time: overnight",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        // Cooking Time
                        Text(
                            text = "\uD83C\uDF73Cooking time: 2 hours",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    // Serves
                    Text(
                        text = "\uD83C\uDF7DServes: 6-8",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 8.dp).padding(start = 10.dp)
                    )
                }

                // Ingredients Text
                Text(
                    text = "Ingredients:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .align(Alignment.Start) // Align to start
                        .padding(8.dp)
                )

                // Description Text
                for (ingredient in recipe.ingredients!!) {
                    Text(
                        text = "• $ingredient",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .align(Alignment.Start) // Align to start
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}