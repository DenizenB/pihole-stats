package xyz.podd.piholecontrol.ui.settings

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

import xyz.podd.piholecontrol.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        findPreference<Preference>("add_device")?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            findNavController().navigate(R.id.navigation_add_device)
            true
        }
    }
}