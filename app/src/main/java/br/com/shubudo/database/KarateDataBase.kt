package br.com.shubudo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.shubudo.database.dao.*
import br.com.shubudo.database.entities.DefesaPessoalEntity
import br.com.shubudo.database.entities.FaixaEntity
import br.com.shubudo.database.entities.KataEntity
import br.com.shubudo.database.entities.MovimentoEntity


@Database(
    version = 1,
    entities = [DefesaPessoalEntity::class, FaixaEntity::class, KataEntity::class, MovimentoEntity::class]
)
abstract class KarateDatabase : RoomDatabase() {

    abstract fun defesaPessoalDao(): DefesaPessoalDao
    abstract fun faixaDao(): FaixaDao
    abstract fun kataDao(): KataDao
    abstract fun movimentoDao(): MovimentoDao

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
