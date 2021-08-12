package com.norm.foodrecipes.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.norm.foodrecipes.util.Constants

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    /** queries preparation to apply on recipes api request */
    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[Constants.QUERY_NUMBER] = Constants.DEFAULT_QUERY_NUMBER
        queries[Constants.QUERY_API_KEY] = Constants.API_KEY
        queries[Constants.QUERY_TYPE] = Constants.DEFAULT_QUERY_TYPE
        queries[Constants.QUERY_DIET] = Constants.DEFAULT_QUERY_DIET
        queries[Constants.QUERY_RECIPE_INFORMATION] = Constants.DEFAULT_QUERY_RECIPE_INFORMATION
        queries[Constants.QUERY_FILL_INGREDIENTS] = Constants.DEFAULT_QUERY_FILL_INGREDIENTS

        return queries
    }

}