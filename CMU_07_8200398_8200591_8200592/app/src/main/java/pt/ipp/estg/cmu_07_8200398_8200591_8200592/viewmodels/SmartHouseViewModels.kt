package pt.ipp.estg.cmu_07_8200398_8200591_8200592.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.database.SmartHouseDatabase
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.database.SmartHouseRepository
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.firebase.FirestoreDataRepository
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.firebase.StorageRepository
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.NetworkAndRoomResult
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.relations.SensorAndBlindSensor
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.relations.SensorAndLightSensor
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.relations.SensorAndPlugSensor
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.RGBB
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.*
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.request.Turn
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.network.sensors.blind.BlindApiRepository
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.network.sensors.light.LightApiRepository
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.network.sensors.plug.PlugApiRepository

class SmartHouseViewModels(application: Application) : AndroidViewModel(application) {
    //Repository DB
    private val repositoryDB: SmartHouseRepository

    //Repository firebase
    private val repositoryFirestore: FirestoreDataRepository

    //Repository storage
    private val repositoryStorage: StorageRepository

    //Repository Api
    private val repositoryPlugApi: PlugApiRepository
    private val repositoryLightApi: LightApiRepository
    private val repositoryBlindApi: BlindApiRepository

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _networkAndRoomResult = MutableLiveData<NetworkAndRoomResult>(
        NetworkAndRoomResult.Empty()
    )
    val networkAndRoomResult: LiveData<NetworkAndRoomResult> = _networkAndRoomResult

    init {
        val db = SmartHouseDatabase.getDatabase(application)
        repositoryDB = SmartHouseRepository(db.getSmartHouseDao())
        repositoryFirestore = FirestoreDataRepository()
        repositoryStorage = StorageRepository()
        repositoryPlugApi = PlugApiRepository()
        repositoryLightApi = LightApiRepository()
        repositoryBlindApi = BlindApiRepository()
    }

    fun resetNetworkAndRoomResult() {
        _networkAndRoomResult.postValue(
            NetworkAndRoomResult.Empty()
        )
    }

    fun getAllHouses(): LiveData<List<House>> {
        return repositoryDB.getHouses()
    }

    fun getHouse(
        idHouse: String
    ): LiveData<House> {
        return repositoryDB.getHouse(
            idHouse = idHouse
        )
    }

    fun getHouseConsume(
        idHouse: String
    ): LiveData<Double> {
        return repositoryDB.getHouseConsume(
            idHouse = idHouse
        )
    }

    fun getTypesDivisions(): LiveData<List<TypeDivision>> {
        return repositoryDB.getTypesDivisions()
    }

    fun getTypeDivision(id: Int): LiveData<TypeDivision> {
        return repositoryDB.getTypeDivision(id = id)
    }

    fun addHouse(
        house: House,
        fileUri: Uri?,
        userList: List<User>
    ) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.Default) {
            if (fileUri != null) {
                val photo = repositoryStorage.saveImage(
                    fileUri = fileUri
                )

                if (photo != null) {
                    house.photo = photo
                }
            }

            val id = repositoryFirestore.addHouse(
                house
            )

            if (id != null) {
                house.id = id

                repositoryDB.addHouse(
                    house = house,
                )

                for (user in userList) {
                    val houseUser = HouseUser(
                        emailUser = user.email,
                        idHouse = house.id,
                    )

                    val isSuccessAddUser = repositoryFirestore.addHouseUser(
                        houseUser = houseUser
                    )

                    if (isSuccessAddUser) {
                        repositoryDB.addHouseUser(
                            houseUser = houseUser
                        )
                    }
                }
            }

            _isLoading.postValue(false)
        }
    }

    fun updateHouse(
        house: House,
        fileUri: Uri?,
        useAdd: List<User>,
        useRemove: List<User>
    ) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.Default) {
            if (fileUri != null) {
                val photo = repositoryStorage.saveImage(
                    fileUri = fileUri
                )

                if (photo != null) {
                    house.photo = photo
                }
            }

            val isSuccess = repositoryFirestore.updateHouse(
                house
            )

            if (isSuccess) {
                repositoryDB.updateHouse(
                    house = house,
                )

                for (user in useAdd) {
                    val houseUser = HouseUser(
                        emailUser = user.email,
                        idHouse = house.id,
                    )

                    val isSuccessAddUser = repositoryFirestore.addHouseUser(
                        houseUser = houseUser
                    )

                    if (isSuccessAddUser) {
                        repositoryDB.addHouseUser(
                            houseUser = houseUser
                        )
                    }
                }

                for (user in useRemove) {
                    val houseUser = HouseUser(
                        emailUser = user.email,
                        idHouse = house.id,
                    )

                    val isSuccessRemoveUser = repositoryFirestore.deleteHouseUser(
                        houseUser = houseUser
                    )

                    if (isSuccessRemoveUser) {
                        repositoryDB.deleteHouseUser(
                            houseUser = houseUser
                        )
                    }
                }
            }

            _isLoading.postValue(false)
        }
    }


    fun removeHouse(
        house: House
    ) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.Default) {
            val isSuccess = repositoryFirestore.removeHouse(
                house = house
            )

            if (isSuccess) {
                repositoryDB.removeHouse(
                    house = house,
                )
            }

            _isLoading.postValue(false)
        }
    }

    fun getHouseDivisions(
        idHouse: String
    ): LiveData<List<Division>> {
        return repositoryDB.getHouseDivisions(
            idHouse = idHouse,
        )
    }

    fun getDivisionById(
        idDivision: String,
        idHouse: String
    ): LiveData<Division> {
        return repositoryDB.getDivisionById(idDivision = idDivision, idHouse = idHouse)
    }

    fun addDivision(
        division: Division
    ) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.Default) {
            val id = repositoryFirestore.addDivision(division)

            if (id != null) {
                division.id = id
                repositoryDB.addDivision(
                    division = division,
                )
            }

            _isLoading.postValue(false)
        }
    }

    fun updateDivision(
        division: Division
    ) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.Default) {
            val isSuccess = repositoryFirestore.updateDivision(division)

            if (isSuccess) {
                repositoryDB.updateDivision(
                    division = division,
                )

            }
            _isLoading.postValue(false)
        }
    }

    fun removeDivision(
        division: Division
    ) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.Default) {
            val isSuccess = repositoryFirestore.removeDivision(division)

            if (isSuccess) {
                repositoryDB.removeDivision(
                    division = division,
                )
            }

            _isLoading.postValue(false)
        }
    }

    fun getAllDivisions(
        idHouse: String
    ): LiveData<List<Division>> {
        return repositoryDB.getAllDivisions(
            idHouse = idHouse
        )
    }

    fun getTypesSensors(): LiveData<List<TypeSensor>> {
        return repositoryDB.getTypesSensors()
    }

    fun getTypeSensor(id: Int): LiveData<TypeSensor> {
        return repositoryDB.getTypeSensor(id = id)
    }

    fun getGroup(
        idHouse: String,
        idDivision: String,
        idTypeSensor: Int,
        name: String
    ): LiveData<Group> {
        return repositoryDB.getGroup(
            idHouse = idHouse,
            idDivision = idDivision,
            idTypeSensor = idTypeSensor,
            name = name
        )
    }

    fun getGroups(
        idHouse: String,
        idDivision: String,
        idTypeSensor: Int
    ): LiveData<List<Group>> {
        return repositoryDB.getGroups(
            idHouse = idHouse,
            idDivision = idDivision,
            idTypeSensor = idTypeSensor
        )
    }

    fun getAllSensors(): LiveData<List<Sensor>> {
        return repositoryDB.getAllSensors()
    }

    fun getDivisionSensors(
        idHouse: String,
        idDivision: String
    ): LiveData<List<Sensor>> {
        return repositoryDB.getDivisionSensors(
            idHouse = idHouse,
            idDivision = idDivision,
        )
    }

    fun getDivisionTypeSensor(
        idHouse: String,
        idDivision: String
    ): LiveData<List<TypeSensor>> {
        return repositoryDB.getDivisionTypeSensor(
            idHouse = idHouse,
            idDivision = idDivision
        )
    }

    fun addPlugSensor(
        group: Group?,
        sensor: Sensor,
        plugSensor: PlugSensor
    ) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.Default) {
            var idGroup: String? = null

            if (group != null) {

                idGroup = repositoryFirestore.addGroup(group = group)

                if (idGroup != null) {
                    group.id = idGroup
                    sensor.idGroup = idGroup

                    repositoryDB.addGroup(
                        group = group
                    )
                }
            }


            if (group == null || idGroup != null) {
                val id = repositoryFirestore.addSensor(
                    sensor = sensor
                )

                if (id != null) {
                    sensor.id = id
                    plugSensor.idSensor = id

                    val isInserted = repositoryDB.addPlugSensor(
                        sensor = sensor,
                        plugSensor = plugSensor,
                    )

                    if (isInserted) {
                        updatePlugSensorOnline(
                            sensor = sensor,
                            plugSensor = plugSensor,
                        )
                    }
                }
            }

            _isLoading.postValue(false)
        }
    }

    fun updatePlugSensorOnline(
        sensor: Sensor,
        plugSensor: PlugSensor
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            _networkAndRoomResult.postValue(
                NetworkAndRoomResult.Loading()
            )

            val response = repositoryPlugApi.status(sensor.ip)

            if (response.message == null) {
                try {
                    response.data?.let { StatusPlugSensor ->
                        sensor.consumption = StatusPlugSensor.meters[0].power
                        plugSensor.status = StatusPlugSensor.relays[0].ison

                        repositoryDB.updateSensorAndPlugSensor(
                            sensor = sensor,
                            plugSensor = plugSensor
                        )
                        _networkAndRoomResult.postValue(
                            NetworkAndRoomResult.Success()
                        )
                        return@launch
                    }
                } catch (_: Exception) {
                }
            }
            _networkAndRoomResult.postValue(
                NetworkAndRoomResult.Error()
            )
        }
    }

    fun turnPlug(
        sensor: Sensor,
        plugSensor: PlugSensor,
        status: Boolean,
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            _networkAndRoomResult.postValue(
                NetworkAndRoomResult.Loading()
            )

            val response = repositoryPlugApi.setTurn(
                baseUrlSensor = sensor.ip,
                turn = Turn.booleanToTurn(status)
            )

            if (response.message == null) {
                try {
                    response.data?.let { StatusPlugSensor ->
                        plugSensor.status = StatusPlugSensor.ison

                        repositoryDB.updateSensorAndPlugSensor(
                            sensor = sensor,
                            plugSensor = plugSensor
                        )
                        _networkAndRoomResult.postValue(
                            NetworkAndRoomResult.Success()
                        )
                        return@launch
                    }
                } catch (_: Exception) {
                }
            }
            _networkAndRoomResult.postValue(
                NetworkAndRoomResult.Error()
            )
        }
    }

    fun addBlindSensor(
        group: Group?,
        sensor: Sensor,
        blindSensor: BlindSensor
    ) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.Default) {
            var idGroup: String? = null

            if (group != null) {

                idGroup = repositoryFirestore.addGroup(group = group)

                if (idGroup != null) {
                    group.id = idGroup
                    sensor.idGroup = idGroup

                    repositoryDB.addGroup(
                        group = group
                    )
                }
            }


            if (group == null || idGroup != null) {
                val id = repositoryFirestore.addSensor(
                    sensor = sensor
                )

                if (id != null) {
                    sensor.id = id
                    blindSensor.idSensor = id

                    val isInserted = repositoryDB.addBlindSensor(
                        sensor = sensor,
                        blindSensor = blindSensor,
                    )

                    if (isInserted) {
                        updateBlindSensorOnline(
                            sensor = sensor,
                            blindSensor = blindSensor,
                        )
                    }
                }
            }

            _isLoading.postValue(false)
        }
    }

    fun updateBlindSensorOnline(
        sensor: Sensor,
        blindSensor: BlindSensor
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            _networkAndRoomResult.postValue(
                NetworkAndRoomResult.Loading()
            )

            val response = repositoryBlindApi.status(sensor.ip)

            if (response.message == null) {
                try {
                    response.data?.let { StatusBlindSensor ->
                        sensor.consumption = StatusBlindSensor.meters[0].power
                        blindSensor.position = StatusBlindSensor.rollers[0].current_pos

                        repositoryDB.updateSensorAndBlindSensor(
                            sensor = sensor,
                            blindSensor = blindSensor
                        )
                        _networkAndRoomResult.postValue(
                            NetworkAndRoomResult.Success()
                        )
                        return@launch
                    }
                } catch (_: Exception) {
                }
            }
            _networkAndRoomResult.postValue(
                NetworkAndRoomResult.Error()
            )
        }
    }

    fun changePositionBlindSensor(
        sensor: Sensor,
        blindSensor: BlindSensor,
        position: Int,
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            _networkAndRoomResult.postValue(
                NetworkAndRoomResult.Loading()
            )

            val response = repositoryBlindApi.setGo(
                baseUrlSensor = sensor.ip,
                position = position,
            )

            if (response.message == null) {
                try {
                    response.data?.let { Roller ->
                        blindSensor.position = blindSensor.position

                        repositoryDB.updateSensorAndBlindSensor(
                            sensor = sensor,
                            blindSensor = blindSensor
                        )
                        _networkAndRoomResult.postValue(
                            NetworkAndRoomResult.Success()
                        )
                        return@launch
                    }
                } catch (_: Exception) {
                }
            }
            _networkAndRoomResult.postValue(
                NetworkAndRoomResult.Error()
            )
        }
    }

    fun addLightSensor(
        group: Group?,
        sensor: Sensor,
        lightSensor: LightSensor
    ) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.Default) {
            var idGroup: String? = null

            if (group != null) {
                idGroup = repositoryFirestore.addGroup(group = group)

                if (idGroup != null) {
                    group.id = idGroup
                    sensor.idGroup = idGroup

                    repositoryDB.addGroup(
                        group = group
                    )
                }
            }


            if (group == null || idGroup != null) {
                val id = repositoryFirestore.addSensor(
                    sensor = sensor
                )

                if (id != null) {
                    sensor.id = id
                    lightSensor.idSensor = id

                    val isInserted = repositoryDB.addLightSensor(
                        sensor = sensor,
                        lightSensor = lightSensor,
                    )

                    if (isInserted) {
                        updateLightSensorOnline(
                            sensor = sensor,
                            lightSensor = lightSensor,
                        )
                    }
                }
            }

            _isLoading.postValue(false)
        }
    }

    fun updateLightSensorOnline(
        sensor: Sensor,
        lightSensor: LightSensor
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            _networkAndRoomResult.postValue(
                NetworkAndRoomResult.Loading()
            )

            val response = repositoryLightApi.status(sensor.ip)

            if (response.message == null) {
                try {
                    response.data?.let { StatusLightSensor ->
                        sensor.consumption = StatusLightSensor.meters[0].power
                        lightSensor.red = StatusLightSensor.lights[0].red
                        lightSensor.green = StatusLightSensor.lights[0].green
                        lightSensor.blue = StatusLightSensor.lights[0].blue
                        lightSensor.brightness = StatusLightSensor.lights[0].gain

                        repositoryDB.updateSensorAndLightSensor(
                            sensor = sensor,
                            lightSensor = lightSensor
                        )
                        _networkAndRoomResult.postValue(
                            NetworkAndRoomResult.Success()
                        )
                        return@launch
                    }
                } catch (_: Exception) {
                }
            }
            _networkAndRoomResult.postValue(
                NetworkAndRoomResult.Error()
            )
        }
    }

    fun changeColorLightSensor(
        sensor: Sensor,
        lightSensor: LightSensor,
        rgbb: RGBB,
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            _networkAndRoomResult.postValue(
                NetworkAndRoomResult.Loading()
            )

            val response = repositoryLightApi.setTurnOn(
                baseUrlSensor = sensor.ip,
                red = rgbb.Red,
                green = rgbb.Green,
                blue = rgbb.Blue,
                brightness = rgbb.Brightness,
            )

            if (response.message == null) {
                try {
                    response.data?.let { Light ->
                        lightSensor.red = Light.red
                        lightSensor.green = Light.green
                        lightSensor.blue = Light.blue
                        lightSensor.brightness = Light.gain

                        repositoryDB.updateSensorAndLightSensor(
                            sensor = sensor,
                            lightSensor = lightSensor
                        )
                        _networkAndRoomResult.postValue(
                            NetworkAndRoomResult.Success()
                        )
                        return@launch
                    }
                } catch (_: Exception) {
                }
            }
            _networkAndRoomResult.postValue(
                NetworkAndRoomResult.Error()
            )
        }
    }

    fun removeSensor(
        sensor: Sensor
    ) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.Default) {
            val isSuccess = repositoryFirestore.removeSensor(sensor = sensor)

            if (isSuccess) {
                repositoryDB.removeSensor(
                    sensor = sensor,
                )
            }

            _isLoading.postValue(false)
        }
    }

    fun getUsersHouse(
        idHouse: String
    ): LiveData<List<User>> {
        return repositoryDB.getUsersHouse(
            idHouse = idHouse
        )
    }

    fun getUser(email: String): LiveData<User> {
        return repositoryDB.getUser(email)
    }

    fun getUserOnline(email: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.Default) {
            val user = repositoryFirestore.getUserWithoutMe(email)
            if (user != null) {
                repositoryDB.addUser(
                    user
                )
            }
            _isLoading.postValue(false)
        }
    }

    fun getUsers(): LiveData<List<User>> {
        return repositoryDB.getUsers()
    }

    fun clearDatabase() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.Default) {
            repositoryDB.clearDatabase()

            _isLoading.postValue(false)
        }
    }

    fun getSensorAndLightSensor(id: Int): LiveData<SensorAndLightSensor> {
        return repositoryDB.getSensorAndLightSensor(id = id)
    }

    fun getSensorsAndLightSensorsByGroup(
        idHouse: String,
        idDivision: String,
        groupList: List<String>
    ): LiveData<List<SensorAndLightSensor>> {
        return repositoryDB.getSensorsAndLightSensorsByGroup(
            idHouse = idHouse,
            idDivision = idDivision,
            groupList = groupList
        )
    }

    fun getSensorAndPlugSensor(id: Int): LiveData<SensorAndPlugSensor> {
        return repositoryDB.getSensorAndPlugSensor(id = id)
    }

    fun getSensorsAndPlugSensorsByGroup(
        idHouse: String,
        idDivision: String,
        groupList: List<String>
    ): LiveData<List<SensorAndPlugSensor>> {
        return repositoryDB.getSensorsAndPlugSensorsByGroup(
            idHouse = idHouse,
            idDivision = idDivision,
            groupList = groupList
        )
    }

    fun getSensorAndBlindSensor(id: Int): LiveData<SensorAndBlindSensor> {
        return repositoryDB.getSensorAndBlindSensor(id = id)
    }

    fun getSensorsAndBlindSensorsByGroup(
        idHouse: String,
        idDivision: String,
        groupList: List<String>
    ): LiveData<List<SensorAndBlindSensor>> {
        return repositoryDB.getSensorsAndBlindSensorsByGroup(
            idHouse = idHouse,
            idDivision = idDivision,
            groupList = groupList
        )
    }

    fun resetLoading() {
        _isLoading.postValue(null)
    }
}