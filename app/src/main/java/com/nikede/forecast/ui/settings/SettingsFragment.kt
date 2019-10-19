package com.nikede.forecast.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.nikede.forecast.R
import org.kodein.di.android.activityRetainedScope

class SettingsFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.settings)
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = null

    }

}