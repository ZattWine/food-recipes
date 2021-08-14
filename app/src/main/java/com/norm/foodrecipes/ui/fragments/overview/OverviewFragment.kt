package com.norm.foodrecipes.ui.fragments.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import coil.load
import com.norm.foodrecipes.R
import com.norm.foodrecipes.bindingadapters.RecipesRowBinding
import com.norm.foodrecipes.databinding.FragmentOverviewBinding
import com.norm.foodrecipes.models.Result
import com.norm.foodrecipes.util.Constants.Companion.RECIPE_BUNDLE_KEY

class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: Result = args!!.getParcelable<Result>(RECIPE_BUNDLE_KEY) as Result

        with(binding) {
            imageView.load(myBundle.image) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
            titleTextView.text = myBundle.title
            likesTextView.text = myBundle.aggregateLikes.toString()
            timeTextView.text = myBundle.readyInMinutes.toString()

            RecipesRowBinding.parseHtmlToString(summaryTextView, myBundle.summary)

            updateColors(myBundle.vegetarian, veganTextView, veganImageView)
            updateColors(myBundle.vegan, veganTextView, veganImageView)
            updateColors(myBundle.cheap, cheapTextView, cheapImageView)
            updateColors(myBundle.dairyFree, dairyFreeTextView, dairyFreeImageView)
            updateColors(myBundle.glutenFree, glutenFreeTextView, glutenFreeImageView)
            updateColors(myBundle.veryHealthy, healthyTextView, healthyImageView)
        }

        return binding.root
    }

    private fun updateColors(stateIsOn: Boolean, textView: TextView, imageView: ImageView) {
        if (stateIsOn) {
            imageView.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // to avoid memory leak
        _binding = null
    }
}