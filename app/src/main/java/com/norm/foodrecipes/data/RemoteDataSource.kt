package com.norm.foodrecipes.data

import com.norm.foodrecipes.data.network.FoodRecipesApi
import com.norm.foodrecipes.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

/**
 * Data source from remote.
 */
class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
) {
    /**
     * Get all recipes via [FoodRecipesApi]
     */
    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesApi.getRecipes(queries)
    }
}