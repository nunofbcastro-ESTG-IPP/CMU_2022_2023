package pt.ipp.estg.cmu_07_8200398_8200591_8200592

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.network.ConnectivityObserver
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.network.NetworkConnectivityObserver
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.scaffold.MyScaffold
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.utils.setImageLoader

class MainActivity : ComponentActivity() {
    private lateinit var connectivityObserver: ConnectivityObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        setContent {
            val networkStatus by connectivityObserver.observe().collectAsState(
                initial = ConnectivityObserver.NetworkStatus.Unavailable
            )

            if(networkStatus == ConnectivityObserver.NetworkStatus.Available){
                setImageLoader(LocalContext.current)
            }

            CMU_07_8200398_8200591_8200592Theme {
                MyScaffold(
                    networkStatus = networkStatus
                )
            }
        }
    }
}