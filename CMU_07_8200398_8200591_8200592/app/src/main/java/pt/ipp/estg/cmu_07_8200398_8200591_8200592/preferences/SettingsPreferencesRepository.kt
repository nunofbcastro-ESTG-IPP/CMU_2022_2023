package pt.ipp.estg.cmu_07_8200398_8200591_8200592.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.first
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.preferences.ThemeColors

class SettingsPreferencesRepository(
    private val context: Context
) {
    companion object {
        private val THEME_COLOR = intPreferencesKey("COLOR")
    }

    suspend fun readColorThemeSettings(): ThemeColors {
        val preferences = context.dataStore.data.first()

        return ThemeColors.values()[
                preferences[THEME_COLOR] ?: 0
        ]
    }

    suspend fun saveColorThemeSettings(color: ThemeColors){
        context.dataStore.edit {
            it[THEME_COLOR] = color.ordinal
        }
    }
}