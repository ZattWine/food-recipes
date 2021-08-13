package com.norm.foodrecipes.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.norm.foodrecipes.models.Result
import com.norm.foodrecipes.util.Constants.Companion.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)