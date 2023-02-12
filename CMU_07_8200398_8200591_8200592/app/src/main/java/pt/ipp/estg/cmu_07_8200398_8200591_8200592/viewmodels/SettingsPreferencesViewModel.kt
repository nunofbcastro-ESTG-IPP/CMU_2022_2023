package pt.ipp.estg.cmu_07_8200398_8200591_8200592.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.preferences.PreferenceResult
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.preferences.ThemeColors
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.preferences.SettingsPreferencesRepository

class SettingsPreferencesViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val repository: SettingsPreferencesRepository

    init {
        repository = SettingsPreferencesRepository(
            (application as Context)
        )
    }

    fun readSettings(): LiveData<PreferenceResult<ThemeColors>> {
        val result : MutableLiveData<PreferenceResult<ThemeColors>> = MutableLiveData<PreferenceResult<ThemeColors>>(
            PreferenceResult.Loading()
        )
        viewModelScope.launch {
            result.value = PreferenceResult.Success(repository.readColorThemeSettings())
        }
        return result
    }

    fun saveColorSettings(color: ThemeColors) {
        viewModelScope.launch {
            repository.saveColorThemeSettings(
                color = color
            )
        }
    }
}