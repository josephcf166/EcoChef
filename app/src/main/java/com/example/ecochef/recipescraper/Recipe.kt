package com.example.ecochef.recipescraper

data class Recipe(var name: String, val ingredients: List<String>?, val subRecipes: List<Recipe>?, val imageURL: String?)
