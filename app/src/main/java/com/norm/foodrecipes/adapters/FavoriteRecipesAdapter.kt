package com.norm.foodrecipes.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.norm.foodrecipes.R
import com.norm.foodrecipes.data.database.entities.FavoritesEntity
import com.norm.foodrecipes.databinding.FavoriteRecipesRowLayoutBinding
import com.norm.foodrecipes.ui.fragments.favorites.FavoriteRecipesFragmentDirections
import com.norm.foodrecipes.util.RecipesDiffUtil
import com.norm.foodrecipes.viewmodels.MainViewModel

class FavoriteRecipesAdapter(
    private val fa: FragmentActivity,
    private val mainViewModel: MainViewModel
) :
    RecyclerView.Adapter<FavoriteRecipesAdapter.MyViewHolder>(),
    ActionMode.Callback {

    private var multiSelection = false
    private lateinit var mActionMode: ActionMode
    private lateinit var rootView: View

    private var myViewHolders = arrayListOf<MyViewHolder>()
    private var selectedRecipes = arrayListOf<FavoritesEntity>()
    private var favoriteRecipes = emptyList<FavoritesEntity>()

    class MyViewHolder(
        val binding: FavoriteRecipesRowLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(favoritesEntity: FavoritesEntity) {
            binding.favoriteEntity = favoritesEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteRecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        myViewHolders.add(holder)
        rootView = holder.itemView.rootView

        val currentFavoriteRecipe = favoriteRecipes[position]
        holder.bind(currentFavoriteRecipe)

        /**
         * Single click listener.
         */
        holder.binding.favoriteRecipesRowLayout.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, currentFavoriteRecipe)
            } else {
                val action =
                    FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(
                        currentFavoriteRecipe.result
                    )
                holder.itemView.findNavController().navigate(action)
            }
        }

        /**
         * Long click listener.
         */
        holder.binding.favoriteRecipesRowLayout.setOnLongClickListener {
            // If not multiSelection, set multiSelection=true
            // start actionMode and set currentFavRecipe to selection
            // If multiSelection, set multiSelection=false
            if (!multiSelection) {
                multiSelection = true
                fa.startActionMode(this)
                applySelection(holder, currentFavoriteRecipe)
                true
            } else {
                multiSelection = false
                false
            }
        }
    }

    /**
     * Add selectedRecipe to [selectedRecipes].
     * If selectedRecipe does not contain in [selectedRecipes],
     * change recipe item style. If not, change recipe item style to default.
     */
    private fun applySelection(holder: MyViewHolder, currentRecipe: FavoritesEntity) {
        if (selectedRecipes.contains(currentRecipe)) {
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
            applyActionModeTitle()
        } else {
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
            applyActionModeTitle()
        }
    }

    /**
     * Change recipe item style.
     */
    private fun changeRecipeStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int) {
        with(holder.binding) {

            favoriteRecipesRowLayout.setBackgroundColor(
                ContextCompat.getColor(fa, backgroundColor)
            )

            favoriteRowCardView.strokeColor = ContextCompat.getColor(fa, strokeColor)
        }
    }

    private fun applyActionModeTitle() {
        when (selectedRecipes.size) {
            0 -> {
                mActionMode.finish()
            }
            1 -> {
                mActionMode.title = "${selectedRecipes.size} item selected"
            }
            else -> {
                mActionMode.title = "${selectedRecipes.size} items selected"
            }
        }
    }

    override fun getItemCount(): Int {
        return favoriteRecipes.size
    }

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favorites_contextual_menu, menu)
        mActionMode = actionMode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        if (menu?.itemId == R.id.delete_favorite_recipe_menu) {
            selectedRecipes.forEach {
                mainViewModel.deleteFavoriteRecipe(it)
            }
            showSnackBar("${selectedRecipes.size} Recipe/s removed.")

            multiSelection = false
            selectedRecipes.clear()
            actionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        // need to re-apply the default style to all selectedRecipe items
        myViewHolders.forEach { holder ->
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        }

        multiSelection = false

        // need to clear the recipe selection list.
        selectedRecipes.clear()

        applyStatusBarColor(R.color.statusBarColor)
    }

    /**
     * Change window statusBarColor.
     */
    private fun applyStatusBarColor(color: Int) {
        fa.window.statusBarColor = ContextCompat.getColor(fa, color)
    }

    fun setData(newFavoriteRecipes: List<FavoritesEntity>) {
        val favRecipesDiffUtil = RecipesDiffUtil(favoriteRecipes, newFavoriteRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(favRecipesDiffUtil)
        favoriteRecipes = newFavoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            rootView,
            message,
            Snackbar.LENGTH_SHORT
        )
            .setAction("Okay") {}
            .show()
    }

    /**
     * Close the action mode if action mode is initialized.
     */
    fun clearContextualMode() {
        if (this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
    }
}