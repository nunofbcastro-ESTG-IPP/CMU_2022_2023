package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.preferences


sealed class PreferenceResult<T>(val data: T? = null) {
    var loading: Boolean = true
        protected set
    class Loading<T>(data: T? = null): PreferenceResult<T>(data)
    class Success<T>(data: T?): PreferenceResult<T>(data){
        init {
            loading = false
        }
    }
}