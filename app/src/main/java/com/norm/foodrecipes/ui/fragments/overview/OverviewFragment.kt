package com.norm.foodrecipes.ui.fragments.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import coil.load
import com.norm.foodrecipes.R
import com.norm.foodrecipes.databinding.FragmentOverviewBinding
import com.norm.foodrecipes.models.Result
import com.norm.foodrecipes.util.Constants.Companion.RECIPE_BUNDLE_KEY
import org.jsoup.Jsoup

class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(RECIPE_BUNDLE_KEY)

        with(binding) {
            imageView.load(myBundle?.image) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
            titleTextView.text = myBundle?.title
            likesTextView.text = myBundle?.aggregateLikes.toString()
            timeTextView.text = myBundle?.readyInMinutes.toString()

            myBundle?.summary.let {
                val summary = Jsoup.parse(it).toString()
                // binding.summaryTextView.text = strSummary
                summaryWebView.webViewClient = object : WebViewClient() {}
                summaryWebView.loadData(summary, "text/html; charset=utf-8", "UTF-8")
            }

            if (myBundle?.vegetarian == true) {
                vegetarianImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                vegetarianTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }

            if (myBundle?.vegan == true) {
                veganImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                veganTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }

            if (myBundle?.glutenFree == true) {
                glutenFreeImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                glutenFreeTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }

            if (myBundle?.dairyFree == true) {
                dairyFreeImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                dairyFreeTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }

            if (myBundle?.veryHealthy == true) {
                healthyImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                healthyTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }

            if (myBundle?.cheap == true) {
                cheapImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                cheapTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // to avoid memory leak
        _binding = null
    }
}