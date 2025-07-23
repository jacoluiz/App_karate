package br.com.shubudo.di.modules

import android.content.Context
import androidx.room.Room
import br.com.shubudo.database.KarateDatabase
import br.com.shubudo.database.dao.ArmamentoDao
import br.com.shubudo.database.dao.DefesaArmaDao
import br.com.shubudo.database.dao.DefesaPessoalDao
import br.com.shubudo.database.dao.DefesaPessoalExtraBannerDao
import br.com.shubudo.database.dao.EventoDao
import br.com.shubudo.database.dao.FaixaDao
import br.com.shubudo.database.dao.KataDao
import br.com.shubudo.database.dao.MovimentoDao
import br.com.shubudo.database.dao.ProjecaoDao
import br.com.shubudo.database.dao.SequenciaDeCombateDao
import br.com.shubudo.database.dao.TecnicaChaoDao
import br.com.shubudo.database.dao.UsuarioDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): KarateDatabase {
        return Room.databaseBuilder(
            context,
            KarateDatabase::class.java,
            "karate.db"
        )
            .fallbackToDestructiveMigration() // Destrói e recria o banco caso haja incompatibilidade
            .build()
    }

    @Provides
    fun provideDefesaPessoalDao(db: KarateDatabase): DefesaPessoalDao {
        return db.defesaPessoalDao()
    }

    @Provides
    fun provideFaixaDao(db: KarateDatabase): FaixaDao {
        return db.faixaDao()
    }

    @Provides
    fun provideKataDao(db: KarateDatabase): KataDao {
        return db.kataDao()
    }

    @Provides
    fun provideMoviemntoDao(db: KarateDatabase): MovimentoDao {
        return db.movimentoDao()
    }

    @Provides
    fun provideSequenciaDeCombateDao(db: KarateDatabase): SequenciaDeCombateDao {
        return db.sequenciaDeCombateDao()
    }

    @Provides
    fun provideUsuarioDao(db: KarateDatabase): UsuarioDao {
        return db.usuarioDao()
    }

    @Provides
    fun provideProjecaoDao(db: KarateDatabase): ProjecaoDao {
        return db.projecaoDao()
    }

    @Provides
    fun provideDefesaPessoalExtraBannerDao(db: KarateDatabase): DefesaPessoalExtraBannerDao {
        return db.defesaPessoalExtraBannerDao()
    }

    @Provides
    fun provideArmamentoDao(db: KarateDatabase): ArmamentoDao {
        return db.armamentoDao()
    }

    @Provides
    fun provideDefesasDeArmasDao(db: KarateDatabase): DefesaArmaDao {
        return db.defesasDeArmasDao()
    }

    @Provides
    fun provideDeEventoDao(db: KarateDatabase): EventoDao {
        return db.eventoDao()
    }

    @Provides
    fun provideTecnicaChaoDao(db: KarateDatabase): TecnicaChaoDao {
        return db.tecnicaChaoDao()
    }
}
