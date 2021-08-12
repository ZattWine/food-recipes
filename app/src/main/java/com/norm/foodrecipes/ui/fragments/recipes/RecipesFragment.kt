package com.norm.foodrecipes.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.norm.foodrecipes.R
import com.norm.foodrecipes.adapters.RecipesAdapter
import com.norm.foodrecipes.databinding.FragmentRecipesBinding
import com.norm.foodrecipes.util.NetworkResult
import com.norm.foodrecipes.util.observeOnce
import com.norm.foodrecipes.viewmodels.MainViewModel
import com.norm.foodrecipes.viewmodels.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<RecipesFragmentArgs>()

    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var mainViewModel: MainViewModel
    private val mAdapter by lazy {
        RecipesAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        setupRecyclerView()
        loadRecipes()

        binding.filterRecipesFab.setOnClickListener {
            findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
        }

        return binding.root
    }

    /** setting up the recipes recycler view */
    private fun setupRecyclerView() {
        binding.recipesRecyclerView.adapter = mAdapter
        binding.recipesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    /** load recipes data from local or remote */
    private fun loadRecipes() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner, { rows ->
                if (rows.isNotEmpty() && !args.backFromBottomSheet) {
                    Log.d("RecipesFragment", "readRecipes() called!")

                    // must have one row only.
                    mAdapter.setData(rows[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }
            })
        }
    }

    /** request api data */
    private fun requestApiData() {
        Log.d("RecipesFragment", "requestApiData() called!")
        mainViewModel.getRecipes(recipeViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let {
                        mAdapter.setData(it)
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }

    /** load data from cache */
    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner, { rows ->
                if (rows.isNotEmpty()) {
                    mAdapter.setData(rows[0].foodRecipe)
                }
            })
        }
    }

    /** show shimmer recycler effect */
    private fun showShimmerEffect() {
        binding.recipesRecyclerView.showShimmer()
    }

    /** hide shimmer recycler effect */
    private fun hideShimmerEffect() {
        binding.recipesRecyclerView.hideShimmer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // to avoid memory leak
        _binding = null
    }
}