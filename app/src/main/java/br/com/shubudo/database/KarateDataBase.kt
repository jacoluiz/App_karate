package br.com.shubudo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.shubudo.database.dao.FaixaDao
import br.com.shubudo.database.entities.FaixaEntity


@Database(version = 1, entities = [FaixaEntity::class])
abstract class KarateDatabase : RoomDatabase() {

    abstract fun faixaDao(): FaixaDao

    companion object {
        fun getDataBase(context: Context): KarateDatabase {
            return Room.databaseBuilder(
                context,
                KarateDatabase::class.java,
                "karate.db"
            ).build()
        }
    }
}
