package com.example.ecochef.recipescraper

data class Recipe(var name: String, val ingredients: List<String>, val instructions: List<String>?, val description: String?, val prepTime: String, val cookTime: String, val numOfServings: String, val subRecipes: List<SubRecipe>?, val imageURL: String?)
data class SubRecipe(var name: String, val ingredients: List<String>?)
