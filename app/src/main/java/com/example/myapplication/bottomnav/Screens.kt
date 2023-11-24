package com.example.myapplication.bottomnav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

@Composable
fun ProfileScreen(){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.teal_700))
            .wrapContentSize(Alignment.Center)
    ){
        Text(
            text = "Profile Screen",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 40.sp
        )
    }
}

@Composable
fun IngredientsScreen(){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.teal_700))
            .wrapContentSize(Alignment.Center)
    ){
        Text(
            text = "Ingredients Screen",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 40.sp
        )
    }
}

@Composable
fun SearchScreen(){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.teal_700))
            .wrapContentSize(Alignment.Center)
    ){
        Text(
            text = "Search Screen",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 40.sp
        )
    }
}