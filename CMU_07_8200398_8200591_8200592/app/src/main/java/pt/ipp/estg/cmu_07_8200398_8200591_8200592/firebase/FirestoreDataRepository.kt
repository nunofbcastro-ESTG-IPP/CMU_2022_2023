package pt.ipp.estg.cmu_07_8200398_8200591_8200592.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.*
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.firebase.CollectionsNames
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.firebase.HouseAndUsers

class FirestoreDataRepository() {

    private val db: FirebaseFirestore
    private val storage: FirebaseStorage
    private val firebaseAuth: FirebaseAuth

    init {
        db = Firebase.firestore
        firebaseAuth = Firebase.auth
        storage = Firebase.storage
    }

    suspend fun getUserWithoutMe(email: String): User? {
        if (firebaseAuth.currentUser == null ||
            firebaseAuth.currentUser!!.email.equals(email)
        ) {
            return null
        }

        return getUser(email)
    }

    private suspend fun getUser(email: String): User? {
        var user: User? = null

        try {
            val queryReference =
                db.collection(CollectionsNames.usersCollection.collectionName)
                    .whereEqualTo(
                        CollectionsNames.usersCollection.fieldEmail, email
                    ).limit(1)

            val result = queryReference.get().await()

            if (!result.isEmpty) {
                user = result.documents[0].toObject(User::class.java)!!
            }
        } catch (_: Exception) {
        }

        return user
    }

    suspend fun getTypeDivisions(): List<TypeDivision> {
        val typeDivisions = mutableListOf<TypeDivision>()

        try {
            val queryReference =
                db.collection(CollectionsNames.typeDivisionsCollection.collectionName)

            val result = queryReference.get().await()

            for (document in result) {
                typeDivisions.add(
                    TypeDivision(
                        id = document.id.toInt(),
                        name = document.get(CollectionsNames.typeDivisionsCollection.fieldName) as String,
                        icon = document.get(CollectionsNames.typeDivisionsCollection.fieldIcon) as String,
                    )
                )
            }

        } catch (_: Exception) {
        }

        return typeDivisions
    }

    suspend fun getTypeSensors(): List<TypeSensor> {
        val typeSensor = mutableListOf<TypeSensor>()

        try {
            val queryReference =
                db.collection(CollectionsNames.typeSensorsCollection.collectionName)

            val result = queryReference.get().await()

            for (document in result) {
                typeSensor.add(
                    TypeSensor(
                        id = document.id.toInt(),
                        name = document.getString(CollectionsNames.typeSensorsCollection.fieldName)!!,
                        icon = document.getString(CollectionsNames.typeSensorsCollection.fieldIcon)!!,
                    )
                )
            }

        } catch (_: Exception) {
        }

        return typeSensor
    }

    suspend fun getHouses(): List<HouseAndUsers> {
        val houseAndUsersList = mutableListOf<HouseAndUsers>()

        try {
            val queryReference =
                db.collection(CollectionsNames.housesCollection.collectionName)
                    .whereArrayContainsAny(
                        CollectionsNames.housesCollection.fieldUsers,
                        listOf(firebaseAuth.currentUser!!.email)
                    )

            val result = queryReference.get().await()

            for (document in result) {
                val house = House(
                    id = document.id,
                    name = document.get(CollectionsNames.housesCollection.fieldName) as String,
                    photo = document.get(CollectionsNames.housesCollection.fieldPhoto) as String,
                    latitude = document.get(CollectionsNames.housesCollection.fieldLatitude) as Double,
                    longitude = document.get(CollectionsNames.housesCollection.fieldLongitude) as Double,
                )

                val users = mutableListOf<User>()

                for (email in document.get(CollectionsNames.housesCollection.fieldUsers) as List<*>) {
                    this.getUserWithoutMe(email as String)?.let { user ->
                        users.add(user)
                    }
                }

                houseAndUsersList.add(
                    HouseAndUsers(
                        house = house,
                        users = users,
                    )
                )
            }

        } catch (_: Exception) {
        }

        return houseAndUsersList
    }

    suspend fun addHouse(
        house: House
    ): String? {
        var result: String? = null

        var email = ""

        firebaseAuth.currentUser?.email?.let { emailFirebase ->
            email = emailFirebase
        }

        try {
            val collection = db.collection(CollectionsNames.housesCollection.collectionName)

            val houseToSave = hashMapOf(
                CollectionsNames.housesCollection.fieldName to house.name,
                CollectionsNames.housesCollection.fieldPhoto to house.photo,
                CollectionsNames.housesCollection.fieldLatitude to house.latitude,
                CollectionsNames.housesCollection.fieldLongitude to house.longitude,
                CollectionsNames.housesCollection.fieldUsers to listOf<String>(email),
            )

            result = collection.add(
                houseToSave
            ).await().id
        } catch (_: Exception) {
        }

        return result
    }

    suspend fun updateHouse(
        house: House
    ): Boolean {
        var result = false

        try {
            val collection =
                db.collection(CollectionsNames.housesCollection.collectionName).document(house.id)

            val houseToUpdate = hashMapOf<String, Any>(
                CollectionsNames.housesCollection.fieldName to house.name,
                CollectionsNames.housesCollection.fieldPhoto to house.photo,
                CollectionsNames.housesCollection.fieldLatitude to house.latitude,
                CollectionsNames.housesCollection.fieldLongitude to house.longitude,
            )

            collection.update(
                houseToUpdate
            ).await()

            result = true
        } catch (_: Exception) {
        }

        return result
    }

    suspend fun removeHouse(
        house: House
    ): Boolean {
        var email = ""
        firebaseAuth.currentUser?.email?.let { emailFirebase ->
            email = emailFirebase
        }

        return deleteHouseUser(
            HouseUser(
                idHouse = house.id,
                emailUser = email,
            )
        )
    }

    suspend fun addHouseUser(
        houseUser: HouseUser
    ): Boolean {
        if (getUser(houseUser.emailUser) == null) {
            return false
        }

        try {
            val refHouse = db.collection(
                CollectionsNames.housesCollection.collectionName
            ).document(houseUser.idHouse)

            refHouse.update(
                CollectionsNames.housesCollection.fieldUsers,
                FieldValue.arrayUnion(houseUser.emailUser)
            ).await()
            return true
        } catch (_: Exception) {
        }
        return false
    }

    suspend fun deleteHouseUser(
        houseUser: HouseUser
    ): Boolean {
        try {
            val refHouse = db.collection(
                CollectionsNames.housesCollection.collectionName
            ).document(houseUser.idHouse)

            refHouse.update(
                CollectionsNames.housesCollection.fieldUsers,
                FieldValue.arrayRemove(houseUser.emailUser)
            ).await()
            return true
        } catch (_: Exception) {
        }
        return false
    }

    suspend fun getDivisions(
        idHouse: String
    ): List<Division> {
        val divisionsList = mutableListOf<Division>()

        try {
            val queryReference =
                db.collection(CollectionsNames.housesCollection.collectionName)
                    .document(idHouse)
                    .collection(CollectionsNames.divisionsCollection.collectionName)

            val result = queryReference.get().await()

            for (document in result) {
                divisionsList.add(
                    Division(
                        id = document.id,
                        idHouse = idHouse,
                        name = document.get(CollectionsNames.divisionsCollection.fieldName) as String,
                        nDevices = 0,
                        idTypeDivision = document.getLong(CollectionsNames.divisionsCollection.fieldTypeDivision)!!
                            .toInt(),
                    )
                )
            }

        } catch (_: Exception) {
        }

        return divisionsList
    }

    suspend fun addDivision(
        division: Division
    ): String? {
        var result: String? = null

        try {
            val collection = db
                .collection(CollectionsNames.housesCollection.collectionName)
                .document(division.idHouse)
                .collection(CollectionsNames.divisionsCollection.collectionName)

            val exist = collection
                .whereEqualTo(
                    CollectionsNames.divisionsCollection.fieldName, division.name
                )
                .get().await()

            if (!exist.isEmpty) {
                throw Exception("Yes there is")
            }

            val divisionToAdd = hashMapOf<String, Any>(
                CollectionsNames.divisionsCollection.fieldName to division.name,
                CollectionsNames.divisionsCollection.fieldTypeDivision to division.idTypeDivision,
            )

            result = collection
                .add(
                    divisionToAdd
                ).await().id
        } catch (_: Exception) {
        }

        return result
    }

    suspend fun updateDivision(
        division: Division
    ): Boolean {
        var result: Boolean = false

        try {
            val collection = db
                .collection(CollectionsNames.housesCollection.collectionName)
                .document(division.idHouse)
                .collection(CollectionsNames.divisionsCollection.collectionName)

            val exist = collection
                .whereEqualTo(
                    CollectionsNames.divisionsCollection.fieldName, division.name
                )
                .get().await()

            if (!exist.isEmpty) {
                throw Exception("Yes there is")
            }

            val divisionToAdd = hashMapOf<String, Any>(
                CollectionsNames.divisionsCollection.fieldName to division.name,
                CollectionsNames.divisionsCollection.fieldTypeDivision to division.idTypeDivision,
            )

            collection
                .document(division.id)
                .update(
                    divisionToAdd
                ).await()
            result = true
        } catch (_: Exception) {
        }

        return result
    }

    suspend fun removeDivision(
        division: Division
    ): Boolean {
        var result: Boolean = false

        try {
            val collection = db
                .collection(CollectionsNames.housesCollection.collectionName)
                .document(division.idHouse)
                .collection(CollectionsNames.divisionsCollection.collectionName)
                .document(division.id)

            collection.delete().await()
            result = true
        } catch (_: Exception) {
        }

        return result
    }

    suspend fun getGroups(
        idHouse: String,
        idDivision: String
    ): List<Group> {
        val groupsList = mutableListOf<Group>()

        try {
            val queryReference =
                db.collection(CollectionsNames.housesCollection.collectionName)
                    .document(idHouse)
                    .collection(CollectionsNames.divisionsCollection.collectionName)
                    .document(idDivision)
                    .collection(CollectionsNames.groupsCollection.collectionName)

            val result = queryReference.get().await()

            for (document in result) {
                val group = Group(
                    id = document.id,
                    idHouse = idHouse,
                    idDivision = idDivision,
                    idTypeSensor = document.getLong(CollectionsNames.groupsCollection.fieldTypeSensor)!!
                        .toInt(),
                    name = document.get(CollectionsNames.groupsCollection.fieldName) as String,
                )

                groupsList.add(
                    group
                )
            }

        } catch (_: Exception) {
        }

        return groupsList
    }

    suspend fun addGroup(
        group: Group
    ): String? {
        var result: String? = null

        try {
            val collection = db
                .collection(CollectionsNames.housesCollection.collectionName)
                .document(group.idHouse)
                .collection(CollectionsNames.divisionsCollection.collectionName)
                .document(group.idDivision)
                .collection(CollectionsNames.groupsCollection.collectionName)

            val exist = collection
                .whereEqualTo(
                    CollectionsNames.groupsCollection.fieldName, group.name
                )
                .whereEqualTo(
                    CollectionsNames.groupsCollection.fieldTypeSensor, group.idTypeSensor
                )
                .get().await()

            if (!exist.isEmpty) {
                throw Exception("Yes there is")
            }

            val groupToAdd = hashMapOf<String, Any>(
                CollectionsNames.groupsCollection.fieldName to group.name,
                CollectionsNames.groupsCollection.fieldTypeSensor to group.idTypeSensor,
            )

            result = collection
                .add(
                    groupToAdd
                ).await().id
        } catch (_: Exception) {
        }

        return result
    }

    suspend fun verifyIfExistIp(
        idHouse: String,
        ip: String,
    ): Boolean {
        try {
            val queryReferenceDivision =
                db.collection(CollectionsNames.housesCollection.collectionName)
                    .document(idHouse)
                    .collection(CollectionsNames.divisionsCollection.collectionName)

            val resultDivisions = queryReferenceDivision.get().await()

            for (division in resultDivisions) {
                val queryReferenceGroup =
                    queryReferenceDivision
                        .document(division.id)
                        .collection(CollectionsNames.groupsCollection.collectionName)

                val resultGroups = queryReferenceGroup.get().await()
                for (group in resultGroups) {
                    val queryExist = queryReferenceGroup
                        .document(group.id)
                        .collection(CollectionsNames.sensorsCollection.collectionName)
                        .whereEqualTo(CollectionsNames.sensorsCollection.fieldIp, ip)
                        .limit(1)

                    val resultSensors = queryExist.get().await()

                    if (resultSensors.size() != 0) {
                        return true
                    }
                }
            }
        } catch (_: Exception) {
        }

        return false
    }

    suspend fun addSensor(
        sensor: Sensor
    ): String? {
        var result: String? = null
        try {
            if (verifyIfExistIp(sensor.idHouse, sensor.ip)) {
                return null
            }

            val collection = db
                .collection(CollectionsNames.housesCollection.collectionName)
                .document(sensor.idHouse)
                .collection(CollectionsNames.divisionsCollection.collectionName)
                .document(sensor.idDivision)
                .collection(CollectionsNames.groupsCollection.collectionName)
                .document(sensor.idGroup)
                .collection(CollectionsNames.sensorsCollection.collectionName)

            val sensorToAdd = hashMapOf<String, Any>(
                CollectionsNames.sensorsCollection.fieldIp to sensor.ip,
            )

            result = collection.add(
                sensorToAdd
            ).await().id
        } catch (_: Exception) {
        }
        return result
    }

    suspend fun removeSensor(
        sensor: Sensor
    ): Boolean {
        var result: Boolean = false
        try {
            val collection = db
                .collection(CollectionsNames.housesCollection.collectionName)
                .document(sensor.idHouse)
                .collection(CollectionsNames.divisionsCollection.collectionName)
                .document(sensor.idDivision)
                .collection(CollectionsNames.groupsCollection.collectionName)
                .document(sensor.idGroup)
                .collection(CollectionsNames.sensorsCollection.collectionName)
                .document(sensor.id)

            collection.delete().await()
            result = true
        } catch (_: Exception) {
        }
        return result
    }

    suspend fun getSensors(
        idHouse: String,
        idDivision: String,
        idGroup: String,
        idTypeSensor: Int,
    ): List<Sensor> {
        val sensorsList = mutableListOf<Sensor>()

        try {
            val queryReference = db
                .collection(CollectionsNames.housesCollection.collectionName)
                .document(idHouse)
                .collection(CollectionsNames.divisionsCollection.collectionName)
                .document(idDivision)
                .collection(CollectionsNames.groupsCollection.collectionName)
                .document(idGroup)
                .collection(CollectionsNames.sensorsCollection.collectionName)

            val result = queryReference.get().await()

            for (document in result) {
                val sensor = Sensor(
                    id = document.id,
                    idHouse = idHouse,
                    idDivision = idDivision,
                    idTypeSensor = idTypeSensor,
                    idGroup = idGroup,
                    ip = document.get(CollectionsNames.sensorsCollection.fieldIp) as String,
                    consumption = 0.0,
                )

                sensorsList.add(
                    sensor
                )
            }

        } catch (_: Exception) {
        }

        return sensorsList
    }

}