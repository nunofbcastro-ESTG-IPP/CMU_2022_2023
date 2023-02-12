package pt.ipp.estg.cmu_07_8200398_8200591_8200592.firebase

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.*

class StorageRepository {
    suspend fun saveImage(
        fileUri: Uri
    ): String? {
        var url: String? = null
        val fileName = UUID.randomUUID().toString() + ".jpg"

        val refStorage = FirebaseStorage.getInstance().reference.child("images").child(fileName)

        try {
            url = refStorage.putFile(fileUri).await().storage.downloadUrl.await().toString()
        } catch (_: Exception) {
        }

        return url
    }
}