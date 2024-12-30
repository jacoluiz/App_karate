package br.com.shubudo.di.modules

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.shubudo.database.KarateDatabase
import br.com.shubudo.database.dao.DefesaPessoalDao
import br.com.shubudo.database.dao.DefesaPessoalExtraBannerDao
import br.com.shubudo.database.dao.FaixaDao
import br.com.shubudo.database.dao.KataDao
import br.com.shubudo.database.dao.MovimentoDao
import br.com.shubudo.database.dao.ProjecaoDao
import br.com.shubudo.database.dao.SequenciaDeCombateDao
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

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Crie a tabela com o novo esquema
            database.execSQL(
                """
            CREATE TABLE IF NOT EXISTS Usuario (
                _id TEXT NOT NULL PRIMARY KEY,
                nome TEXT NOT NULL,
                username TEXT NOT NULL,
                email TEXT NOT NULL,
                senha TEXT NOT NULL,
                idade TEXT NOT NULL,
                peso TEXT NOT NULL,
                altura TEXT NOT NULL,
                corFaixa TEXT NOT NULL,
                perfil TEXT NOT NULL
            )
            """.trimIndent()
            )

            // Caso seja necessário, migre os dados da tabela antiga para a nova
            // Você pode usar SQL aqui para migrar registros da tabela antiga, se necessário.
        }
    }

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): KarateDatabase {
        return Room.databaseBuilder(
            context,
            KarateDatabase::class.java,
            "karate.db"
        ).addMigrations(MIGRATION_1_2).build()
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
}