package com.norm.foodrecipes.ui.fragments.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.norm.foodrecipes.adapters.RecipesAdapter
import com.norm.foodrecipes.databinding.FragmentRecipesBinding
import com.norm.foodrecipes.util.Constants.Companion.API_KEY
import com.norm.foodrecipes.util.Constants.Companion.DEFAULT_QUERY_DIET
import com.norm.foodrecipes.util.Constants.Companion.DEFAULT_QUERY_FILL_INGREDIENTS
import com.norm.foodrecipes.util.Constants.Companion.DEFAULT_QUERY_NUMBER
import com.norm.foodrecipes.util.Constants.Companion.DEFAULT_QUERY_RECIPE_INFORMATION
import com.norm.foodrecipes.util.Constants.Companion.DEFAULT_QUERY_TYPE
import com.norm.foodrecipes.util.Constants.Companion.QUERY_API_KEY
import com.norm.foodrecipes.util.Constants.Companion.QUERY_DIET
import com.norm.foodrecipes.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.norm.foodrecipes.util.Constants.Companion.QUERY_NUMBER
import com.norm.foodrecipes.util.Constants.Companion.QUERY_RECIPE_INFORMATION
import com.norm.foodrecipes.util.Constants.Companion.QUERY_TYPE
import com.norm.foodrecipes.util.NetworkResult
import com.norm.foodrecipes.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private var _binding: FragmentRecipesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private val mAdapter by lazy {
        RecipesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        setupRecyclerView()
        requestApiData()

        return binding.root
    }

    /** request api data */
    private fun requestApiData() {
        mainViewModel.getRecipes(applyQueries())
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

    /** queries preparation to apply on recipes api request */
    private fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_NUMBER] = DEFAULT_QUERY_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = DEFAULT_QUERY_TYPE
        queries[QUERY_DIET] = DEFAULT_QUERY_DIET
        queries[QUERY_RECIPE_INFORMATION] = DEFAULT_QUERY_RECIPE_INFORMATION
        queries[QUERY_FILL_INGREDIENTS] = DEFAULT_QUERY_FILL_INGREDIENTS

        return queries
    }

    /** setting up the recipes recycler view */
    private fun setupRecyclerView() {
        binding.recipesRecyclerView.adapter = mAdapter
        binding.recipesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
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