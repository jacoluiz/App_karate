package br.com.shubudo.di.modules

import android.content.Context
import br.com.shubudo.database.KarateDatabase
import br.com.shubudo.database.dao.FaixaDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideFaixaDao(@ApplicationContext context: Context): FaixaDao {
        return KarateDatabase.getDataBase(context).faixaDao()
    }
}