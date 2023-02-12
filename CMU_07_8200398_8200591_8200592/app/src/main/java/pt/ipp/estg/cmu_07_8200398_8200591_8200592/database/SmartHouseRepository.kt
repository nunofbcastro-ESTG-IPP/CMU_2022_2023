package pt.ipp.estg.cmu_07_8200398_8200591_8200592.database

import androidx.lifecycle.LiveData

import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.relations.SensorAndBlindSensor
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.relations.SensorAndLightSensor
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.relations.SensorAndPlugSensor
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.*

class SmartHouseRepository(
    private val smartHouseDao: SmartHouseDao
) {
    fun getHouses(): LiveData<List<House>> {
        return smartHouseDao.getAllHouses()
    }

    fun getHouse(
        idHouse: String
    ): LiveData<House> {
        return smartHouseDao.getHouse(idHouse)
    }

    fun getHouseConsume(
        idHouse: String
    ): LiveData<Double> {
        return smartHouseDao.getHouseConsume(
            idHouse = idHouse
        )
    }

    fun getTypesDivisions(): LiveData<List<TypeDivision>> {
        return smartHouseDao.getAllTypesDivisions()
    }

    fun getTypeDivision(id: Int): LiveData<TypeDivision> {
        return smartHouseDao.getTypeDivision(id = id)
    }

    suspend fun addTypeDivision(
        typeDivision: TypeDivision
    ) {
        smartHouseDao.insertTypeDivision(
            typeDivision = typeDivision
        )
    }

    suspend fun removeTypeDivisionsIfNotExist(
        ids: List<Int>
    ) {
        smartHouseDao.removeTypeDivisionsIfNotExist(
            ids = ids
        )
    }

    suspend fun addHouse(
        house: House
    ) {
        smartHouseDao.insertHouse(
            house = house
        )
    }

    suspend fun upsertHouse(
        house: House
    ) {
        smartHouseDao.upsertHouse(
            house = house
        )
    }

    suspend fun updateHouse(
        house: House
    ) {
        smartHouseDao.updateHouse(
            house = house
        )
    }

    suspend fun removeHouse(
        house: House
    ) {
        smartHouseDao.deleteHouse(
            house = house
        )
    }

    suspend fun removeHousesIfNotExist(
        ids: List<String>
    ) {
        smartHouseDao.removeHousesIfNotExist(
            ids = ids
        )
    }

    fun getAllDivisions(
        idHouse: String
    ): LiveData<List<Division>> {
        return smartHouseDao.getAllDivisions(
            idHouse = idHouse
        )
    }

    fun getHouseDivisions(
        idHouse: String
    ): LiveData<List<Division>> {
        return smartHouseDao.getHouseDivisions(
            idHouse = idHouse
        )
    }

    fun getDivisionById(
        idDivision: String,
        idHouse: String
    ): LiveData<Division> {
        return smartHouseDao.getDivisionById(idDivision = idDivision, idHouse = idHouse)
    }

    suspend fun addDivision(
        division: Division
    ) {
        smartHouseDao.insertDivision(
            division = division
        )
    }

    suspend fun setNDevices(
        idDivision: String,
        idHouse: String,
        nDevices: Int,
    ) {
        smartHouseDao.setNDevices(
            idDivision = idDivision,
            idHouse = idHouse,
            nDevices = nDevices,
        )
    }

    suspend fun upsertDivision(
        division: Division
    ) {
        smartHouseDao.upsertDivision(
            division = division
        )
    }

    suspend fun updateDivision(
        division: Division
    ) {
        smartHouseDao.updateDivision(
            division = division
        )
    }

    suspend fun removeDivision(
        division: Division
    ) {
        smartHouseDao.deleteDivision(
            division = division
        )
    }

    suspend fun removeDevicesIfNotExist(
        ids: List<String>,
        idHouse: String,
    ) {
        smartHouseDao.removeDevicesIfNotExist(
            ids = ids,
            idHouse = idHouse,
        )
    }

    fun getTypesSensors(): LiveData<List<TypeSensor>> {
        return smartHouseDao.getAllTypesSensors()
    }

    fun getTypeSensor(
        id: Int
    ): LiveData<TypeSensor> {
        return smartHouseDao.getTypeSensor(id = id)
    }

    suspend fun addTypeSensor(
        typeSensor: TypeSensor
    ) {
        smartHouseDao.insertTypeSensor(
            typeSensor = typeSensor
        )
    }

    suspend fun removeTypeSensorsIfNotExist(
        ids: List<Int>
    ) {
        smartHouseDao.removeTypeSensorsIfNotExist(
            ids = ids
        )
    }

    suspend fun addGroup(
        group: Group
    ) {
        smartHouseDao.addGroup(
            group = group
        )
    }

    suspend fun upsertGroup(
        group: Group
    ) {
        smartHouseDao.upsertGroup(
            group = group
        )
    }

    suspend fun removeGroupsIfNotExist(
        ids: List<String>,
        idHouse: String,
        idDivision: String,
    ) {
        smartHouseDao.removeGroupsIfNotExist(
            ids = ids,
            idHouse = idHouse,
            idDivision = idDivision,
        )
    }

    fun getGroup(
        idHouse: String,
        idDivision: String,
        idTypeSensor: Int,
        name: String
    ): LiveData<Group> {
        return smartHouseDao.getGroup(
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
        return smartHouseDao.getGroups(
            idHouse = idHouse,
            idDivision = idDivision,
            idTypeSensor = idTypeSensor
        )
    }

    fun getAllSensors(): LiveData<List<Sensor>> {
        return smartHouseDao.getAllSensors()
    }

    fun getDivisionSensors(
        idHouse: String,
        idDivision: String
    ): LiveData<List<Sensor>> {
        return smartHouseDao.getDivisionSensors(
            idHouse = idHouse,
            idDivision = idDivision
        )
    }

    fun getDivisionTypeSensor(
        idHouse: String,
        idDivision: String
    ): LiveData<List<TypeSensor>> {
        return smartHouseDao.getDivisionTypeSensor(
            idHouse = idHouse,
            idDivision = idDivision
        )
    }

    suspend fun removeSensorsIfNotExist(
        ids: List<String>,
        idHouse: String,
        idDivision: String,
        idGroup: String,
    ){
        smartHouseDao.removeSensorsIfNotExist(
            ids = ids,
            idHouse = idHouse,
            idDivision = idDivision,
            idGroup = idGroup,
        )
    }

    fun getPlugSensor(
        idSensor: Int
    ): LiveData<PlugSensor> {
        return smartHouseDao.getPlugSensor(
            idSensor = idSensor
        )
    }

    suspend fun addPlugSensor(
        sensor: Sensor,
        plugSensor: PlugSensor
    ): Boolean {
        val isInserted = smartHouseDao.insertSensorAndPlugSensor(
            sensor = sensor,
            plugSensor = plugSensor
        )

        return isInserted > 0
    }

    suspend fun updateSensorAndPlugSensor(
        sensor: Sensor,
        plugSensor: PlugSensor
    ) {
        smartHouseDao.updateSensorAndPlugSensor(
            sensor = sensor,
            plugSensor = plugSensor
        )
    }

    suspend fun updateIpSensor(
        idSensor: String,
        ip: String,
    ){
        smartHouseDao.updateIpSensor(
            idSensor = idSensor,
            ip = ip
        )
    }

    fun getBlindSensor(
        idSensor: Int
    ): LiveData<BlindSensor> {
        return smartHouseDao.getBlindSensor(
            idSensor = idSensor
        )
    }

    suspend fun addBlindSensor(
        sensor: Sensor,
        blindSensor: BlindSensor
    ): Boolean {
        val isInserted = smartHouseDao.insertSensorAndBlindSensor(
            sensor = sensor,
            blindSensor = blindSensor
        )
        return isInserted > 0
    }

    suspend fun updateSensorAndBlindSensor(
        sensor: Sensor,
        blindSensor: BlindSensor
    ) {
        smartHouseDao.updateSensorAndBlindSensor(
            sensor = sensor,
            blindSensor = blindSensor
        )
    }

    fun getLightSensor(
        idSensor: Int
    ): LiveData<LightSensor> {
        return smartHouseDao.getLightSensor(
            idSensor = idSensor
        )
    }

    suspend fun addLightSensor(
        sensor: Sensor,
        lightSensor: LightSensor
    ): Boolean {
        val isInserted = smartHouseDao.insertSensorAndLightSensor(
            sensor = sensor,
            lightSensor = lightSensor
        )
        return isInserted > 0
    }

    suspend fun updateSensorAndLightSensor(
        sensor: Sensor,
        lightSensor: LightSensor
    ) {
        smartHouseDao.updateSensorAndLightSensor(
            sensor = sensor,
            lightSensor = lightSensor
        )
    }

    suspend fun removeSensor(
        sensor: Sensor
    ) {
        smartHouseDao.deleteSensorAndDecrement(
            sensor = sensor
        )
    }

    fun getUsersHouse(
        idHouse: String
    ): LiveData<List<User>> {
        return smartHouseDao.getUsersHouse(
            idHouse = idHouse
        )
    }

    suspend fun addHouseUser(
        houseUser: HouseUser
    ) {
        smartHouseDao.insertHouseUser(
            houseUser = houseUser
        )
    }

    suspend fun deleteHouseUser(
        houseUser: HouseUser
    ) {
        smartHouseDao.deleteHouseUser(
            houseUser = houseUser
        )
    }

    suspend fun removeHouseUsersIfNotExist(
        emailsUsers: List<String>,
        idHouse: String,
    ) {
        smartHouseDao.removeHouseUsersIfNotExist(
            emailsUsers = emailsUsers,
            idHouse = idHouse
        )
    }

    suspend fun addUser(
        user: User
    ) {
        smartHouseDao.insertUser(
            user = user
        )
    }

    fun getUser(email: String): LiveData<User> {
        return smartHouseDao.getUser(email)
    }

    fun getUsers(): LiveData<List<User>> {
        return smartHouseDao.getUsers()
    }

    suspend fun clearDatabase() {
        smartHouseDao.deleteTables()
    }

    suspend fun getSensor(
        id: String
    ): Sensor? {
        return smartHouseDao.getSensor(id)
    }

    fun getSensorAndLightSensor(id: Int): LiveData<SensorAndLightSensor> {
        return smartHouseDao.getSensorAndLightSensor(id = id)
    }

    fun getSensorsAndLightSensorsByGroup(
        idHouse: String,
        idDivision: String,
        groupList: List<String>
    ): LiveData<List<SensorAndLightSensor>> {
        return smartHouseDao.getSensorsAndLightSensorsByGroup(
            idHouse = idHouse,
            idDivision = idDivision,
            groupList = groupList
        )
    }

    fun getSensorAndPlugSensor(id: Int): LiveData<SensorAndPlugSensor> {
        return smartHouseDao.getSensorAndPlugSensor(id = id)
    }

    fun getSensorsAndPlugSensorsByGroup(
        idHouse: String,
        idDivision: String,
        groupList: List<String>
    ): LiveData<List<SensorAndPlugSensor>> {
        return smartHouseDao.getSensorsAndPlugSensorsByGroup(
            idHouse = idHouse,
            idDivision = idDivision,
            groupList = groupList
        )
    }

    fun getSensorAndBlindSensor(id: Int): LiveData<SensorAndBlindSensor> {
        return smartHouseDao.getSensorAndBlindSensor(id = id)
    }

    fun getSensorsAndBlindSensorsByGroup(
        idHouse: String,
        idDivision: String,
        groupList: List<String>
    ): LiveData<List<SensorAndBlindSensor>> {
        return smartHouseDao.getSensorsAndBlindSensorsByGroup(
            idHouse = idHouse,
            idDivision = idDivision,
            groupList = groupList
        )
    }
}