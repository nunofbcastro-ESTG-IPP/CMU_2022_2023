package pt.ipp.estg.cmu_07_8200398_8200591_8200592.database

import androidx.lifecycle.LiveData
import androidx.room.*

import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.*
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.relations.SensorAndBlindSensor
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.relations.SensorAndLightSensor
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.relations.SensorAndPlugSensor

@Dao
interface SmartHouseDao {
    //Query's houses
    @Query(
        "SELECT * FROM Houses"
    )
    fun getAllHouses(): LiveData<List<House>>

    @Query(
        "SELECT * FROM Houses WHERE id = :idHouse"
    )
    fun getHouse(
        idHouse: String
    ): LiveData<House>

    @Insert(
        onConflict = OnConflictStrategy.IGNORE
    )
    suspend fun insertHouse(
        house: House
    ): Long

    @Upsert
    suspend fun upsertHouse(
        house: House
    )

    @Query(
        "SELECT SUM(Sensors.consumption) AS Consume FROM SENSORS, HOUSES WHERE SENSORS.idHouse = HOUSES.id AND HOUSES.id = :idHouse"
    )
    fun getHouseConsume(
        idHouse: String
    ): LiveData<Double>

    @Update
    suspend fun updateHouse(
        house: House
    )

    @Delete
    suspend fun deleteHouse(
        house: House
    )

    @Query(
        """
            DELETE FROM HOUSES WHERE id NOT IN (:ids)
        """
    )
    suspend fun removeHousesIfNotExist(
        ids: List<String>
    )

    //Query's TypesDivisions
    @Query(
        "SELECT * FROM TypesDivisions"
    )
    fun getAllTypesDivisions(): LiveData<List<TypeDivision>>

    @Query(
        "SELECT * FROM TypesDivisions WHERE id= :id"
    )
    fun getTypeDivision(id: Int): LiveData<TypeDivision>

    @Upsert
    suspend fun insertTypeDivision(
        typeDivision: TypeDivision
    )

    @Query(
        """
            DELETE FROM TypesDivisions WHERE id NOT IN (:ids)
        """
    )
    suspend fun removeTypeDivisionsIfNotExist(ids: List<Int>)

    //Query's divisions
    @Query(
        "SELECT * FROM Divisions WHERE idHouse = :idHouse"
    )
    fun getAllDivisions(
        idHouse: String
    ): LiveData<List<Division>>

    @Query(
        "SELECT * FROM Divisions WHERE idHouse = :idHouse"
    )
    fun getHouseDivisions(
        idHouse: String
    ): LiveData<List<Division>>

    @Query(
        "SELECT * FROM Divisions WHERE id= :idDivision AND idHouse = :idHouse"
    )
    fun getDivisionById(
        idDivision: String,
        idHouse: String
    ): LiveData<Division>

    @Insert(
        onConflict = OnConflictStrategy.IGNORE
    )
    suspend fun insertDivision(
        division: Division
    ): Long

    @Upsert
    suspend fun upsertDivision(
        division: Division
    )

    @Query(
        "UPDATE Divisions SET nDevices = nDevices+1 WHERE id= :idDivision AND idHouse = :idHouse"
    )
    suspend fun addDeviceToDivision(
        idDivision: String,
        idHouse: String
    )

    @Query(
        "UPDATE Divisions SET nDevices = :nDevices WHERE id= :idDivision AND idHouse = :idHouse"
    )
    suspend fun setNDevices(
        idDivision: String,
        idHouse: String,
        nDevices: Int,
    )


    @Query(
        "UPDATE Divisions SET nDevices= nDevices-1 WHERE id= :idDivision AND idHouse = :idHouse"
    )
    suspend fun removeDeviceFromDivision(
        idDivision: String,
        idHouse: String
    )

    @Query(
        """
            DELETE FROM Divisions WHERE idHouse = :idHouse AND id NOT IN (:ids)
        """
    )
    suspend fun removeDevicesIfNotExist(
        ids: List<String>,
        idHouse: String,
    )

    @Update
    suspend fun updateDivision(
        division: Division
    )

    @Delete
    suspend fun deleteDivision(
        division: Division
    )

    //Query's TypesSensors
    @Query(
        "SELECT * FROM TypesSensors"
    )
    fun getAllTypesSensors(): LiveData<List<TypeSensor>>

    @Query(
        "SELECT * FROM TypesSensors WHERE id= :id"
    )
    fun getTypeSensor(id: Int): LiveData<TypeSensor>

    @Upsert
    suspend fun insertTypeSensor(
        typeSensor: TypeSensor
    )

    @Query(
        """
            DELETE FROM TypesSensors WHERE id NOT IN (:ids)
        """
    )
    suspend fun removeTypeSensorsIfNotExist(ids: List<Int>)

    //Query's groups
    @Insert(
        onConflict = OnConflictStrategy.IGNORE
    )
    suspend fun addGroup(
        group: Group
    ): Long

    @Upsert
    suspend fun upsertGroup(
        group: Group
    ): Long

    @Query(
        """
            DELETE 
            FROM Groups 
            WHERE 
            idHouse= :idHouse
            AND idDivision= :idDivision 
            AND id NOT IN (:ids)
        """
    )
    suspend fun removeGroupsIfNotExist(
        ids: List<String>,
        idHouse: String,
        idDivision: String,
    )

    @Query(
        """
            SELECT * 
            FROM Groups 
            WHERE idHouse= :idHouse 
            AND idDivision= :idDivision 
            AND idTypeSensor= :idTypeSensor 
            AND name= :name
        """
    )
    fun getGroup(
        idHouse: String,
        idDivision: String,
        idTypeSensor: Int,
        name: String
    ): LiveData<Group>

    @Query(
        "SELECT * FROM Groups WHERE idHouse= :idHouse AND idDivision= :idDivision AND idTypeSensor= :idTypeSensor"
    )
    fun getGroups(
        idHouse: String,
        idDivision: String,
        idTypeSensor: Int
    ): LiveData<List<Group>>

    //Query's sensors
    @Query(
        "SELECT * FROM Sensors"
    )
    fun getAllSensors(): LiveData<List<Sensor>>

    @Query(
        "SELECT * FROM Sensors WHERE idHouse = :idHouse and idDivision = :idDivision"
    )
    fun getDivisionSensors(
        idHouse: String,
        idDivision: String
    ): LiveData<List<Sensor>>

    @Query(
        "SELECT DISTINCT(TypesSensors.id) ,TypesSensors.* FROM TypesSensors, Sensors WHERE TypesSensors.id = Sensors.idTypeSensor AND Sensors.idHouse = :idHouse AND Sensors.idDivision = :idDivision"
    )
    fun getDivisionTypeSensor(
        idHouse: String,
        idDivision: String
    ): LiveData<List<TypeSensor>>

    @Insert(
        onConflict = OnConflictStrategy.IGNORE
    )
    suspend fun insertSensor(
        sensor: Sensor
    ): Long

    @Query(
        """
            DELETE 
            FROM Sensors 
            WHERE 
            idHouse = :idHouse
            AND idDivision = :idDivision
            AND idGroup = :idGroup
            AND id NOT IN (:ids)
        """
    )
    suspend fun removeSensorsIfNotExist(
        ids: List<String>,
        idHouse: String,
        idDivision: String,
        idGroup: String,
    )

    @Delete
    suspend fun deleteSensor(
        sensor: Sensor
    )

    @Update
    suspend fun updateSensor(
        sensor: Sensor
    )

    @Query(
        """
            UPDATE Sensors 
            SET ip = :ip
            WHERE 
            id = :idSensor
        """
    )
    suspend fun updateIpSensor(
        idSensor: String,
        ip: String,
    )

    @Transaction
    suspend fun deleteSensorAndDecrement(
        sensor: Sensor
    ) {
        deleteSensor(
            sensor = sensor
        )
        removeDeviceFromDivision(
            idDivision = sensor.idDivision,
            idHouse = sensor.idHouse
        )
    }

    //Query's plug sensors
    @Query(
        "SELECT * FROM PlugSensors WHERE idSensor = :idSensor"
    )
    fun getPlugSensor(
        idSensor: Int
    ): LiveData<PlugSensor>

    @Insert(
        onConflict = OnConflictStrategy.IGNORE
    )
    suspend fun insertPlugSensor(
        plugSensor: PlugSensor
    ): Long

    @Transaction
    suspend fun insertSensorAndPlugSensor(
        sensor: Sensor,
        plugSensor: PlugSensor
    ): Long {
        var isInserted: Long = -1
        if (
            sensor.id == plugSensor.idSensor
        ) {
            isInserted = insertSensor(sensor)

            if (isInserted >= 0L) {
                isInserted = insertPlugSensor(plugSensor)
            }

            if (isInserted < 0L) {
                deleteSensor(sensor)
            } else {
                addDeviceToDivision(
                    idDivision = sensor.idDivision,
                    idHouse = sensor.idHouse
                )
            }
        }
        return isInserted
    }

    @Update
    suspend fun updatePlugSensor(
        plugSensor: PlugSensor
    )

    @Transaction
    suspend fun updateSensorAndPlugSensor(
        sensor: Sensor,
        plugSensor: PlugSensor
    ) {
        if (sensor.id == plugSensor.idSensor) {
            updateSensor(sensor)
            updatePlugSensor(plugSensor)
        }
    }

    //Query's light sensors
    @Query(
        "SELECT * FROM lightSensors WHERE idSensor = :idSensor"
    )
    fun getLightSensor(
        idSensor: Int
    ): LiveData<LightSensor>

    @Insert(
        onConflict = OnConflictStrategy.IGNORE
    )
    suspend fun insertLightSensor(
        lightSensor: LightSensor
    ): Long

    @Transaction
    suspend fun insertSensorAndLightSensor(
        sensor: Sensor,
        lightSensor: LightSensor
    ): Long {
        var isInserted: Long = 0
        if (
            sensor.id == lightSensor.idSensor
        ) {
            isInserted = insertSensor(sensor)

            if (isInserted >= 0L) {
                isInserted = insertLightSensor(lightSensor)
            }

            if (isInserted < 0L) {
                deleteSensor(sensor)
            } else {
                addDeviceToDivision(
                    idDivision = sensor.idDivision,
                    idHouse = sensor.idHouse
                )
            }
        }
        return isInserted
    }

    @Update
    suspend fun updateLightSensor(
        lightSensor: LightSensor
    )

    @Transaction
    suspend fun updateSensorAndLightSensor(
        sensor: Sensor,
        lightSensor: LightSensor
    ) {
        if (sensor.id == lightSensor.idSensor) {
            updateSensor(sensor)
            updateLightSensor(lightSensor)
        }
    }

    //Query's blind sensors
    @Query(
        "SELECT * FROM BlindSensors WHERE idSensor = :idSensor"
    )
    fun getBlindSensor(
        idSensor: Int
    ): LiveData<BlindSensor>

    @Insert(
        onConflict = OnConflictStrategy.IGNORE
    )
    suspend fun insertBlindSensor(
        blindSensor: BlindSensor
    ): Long

    @Transaction
    suspend fun insertSensorAndBlindSensor(
        sensor: Sensor,
        blindSensor: BlindSensor
    ): Long {
        var isInserted: Long = 0
        if (
            sensor.id == blindSensor.idSensor
        ) {
            isInserted = insertSensor(sensor)

            if (isInserted >= 0L) {
                isInserted = insertBlindSensor(blindSensor)
            }

            if (isInserted < 0L) {
                deleteSensor(sensor)
            } else {
                addDeviceToDivision(
                    idDivision = sensor.idDivision,
                    idHouse = sensor.idHouse
                )
            }
        }
        return isInserted
    }

    @Update
    suspend fun updateBlindSensor(
        blindSensor: BlindSensor
    )

    @Transaction
    suspend fun updateSensorAndBlindSensor(
        sensor: Sensor,
        blindSensor: BlindSensor
    ) {
        if (sensor.id == blindSensor.idSensor) {
            updateSensor(sensor)
            updateBlindSensor(blindSensor)
        }
    }

    //Query's users
    @Query(
        "SELECT Users.* FROM HousesUsers, Users WHERE HousesUsers.emailUser = Users.email and HousesUsers.idHouse = :idHouse"
    )
    fun getUsersHouse(
        idHouse: String
    ): LiveData<List<User>>

    @Insert(
        onConflict = OnConflictStrategy.IGNORE
    )
    suspend fun insertHouseUser(
        houseUser: HouseUser
    ): Long

    @Upsert
    suspend fun insertUser(
        user: User
    )

    @Query(
        """
            DELETE FROM HousesUsers WHERE HousesUsers.idHouse = :idHouse and emailUser NOT IN (:emailsUsers)
        """
    )
    suspend fun removeHouseUsersIfNotExist(
        emailsUsers: List<String>,
        idHouse: String,
    )

    @Query(
        "SELECT * FROM Users WHERE email= :email"
    )
    fun getUser(email: String): LiveData<User>

    @Query(
        "SELECT * FROM Users"
    )
    fun getUsers(): LiveData<List<User>>

    @Delete
    suspend fun deleteHouseUser(houseUser: HouseUser)

    @Query(
        """
            SELECT * 
            FROM Sensors
            WHERE id = :id
            LIMIT 1
        """
    )
    suspend fun getSensor(
        id: String
    ): Sensor?

    @Transaction
    @Query("SELECT * FROM Sensors WHERE id = :id")
    fun getSensorAndLightSensor(id: Int): LiveData<SensorAndLightSensor>

    @Transaction
    @Query("SELECT * FROM Sensors WHERE idHouse = :idHouse AND idDivision= :idDivision AND idGroup IN (:groupList)")
    fun getSensorsAndLightSensorsByGroup(
        idHouse: String,
        idDivision: String,
        groupList: List<String>
    ): LiveData<List<SensorAndLightSensor>>

    @Transaction
    @Query("SELECT * FROM Sensors WHERE id = :id")
    fun getSensorAndPlugSensor(id: Int): LiveData<SensorAndPlugSensor>

    @Transaction
    @Query("SELECT * FROM Sensors WHERE idHouse = :idHouse AND idDivision= :idDivision AND idGroup IN (:groupList)")
    fun getSensorsAndPlugSensorsByGroup(
        idHouse: String,
        idDivision: String,
        groupList: List<String>
    ): LiveData<List<SensorAndPlugSensor>>

    @Transaction
    @Query("SELECT * FROM Sensors WHERE id = :id")
    fun getSensorAndBlindSensor(id: Int): LiveData<SensorAndBlindSensor>

    @Transaction
    @Query("SELECT * FROM Sensors WHERE idHouse = :idHouse AND idDivision= :idDivision AND idGroup IN (:groupList)")
    fun getSensorsAndBlindSensorsByGroup(
        idHouse: String,
        idDivision: String,
        groupList: List<String>
    ): LiveData<List<SensorAndBlindSensor>>

    //Drop tables
    @Query(
        "Delete From Houses"
    )
    suspend fun deleteTableHouses()

    @Query(
        "Delete From Users"
    )
    suspend fun deleteTableUsers()

    @Query(
        "Delete From TypesDivisions"
    )
    suspend fun deleteTableTypesDivisions()

    @Query(
        "Delete From TypesSensors"
    )
    suspend fun deleteTableTypesSensors()


    @Transaction
    suspend fun deleteTables() {
        deleteTableHouses()
        deleteTableUsers()
        deleteTableTypesSensors()
        deleteTableTypesDivisions()
    }
}