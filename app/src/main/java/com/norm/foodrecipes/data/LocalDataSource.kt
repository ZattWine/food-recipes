package com.norm.foodrecipes.data

import com.norm.foodrecipes.data.database.RecipesDao
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
     * Insert all recipes from api to recipe table.
     */
    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDao.insertRecipes(recipesEntity)
    }

}