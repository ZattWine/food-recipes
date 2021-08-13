package com.norm.foodrecipes.util

class Constants {
    companion object {
        // API
        const val BASE_URL = "https://api.spoonacular.com"
        const val BASE_IMAGE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"
        const val API_KEY = "9f64244b375c4b28a124a87204968217"

        // Bundle
        const val RECIPE_BUNDLE_KEY = "recipeBundle"

        // API query keys and default values
        const val QUERY_SEARCH = "query"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_NUMBER = "number"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        // Room Database
        const val DATABASE_NAME = "recipes_db"
        const val RECIPES_TABLE = "recipes"
        const val FAVORITE_RECIPES_TABLE = "favorite_recipes"

        // Bottom Sheet and Preferences
        const val DEFAULT_RECIPES_NUMBER = "50"
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "gluten free"

        const val PREFS_NAME = "food_recipes_prefs"
        const val PREFS_MEAL_TYPE = "mealType"
        const val PREFS_MEAL_TYPE_ID = "mealTypeId"
        const val PREFS_DIET_TYPE = "dietType"
        const val PREFS_DIET_TYPE_ID = "dietTypeId"
        const val PREFS_BACK_ONLINE = "backOnline"

    }
}