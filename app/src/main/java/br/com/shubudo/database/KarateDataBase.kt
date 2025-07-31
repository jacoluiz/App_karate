package br.com.shubudo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.shubudo.database.dao.AcademiaDao
import br.com.shubudo.database.dao.ArmamentoDao
import br.com.shubudo.database.dao.AvisoDao
import br.com.shubudo.database.dao.DefesaArmaDao
import br.com.shubudo.database.dao.DefesaPessoalDao
import br.com.shubudo.database.dao.DefesaPessoalExtraBannerDao
import br.com.shubudo.database.dao.EventoDao
import br.com.shubudo.database.dao.FaixaDao
import br.com.shubudo.database.dao.GaleriaEventoDao
import br.com.shubudo.database.dao.GaleriaFotoDao
import br.com.shubudo.database.dao.KataDao
import br.com.shubudo.database.dao.MovimentoDao
import br.com.shubudo.database.dao.ParceiroDao
import br.com.shubudo.database.dao.ProjecaoDao
import br.com.shubudo.database.dao.SequenciaDeCombateDao
import br.com.shubudo.database.dao.TecnicaChaoDao
import br.com.shubudo.database.dao.UsuarioDao
import br.com.shubudo.database.entities.AcademiaEntity
import br.com.shubudo.database.entities.ArmamentoEntity
import br.com.shubudo.database.entities.AvisoEntity
import br.com.shubudo.database.entities.DefesaArmaEntity
import br.com.shubudo.database.entities.DefesaPessoalEntity
import br.com.shubudo.database.entities.DefesaPessoalExtraBannerEntity
import br.com.shubudo.database.entities.EventoEntity
import br.com.shubudo.database.entities.FaixaEntity
import br.com.shubudo.database.entities.GaleriaEventoEntity
import br.com.shubudo.database.entities.GaleriaFotoEntity
import br.com.shubudo.database.entities.KataEntity
import br.com.shubudo.database.entities.MovimentoEntity
import br.com.shubudo.database.entities.ParceiroEntity
import br.com.shubudo.database.entities.ProjecaoEntity
import br.com.shubudo.database.entities.SequenciaDeCombateEntity
import br.com.shubudo.database.entities.TecnicaChaoEntity
import br.com.shubudo.database.entities.UsuarioEntity

@Database(
    version = 9,
    entities = [ParceiroEntity::class, GaleriaFotoEntity::class, GaleriaEventoEntity::class, AcademiaEntity::class, AvisoEntity::class, DefesaPessoalEntity::class, TecnicaChaoEntity::class, FaixaEntity::class, KataEntity::class, MovimentoEntity::class, SequenciaDeCombateEntity::class, UsuarioEntity::class, ProjecaoEntity::class, DefesaPessoalExtraBannerEntity::class, ArmamentoEntity::class, DefesaArmaEntity::class, EventoEntity::class]
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
    abstract fun armamentoDao(): ArmamentoDao
    abstract fun defesasDeArmasDao(): DefesaArmaDao
    abstract fun eventoDao(): EventoDao
    abstract fun tecnicaChaoDao(): TecnicaChaoDao
    abstract fun avisoDao(): AvisoDao
    abstract fun academiaDao(): AcademiaDao
    abstract fun galeriaEventoDao(): GaleriaEventoDao
    abstract fun galeriaFotoDao(): GaleriaFotoDao
    abstract fun parceiroDao(): ParceiroDao
}
