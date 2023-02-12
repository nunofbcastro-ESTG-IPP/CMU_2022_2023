package pt.ipp.estg.cmu_07_8200398_8200591_8200592.firebase

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.User
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.firebase.CollectionsNames

class UserFirestoreRepository() {
    private val db: FirebaseFirestore
    private val firebaseAuth: FirebaseAuth

    init {
        db = Firebase.firestore
        firebaseAuth = Firebase.auth
    }

    suspend fun getUser(): User? {
        var user: User? = null

        try {
            val queryReference =
                db.collection(CollectionsNames.usersCollection.collectionName)
                    .whereEqualTo(
                        CollectionsNames.usersCollection.fieldEmail,
                        firebaseAuth.currentUser!!.email
                    ).limit(1)

            val result = queryReference.get().await()

            if (!result.isEmpty) {
                user = result.documents[0].toObject(User::class.java)!!
            }
        } catch (_: Exception) {
        }

        return user
    }

    suspend fun registUser(user: User): Boolean {
        if (firebaseAuth.currentUser == null || !firebaseAuth.currentUser!!.email.equals(user.email)) {
            return false
        }

        try {
            val collection = db.collection(CollectionsNames.usersCollection.collectionName)
            val result =
                collection.whereEqualTo(CollectionsNames.usersCollection.fieldEmail, user.email)
                    .get().await()


            if (result.isEmpty) {
                collection.add(
                    user
                ).await()
                return true
            }
        } catch (_: Exception) {
        }

        return false
    }

    suspend fun changePassword(
        email: String,
        oldPassword: String,
        newPassword: String,
    ): Boolean {
        val user = firebaseAuth.currentUser

        if (user == null || !firebaseAuth.currentUser!!.email.equals(email)) {
            return false
        }

        var isSuccess = false

        try {
            user.reauthenticateAndRetrieveData(
                EmailAuthProvider.getCredential(
                    email,
                    oldPassword
                )
            ).await()
            user.updatePassword(newPassword).await()

            isSuccess = true
        } catch (_: Exception) {
        }

        return isSuccess
    }

    suspend fun changeNameAndAvatar(
        email: String,
        name: String,
        avatar: String,
    ): Boolean {
        if (firebaseAuth.currentUser == null || !firebaseAuth.currentUser!!.email.equals(email)) {
            return false
        }

        try {
            val collection = db.collection(CollectionsNames.usersCollection.collectionName)
            val result =
                collection.whereEqualTo(CollectionsNames.usersCollection.fieldEmail, email).limit(1)
                    .get().await()

            if (!result.isEmpty) {
                val userToUpdate = hashMapOf<String, Any>(
                    CollectionsNames.usersCollection.fieldName to name,
                    CollectionsNames.usersCollection.fieldAvatar to avatar
                )

                collection.document(result.documents[0].id).update(
                    userToUpdate
                ).await()
                return true
            }
        } catch (_: Exception) {
        }

        return false
    }
}