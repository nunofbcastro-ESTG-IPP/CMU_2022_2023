package pt.ipp.estg.cmu_07_8200398_8200591_8200592.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.*

@Database(
    entities = [
        House::class,
        User::class,
        HouseUser::class,
        TypeDivision::class,
        Division::class,
        Group::class,
        TypeSensor::class,
        Sensor::class,
        LightSensor::class,
        PlugSensor::class,
        BlindSensor::class,
    ], version = 4, exportSchema = false
)
abstract class SmartHouseDatabase : RoomDatabase() {
    abstract fun getSmartHouseDao(): SmartHouseDao

    companion object {
        @Volatile
        private var INSTANCE: SmartHouseDatabase? = null

        fun getDatabase(context: Context): SmartHouseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SmartHouseDatabase::class.java,
                    "smarthouse_database.db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}