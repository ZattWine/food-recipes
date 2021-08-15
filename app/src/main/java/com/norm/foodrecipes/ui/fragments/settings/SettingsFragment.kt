package com.norm.foodrecipes.ui.fragments.settings

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.norm.foodrecipes.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val oosLicensePref = findPreference<Preference>("licenses")
        val licenseIntent = Intent(requireContext(), OssLicensesMenuActivity::class.java)
        oosLicensePref?.intent = licenseIntent
    }
}