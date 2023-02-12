package pt.ipp.estg.cmu_07_8200398_8200591_8200592.viewmodels

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.firebase.StorageRepository
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.firebase.UserFirestoreRepository
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.User
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.preferences.PreferenceResult
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.preferences.UserPreferencesRepository

class UserPreferencesViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val repositoryUserPreferences: UserPreferencesRepository
    private val repositoryUserFirestore: UserFirestoreRepository
    private val repositoryStorage: StorageRepository

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>(null)
    val isError: LiveData<Boolean> = _isError

    init {
        repositoryUserPreferences = UserPreferencesRepository(
            (application as Context)
        )
        repositoryUserFirestore = UserFirestoreRepository()
        repositoryStorage = StorageRepository()
    }

    fun readUser(): LiveData<PreferenceResult<User>> {
        val result: MutableLiveData<PreferenceResult<User>> =
            MutableLiveData<PreferenceResult<User>>(
                PreferenceResult.Loading()
            )
        viewModelScope.launch {
            result.value = PreferenceResult.Success(
                repositoryUserPreferences.readUserAndToken()
            )
        }
        return result
    }

    fun updateUser() {
        _isLoading.value = true
        viewModelScope.launch {
            val user: User? = repositoryUserFirestore.getUser()

            repositoryUserPreferences.saveUser(
                user = user!!,
            )

            _isLoading.postValue(false)
        }
    }

    fun registUser(user: User) {
        _isLoading.value = true
        viewModelScope.launch {
            val isSuccess: Boolean = repositoryUserFirestore.registUser(user)

            if (isSuccess) {
                repositoryUserPreferences.saveUser(
                    user = user,
                )
            }

            _isLoading.postValue(false)
        }
    }

    fun changeUser(
        user: User,
        fileUri: Uri?,
        oldPassword: String,
        newPassword: String
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            var isSuccess: Boolean = true

            if (fileUri != null) {
                val photo = repositoryStorage.saveImage(
                    fileUri = fileUri
                )

                isSuccess = photo != null

                if (isSuccess) {
                    user.avatar = photo
                }
            }

            if (isSuccess && oldPassword != "" && newPassword != "") {
                isSuccess = repositoryUserFirestore.changePassword(
                    email = user.email,
                    newPassword = newPassword,
                    oldPassword = oldPassword
                )
            }

            if (isSuccess) {

                isSuccess = repositoryUserFirestore.changeNameAndAvatar(
                    email = user.email,
                    name = user.name,
                    avatar = if(user.avatar!=null){user.avatar!! }else{""}
                )

                if (isSuccess) {
                    repositoryUserPreferences.changeUser(
                        user = user,
                    )
                }
            }
            _isError.postValue(isSuccess)

            _isLoading.postValue(false)
        }
    }

    fun deleteUser() {
        _isLoading.value = true
        viewModelScope.launch {
            repositoryUserPreferences.deleteUser()

            _isLoading.postValue(false)
        }
    }

}