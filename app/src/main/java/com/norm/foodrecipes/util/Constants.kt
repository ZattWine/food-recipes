package com.norm.foodrecipes.util

class Constants {
    companion object {
        // API
        const val BASE_URL = "https://api.spoonacular.com"
        const val API_KEY = "9f64244b375c4b28a124a87204968217"

        const val QUERY_API_KEY = "apiKey"

        const val QUERY_NUMBER = "number"
        const val DEFAULT_QUERY_NUMBER = "50"

        const val QUERY_TYPE = "type"
        const val DEFAULT_QUERY_TYPE = "snack"

        const val QUERY_DIET = "diet"
        const val DEFAULT_QUERY_DIET = "vegan"

        const val QUERY_RECIPE_INFORMATION = "addRecipeInformation"
        const val DEFAULT_QUERY_RECIPE_INFORMATION = "true"

        const val QUERY_FILL_INGREDIENTS = "fillIngredients"
        const val DEFAULT_QUERY_FILL_INGREDIENTS = "true"
    }
}