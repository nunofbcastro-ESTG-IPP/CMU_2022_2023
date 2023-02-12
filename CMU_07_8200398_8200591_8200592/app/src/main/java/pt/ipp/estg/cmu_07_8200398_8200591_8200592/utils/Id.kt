package pt.ipp.estg.cmu_07_8200398_8200591_8200592.utils

inline fun <reified T : Class<*>> T.getId(resourceName: String): Int {
    return try {
        val idField = getDeclaredField(resourceName)
        idField.getInt(idField)
    } catch (e: Exception) {
        -1
    }
}