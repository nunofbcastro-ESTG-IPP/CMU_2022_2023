package pt.ipp.estg.cmu_07_8200398_8200591_8200592.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.firebase.AuthStatus


class AuthenticationRepository() {
    private val firebaseAuth: FirebaseAuth

    init {
        firebaseAuth = Firebase.auth
    }

    fun isLogged(): AuthStatus {
        return if(firebaseAuth.currentUser != null){
            AuthStatus.LOGGED
        }else{
            AuthStatus.NO_LOGIN
        }
    }

    suspend fun login(
        email: String,
        password: String
    ): AuthStatus {
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            if (result != null && result.user != null) {
                return AuthStatus.LOGGED
            }
        } catch (_: Exception) {
        }
        return AuthStatus.INVALID_LOGIN
    }

    suspend fun register(
        email: String,
        password: String
    ): AuthStatus {
        try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            if (result != null && result.user != null) {
                return AuthStatus.LOGGED
            }
        } catch (_: Exception) {
        }
        return AuthStatus.INVALID_LOGIN
    }

    fun logout(): AuthStatus {
        firebaseAuth.signOut()
        return AuthStatus.NO_LOGIN
    }

}