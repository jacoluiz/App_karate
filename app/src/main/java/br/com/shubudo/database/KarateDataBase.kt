package br.com.shubudo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.shubudo.database.dao.DefesaPessoalDao
import br.com.shubudo.database.dao.FaixaDao
import br.com.shubudo.database.dao.KataDao
import br.com.shubudo.database.dao.MovimentoDao
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
}
