package com.example.ecochef

import com.example.ecochef.R

sealed class Ingredient (var name:String, var imageID:Int){
    object salmon : Ingredient("salmon", R.drawable.architecture)
    object bread : Ingredient("bread", R.drawable.business)
    object chicken : Ingredient("chicken", R.drawable.crafts)
}
