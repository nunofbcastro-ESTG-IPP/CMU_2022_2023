package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models

sealed class NetworkAndRoomResult {
    var loading: Boolean? = null
        protected set
    var sucess: Boolean? = null
        protected set

    class Empty() : NetworkAndRoomResult() {
        init {
            loading = null
            sucess = null
        }
    }

    class Loading() : NetworkAndRoomResult() {
        init {
            loading = true
            sucess = null
        }
    }

    class Error() : NetworkAndRoomResult() {
        init {
            loading = false
            sucess = false
        }
    }

    class Success() : NetworkAndRoomResult() {
        init {
            loading = false
            sucess = true
        }
    }
}