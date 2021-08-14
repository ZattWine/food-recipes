package com.norm.foodrecipes.bindingadapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.norm.foodrecipes.adapters.FavoriteRecipesAdapter
import com.norm.foodrecipes.data.database.entities.FavoritesEntity

class FavoriteRecipesBinding {
    companion object {

        @BindingAdapter("viewVisibility", "setData", requireAll = false)
        @JvmStatic
        fun setDataAndViewVisibility(
            view: View,
            favoriteEntities: List<FavoritesEntity>?,
            mAdapter: FavoriteRecipesAdapter?
        ) {
            if (favoriteEntities.isNullOrEmpty()) {
                Log.d("setDataAndViewVisibility", "favorite is empty")
                when(view) {
                    is ImageView -> {
                        view.visibility = View.VISIBLE
                    }
                    is TextView -> {
                        view.visibility = View.VISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.INVISIBLE
                    }
                }
            } else {
                Log.d("setDataAndViewVisibility", "favorite isn't empty")
                when(view) {
                    is ImageView -> {
                        view.visibility = View.INVISIBLE
                    }
                    is TextView -> {
                        view.visibility = View.INVISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.VISIBLE
                        mAdapter?.setData(favoriteEntities)
                    }
                }
            }
        }

    }
}