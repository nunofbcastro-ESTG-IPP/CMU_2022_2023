package pt.ipp.estg.cmu_07_8200398_8200591_8200592.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.R
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.database.SmartHouseDatabase
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.database.SmartHouseRepository
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.firebase.AuthenticationRepository
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.firebase.FirestoreDataRepository
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.firebase.UserFirestoreRepository
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.*
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.preferences.UserPreferencesRepository

class UpdateRoomService : Service() {
    companion object {
        private const val TIMER_SERVICE_NOTIFICATION_CHANNEL_ID = "SmartHouse"
        private const val TIMER_SERVICE_NOTIFICATION_ID = 1
    }

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private lateinit var notificationManager: NotificationManagerCompat
    private lateinit var notification: Notification

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            TIMER_SERVICE_NOTIFICATION_CHANNEL_ID,
            getString(R.string.app_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(serviceChannel)
    }

    private fun createNotification() {
        val title = getString(R.string.synchronizing)

        val PROGRESS_MAX = 100
        val PROGRESS_MIN = 0

        // when the service starts, a notification will be created
        notification = NotificationCompat.Builder(this, TIMER_SERVICE_NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setOngoing(true) // an ongoing notification means can't dismiss by the user.
            .setOnlyAlertOnce(true)
            .setProgress(PROGRESS_MAX, PROGRESS_MIN, true)
            .build()

        startForeground(TIMER_SERVICE_NOTIFICATION_ID, notification)
    }

    private suspend fun updateTypeDivisions(
        firestoreDataRepository: FirestoreDataRepository,
        smartHouseRepository: SmartHouseRepository,
    ) {
        val idsTypeDivisions = mutableListOf<Int>()
        for (typeDivisions in firestoreDataRepository.getTypeDivisions()) {
            smartHouseRepository.addTypeDivision(typeDivisions)
            idsTypeDivisions.add(typeDivisions.id)
        }
        smartHouseRepository.removeTypeDivisionsIfNotExist(idsTypeDivisions)
    }

    private suspend fun updateTypeSensors(
        firestoreDataRepository: FirestoreDataRepository,
        smartHouseRepository: SmartHouseRepository,
    ) {
        val idsTypeSensors = mutableListOf<Int>()
        for (typeSensors in firestoreDataRepository.getTypeSensors()) {
            smartHouseRepository.addTypeSensor(typeSensors)
            idsTypeSensors.add(typeSensors.id)
        }
        smartHouseRepository.removeTypeSensorsIfNotExist(idsTypeSensors)
    }

    private suspend fun updateUser(
        userFirestoreRepository: UserFirestoreRepository,
        userPreferencesRepository: UserPreferencesRepository,
        authenticationRepository: AuthenticationRepository,
    ) {
        val user = userFirestoreRepository.getUser()

        if (user != null) {
            userPreferencesRepository.changeUser(user)
        } else {
            authenticationRepository.logout()
        }
    }

    private suspend fun updateHouses(
        firestoreDataRepository: FirestoreDataRepository,
        smartHouseRepository: SmartHouseRepository,
    ) {
        val idsHouses = mutableListOf<String>()

        for (house in firestoreDataRepository.getHouses()) {
            smartHouseRepository.upsertHouse(house.house)
            idsHouses.add(house.house.id)

            val emailsUsers = mutableListOf<String>()
            for (user in house.users) {
                smartHouseRepository.addUser(user)
                smartHouseRepository.addHouseUser(
                    HouseUser(
                        emailUser = user.email,
                        idHouse = house.house.id
                    )
                )
                emailsUsers.add(user.email)
            }
            smartHouseRepository.removeHouseUsersIfNotExist(
                emailsUsers = emailsUsers,
                idHouse = house.house.id,
            )

            updateDivisions(
                idHouse = house.house.id,
                firestoreDataRepository = firestoreDataRepository,
                smartHouseRepository = smartHouseRepository,
            )
        }
        smartHouseRepository.removeHousesIfNotExist(idsHouses)
    }

    private suspend fun updateDivisions(
        idHouse: String,
        firestoreDataRepository: FirestoreDataRepository,
        smartHouseRepository: SmartHouseRepository,
    ) {
        val idsDivisions = mutableListOf<String>()

        for (division in firestoreDataRepository.getDivisions(idHouse)) {
            smartHouseRepository.upsertDivision(division)

            val nDevices = updateGroups(
                idHouse = idHouse,
                idDivision = division.id,
                firestoreDataRepository = firestoreDataRepository,
                smartHouseRepository = smartHouseRepository,
            )

            smartHouseRepository.setNDevices(
                idDivision = division.id,
                idHouse = idHouse,
                nDevices = nDevices,
            )

            idsDivisions.add(division.id)
        }
        smartHouseRepository.removeDevicesIfNotExist(
            ids = idsDivisions,
            idHouse = idHouse,
        )
    }

    private suspend fun updateGroups(
        idHouse: String,
        idDivision: String,
        firestoreDataRepository: FirestoreDataRepository,
        smartHouseRepository: SmartHouseRepository,
    ): Int {
        val idsGroup = mutableListOf<String>()

        var nSensors = 0

        for (group in firestoreDataRepository.getGroups(idHouse, idDivision)) {
            smartHouseRepository.upsertGroup(group = group)

            updateSensors(
                group = group,
                firestoreDataRepository = firestoreDataRepository,
                smartHouseRepository = smartHouseRepository,
                setNSensor = {
                    nSensors++
                }
            )

            idsGroup.add(group.id)
        }

        smartHouseRepository.removeGroupsIfNotExist(
            ids = idsGroup,
            idHouse = idHouse,
            idDivision = idDivision,
        )

        return nSensors
    }

    private suspend fun updateSensors(
        group: Group,
        firestoreDataRepository: FirestoreDataRepository,
        smartHouseRepository: SmartHouseRepository,
        setNSensor: () -> Unit
    ) {
        val idsSensor = mutableListOf<String>()

        for (sensor in firestoreDataRepository.getSensors(
            idHouse = group.idHouse,
            idDivision = group.idDivision,
            idGroup = group.id,
            idTypeSensor = group.idTypeSensor,
        )) {
            if(smartHouseRepository.getSensor(sensor.id) == null){
                when (sensor.idTypeSensor) {
                    0 -> {
                        smartHouseRepository.addLightSensor(
                            sensor = sensor,
                            lightSensor = LightSensor(
                                idSensor = sensor.id,
                                red = 0,
                                green = 0,
                                blue = 0,
                                brightness = 0,
                            )
                        )
                    }
                    1 -> {
                        smartHouseRepository.addPlugSensor(
                            sensor = sensor,
                            plugSensor = PlugSensor(
                                idSensor = sensor.id,
                                status = false,
                            )
                        )
                    }
                    2 -> {
                        smartHouseRepository.addBlindSensor(
                            sensor = sensor,
                            blindSensor = BlindSensor(
                                idSensor = sensor.id,
                                position = 100,
                            )
                        )
                    }
                }
            }else{
                smartHouseRepository.updateIpSensor(
                    idSensor = sensor.id,
                    ip = sensor.ip,
                )
            }

            idsSensor.add(sensor.id)

            setNSensor()
        }
        smartHouseRepository.removeSensorsIfNotExist(
            ids = idsSensor,
            idHouse = group.idHouse,
            idDivision = group.idDivision,
            idGroup = group.id,
        )
    }

    private fun reloadData(intent: Intent?) {
        scope.launch {
            val context: Context = application

            val authenticationRepository = AuthenticationRepository()
            val userFirestoreRepository = UserFirestoreRepository()
            val firestoreDataRepository = FirestoreDataRepository()

            val userPreferencesRepository = UserPreferencesRepository(
                context
            )
            val db = SmartHouseDatabase.getDatabase(context)
            val smartHouseRepository = SmartHouseRepository(db.getSmartHouseDao())

            updateUser(
                userFirestoreRepository = userFirestoreRepository,
                userPreferencesRepository = userPreferencesRepository,
                authenticationRepository = authenticationRepository,
            )

            updateTypeDivisions(
                firestoreDataRepository = firestoreDataRepository,
                smartHouseRepository = smartHouseRepository,
            )

            updateTypeSensors(
                firestoreDataRepository = firestoreDataRepository,
                smartHouseRepository = smartHouseRepository,
            )

            updateHouses(
                firestoreDataRepository = firestoreDataRepository,
                smartHouseRepository = smartHouseRepository,
            )

            //close service
            stopService(intent)
        }
    }

    override fun onCreate() {
        // the notification channel creates when the Service is created
        super.onCreate()
        notificationManager = NotificationManagerCompat.from(this)
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotification()

        //Update room
        reloadData(intent)

        //If the mobile device does not have enough space, the process is saved so that it can be executed later, when space is available.
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    override fun onBind(p0: Intent?): IBinder? = null // We don't need a binder
}
