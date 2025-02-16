package br.com.shubudo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.shubudo.database.dao.AvisoDao
import br.com.shubudo.database.dao.DefesaPessoalDao
import br.com.shubudo.database.dao.DefesaPessoalExtraBannerDao
import br.com.shubudo.database.dao.FaixaDao
import br.com.shubudo.database.dao.KataDao
import br.com.shubudo.database.dao.MovimentoDao
import br.com.shubudo.database.dao.ProjecaoDao
import br.com.shubudo.database.dao.SequenciaDeCombateDao
import br.com.shubudo.database.dao.UsuarioDao
import br.com.shubudo.database.entities.AvisoEntity
import br.com.shubudo.database.entities.DefesaPessoalEntity
import br.com.shubudo.database.entities.DefesaPessoalExtraBannerEntity
import br.com.shubudo.database.entities.FaixaEntity
import br.com.shubudo.database.entities.KataEntity
import br.com.shubudo.database.entities.MovimentoEntity
import br.com.shubudo.database.entities.ProjecaoEntity
import br.com.shubudo.database.entities.SequenciaDeCombateEntity
import br.com.shubudo.database.entities.UsuarioEntity

@Database(
    version = 3,
    entities = [DefesaPessoalEntity::class, FaixaEntity::class, KataEntity::class, MovimentoEntity::class, SequenciaDeCombateEntity::class, UsuarioEntity::class, ProjecaoEntity::class, DefesaPessoalExtraBannerEntity::class, AvisoEntity::class]
)
abstract class KarateDatabase : RoomDatabase() {

    abstract fun defesaPessoalDao(): DefesaPessoalDao
    abstract fun faixaDao(): FaixaDao
    abstract fun kataDao(): KataDao
    abstract fun movimentoDao(): MovimentoDao
    abstract fun sequenciaDeCombateDao(): SequenciaDeCombateDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun defesaPessoalExtraBannerDao(): DefesaPessoalExtraBannerDao
    abstract fun projecaoDao(): ProjecaoDao
    abstract fun avisoDao(): AvisoDao
}
