package com.norm.foodrecipes.ui.fragments.instructions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.norm.foodrecipes.R
import com.norm.foodrecipes.databinding.FragmentInstructionsBinding
import com.norm.foodrecipes.models.Result
import com.norm.foodrecipes.util.Constants

class InstructionsFragment : Fragment() {

    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInstructionsBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(Constants.RECIPE_BUNDLE_KEY)

        binding.instructionWebView.webViewClient = object : WebViewClient() {}
        val sourceUrl: String = myBundle!!.sourceUrl
        binding.instructionWebView.loadUrl(sourceUrl)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // to avoid memory leak
        _binding = null
    }
}