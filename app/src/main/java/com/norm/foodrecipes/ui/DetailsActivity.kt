package com.norm.foodrecipes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.norm.foodrecipes.R
import com.norm.foodrecipes.adapters.PagerAdapter
import com.norm.foodrecipes.data.database.entities.FavoritesEntity
import com.norm.foodrecipes.databinding.ActivityDetailsBinding
import com.norm.foodrecipes.ui.fragments.ingredients.IngredientsFragment
import com.norm.foodrecipes.ui.fragments.instructions.InstructionsFragment
import com.norm.foodrecipes.ui.fragments.overview.OverviewFragment
import com.norm.foodrecipes.util.Constants.Companion.RECIPE_BUNDLE_KEY
import com.norm.foodrecipes.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()

    private var recipeSaved = false
    private var savedRecipeId = 0

    private lateinit var menuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolBar)
        binding.toolBar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPE_BUNDLE_KEY, args.result)

        val pagerAdapter = PagerAdapter(resultBundle, fragments, this)
        binding.viewPager.apply {
            isUserInputEnabled = false
            adapter = pagerAdapter
        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        menuItem = menu!!.findItem(R.id.save_to_favorite_menu)
        checkSavedRecipe(menuItem)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.save_to_favorite_menu && !recipeSaved) {
            saveToFavorite(item)
        } else if (item.itemId == R.id.save_to_favorite_menu && recipeSaved) {
            removeFromFavorite(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkSavedRecipe(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this, { rows ->
            try {
                for (savedRecipe in rows) {
                    if (savedRecipe.result.id == args.result.id) {
                        Log.d("checkSavedRecipe", "${savedRecipe.result.id} = ${args.result.id}")
                        changeMenuItemIconColor(menuItem, R.color.yellow)
                        savedRecipeId = savedRecipe.id
                        recipeSaved = true
                        break
                    }
                }
            } catch (e: Exception) {
                Log.d("DetailsActivity", e.message.toString())
            }
        })
    }

    private fun saveToFavorite(item: MenuItem) {
        val favoritesEntity =
            FavoritesEntity(
                0,
                args.result
            )
        mainViewModel.insertFavoriteRecipe(favoritesEntity)

        changeMenuItemIconColor(item, R.color.yellow)
        showSnackBar("Recipe saved.")
        recipeSaved = true
    }

    private fun removeFromFavorite(item: MenuItem) {
        val favoritesEntity =
            FavoritesEntity(
                savedRecipeId,
                args.result
            )
        mainViewModel.deleteFavoriteRecipe(favoritesEntity)
        changeMenuItemIconColor(item, R.color.white)
        showSnackBar("Remove from favorite.")
        recipeSaved = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.detailsLayout, message, Snackbar.LENGTH_SHORT)
            .setAction("Okay") {}
            .show()
    }

    private fun changeMenuItemIconColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }

    override fun onDestroy() {
        super.onDestroy()
        changeMenuItemIconColor(menuItem, R.color.white)
    }
}