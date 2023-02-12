package pt.ipp.estg.cmu_07_8200398_8200591_8200592.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.User

class UserPreferencesRepository(
    private val context: Context
) {
    companion object {
        private val USER_AVATAR = stringPreferencesKey("USER_AVATAR")
        private val USER_EMAIL = stringPreferencesKey("USER_EMAIL")
        private val USER_NAME = stringPreferencesKey("USER_NAME")
    }

    suspend fun readUserAndToken(): User? {
        val preferences = context.dataStore.data.first()

        val userAvatar = preferences[USER_AVATAR]
        val userEmail = preferences[USER_EMAIL]
        val userName = preferences[USER_NAME]

        if (userAvatar == null || userEmail == null || userName == null) {
            return null
        }

        return User(
            avatar = userAvatar,
            email = userEmail,
            name = userName,
        )
    }

    suspend fun saveUser(user: User) {
        if(user.avatar == null){
            user.avatar = ""
        }
        context.dataStore.edit {
            it[USER_AVATAR] = user.avatar!!
            it[USER_EMAIL] = user.email
            it[USER_NAME] = user.name
        }
    }

    suspend fun changeUser(user: User) {
        if(user.avatar == null){
            user.avatar = ""
        }
        context.dataStore.edit {
            it[USER_AVATAR] = user.avatar!!
            it[USER_EMAIL] = user.email
            it[USER_NAME] = user.name
        }
    }

    suspend fun deleteUser() {
        context.dataStore.edit {
            it.remove(USER_AVATAR)
            it.remove(USER_EMAIL)
            it.remove(USER_NAME)
        }
    }
}