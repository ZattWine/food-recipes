package com.norm.foodrecipes.data

import com.norm.foodrecipes.data.database.RecipesDao
import com.norm.foodrecipes.data.database.entities.FavoritesEntity
import com.norm.foodrecipes.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
) {

    /**
     * Read all data from recipes table using kotlinx flow.
     */
    fun readRecipes(): Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }

    /**
     * Read all favorite recipes.
     */
    fun readFavoriteRecipes(): Flow<List<FavoritesEntity>> {
        return recipesDao.readFavoriteRecipes()
    }

    /**
     * Insert all recipes from api to recipe table.
     */
    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDao.insertRecipes(recipesEntity)
    }

    /**
     * Insert favorite recipe.
     */
    suspend fun insertFavoriteRecipe(favoritesEntity: FavoritesEntity) {
        recipesDao.insertFavoriteRecipe(favoritesEntity)
    }

    /**
     * Delete favorite recipe.
     */
    suspend fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity) {
        recipesDao.deleteFavoriteRecipe(favoritesEntity)
    }

    /**
     * Delete all favorite recipes.
     */
    suspend fun deleteAllFavoriteRecipes() {
        recipesDao.deleteAllFavoriteRecipes()
    }

}