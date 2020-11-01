@file:Suppress("DEPRECATION")

package it.giovanni.arkivio.utils

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.GsonBuilder
import it.giovanni.arkivio.App
import it.giovanni.arkivio.bean.SelectedDaysResponse

class SharedPreferencesManager {

    companion object {

        private val DARK_MODE = "DARK_MODE"
        private val SELECTED_DATE: String = "SELECTED_DATE"

        private var preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.context)

        fun saveDarkModeStateToPreferences(isDarkMode: Boolean) {
            // preferences = PreferenceManager.getDefaultSharedPreferences(App.context)
            val editor = preferences.edit()
            editor.putBoolean(DARK_MODE, isDarkMode)
            editor.apply()
        }

        fun loadDarkModeStateFromPreferences(): Boolean {
            // preferences = PreferenceManager.getDefaultSharedPreferences(App.context)
            return preferences.getBoolean(DARK_MODE, false)
        }

        fun saveSelectedDateToPreferences(selectedDaysResponse: SelectedDaysResponse?) {
            val editor: SharedPreferences.Editor = preferences.edit()
            val builder = GsonBuilder()
            val gson = builder.serializeNulls().create()
            val responseString = gson.toJson(selectedDaysResponse)
            editor.putString(SELECTED_DATE, responseString)
            editor.apply()
        }

        fun loadSelectedDateFromPreferences(): SelectedDaysResponse? {
            val responseString = preferences.getString(SELECTED_DATE, null)
            var selectedDaysResponse: SelectedDaysResponse? = null
            if (responseString != null && responseString != "") {
                val builder = GsonBuilder()
                val gson = builder.serializeNulls().create()
                selectedDaysResponse = gson.fromJson(responseString, SelectedDaysResponse::class.java)
            }
            return selectedDaysResponse
        }
    }
}