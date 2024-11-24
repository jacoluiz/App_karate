package br.com.shubudo.di.modules

import br.com.shubudo.network.services.DefesaPessoalServices
import br.com.shubudo.network.services.FaixasServices
import br.com.shubudo.network.services.KataServices
import br.com.shubudo.network.services.MovimentoService
import br.com.shubudo.network.services.SequenciaDeCombateService
import br.com.shubudo.network.services.UsuarioService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RestApiModule {
    val baseUrl = "http://3.15.7.165:3000/"

    @Provides
    @Singleton
    fun providerRetroFit(client : OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor() : HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }

    @Provides
    @Singleton
    fun providerOkHttpClient(loggingInterceptor : HttpLoggingInterceptor) : OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    }

    @Provides
    @Singleton
    fun provideFaixasService(retrofit : Retrofit) : FaixasServices {
        return retrofit.create(FaixasServices::class.java)
    }

    @Provides
    @Singleton
    fun provideDefesaPessoalService(retrofit : Retrofit) : DefesaPessoalServices {
        return retrofit.create(DefesaPessoalServices::class.java)
    }

    @Provides
    @Singleton
    fun providerKataService(retrofit : Retrofit) : KataServices {
        return retrofit.create(KataServices::class.java)
    }

    @Provides
    @Singleton
    fun providerMovimentoService(retrofit : Retrofit) : MovimentoService {
        return retrofit.create(MovimentoService::class.java)
    }

    @Provides
    @Singleton
    fun providerSequenciaDeCombateService(retrofit : Retrofit) : SequenciaDeCombateService {
        return retrofit.create(SequenciaDeCombateService::class.java)
    }

    @Provides
    @Singleton
    fun providerUsuarioService(retrofit : Retrofit) : UsuarioService {
        return retrofit.create(UsuarioService::class.java)
    }
}