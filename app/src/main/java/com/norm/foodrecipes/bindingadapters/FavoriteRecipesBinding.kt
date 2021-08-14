package com.norm.foodrecipes.bindingadapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.norm.foodrecipes.adapters.FavoriteRecipesAdapter
import com.norm.foodrecipes.data.database.entities.FavoritesEntity

class FavoriteRecipesBinding {
    companion object {

        @BindingAdapter("setVisibility", "setData", requireAll = false)
        @JvmStatic
        fun setVisibility(
            view: View,
            favoriteEntities: List<FavoritesEntity>?,
            mAdapter: FavoriteRecipesAdapter?
        ) {
            when (view) {
                is RecyclerView -> {
                    val dataCheck = favoriteEntities.isNullOrEmpty()
                    view.isInvisible = dataCheck
                    if (!dataCheck) {
                        favoriteEntities?.let { mAdapter?.setData(it) }
                    }
                }
                else -> view.isVisible = favoriteEntities.isNullOrEmpty()
            }
        }

    }
}