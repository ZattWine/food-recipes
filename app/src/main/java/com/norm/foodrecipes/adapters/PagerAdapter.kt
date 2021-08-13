package com.norm.foodrecipes.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(
    private val result: Bundle,
    private val fragments: ArrayList<Fragment>,
    fa: FragmentActivity
): FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = fragments[position]
        fragment.arguments = result
        return fragment
    }
}