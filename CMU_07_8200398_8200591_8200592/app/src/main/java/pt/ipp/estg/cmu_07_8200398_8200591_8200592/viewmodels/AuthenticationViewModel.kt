package pt.ipp.estg.cmu_07_8200398_8200591_8200592.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.firebase.AuthenticationRepository
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.firebase.AuthStatus

class AuthenticationViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val repository: AuthenticationRepository

    private val _authState: MutableLiveData<AuthStatus>
    val authState: LiveData<AuthStatus>

    init {
        repository = AuthenticationRepository()
        _authState = MutableLiveData(AuthStatus.LOADING)

        viewModelScope.launch {
            _authState.postValue(
                repository.isLogged()
            )
        }

        authState = _authState
    }

    fun login(
        email: String,
        password: String
    ) {
        _authState.value = AuthStatus.LOADING
        viewModelScope.launch {
            _authState.postValue(
                repository.login(
                    email = email,
                    password = password
                )
            )
        }
    }

    fun register(
        email: String,
        password: String
    ) {
        _authState.value = AuthStatus.LOADING
        viewModelScope.launch {
            _authState.postValue(
                repository.register(
                    email = email,
                    password = password
                )
            )
        }
    }

    fun logout(){
        _authState.value = AuthStatus.LOADING
        viewModelScope.launch {
            _authState.postValue(
                repository.logout()
            )
        }
    }

}