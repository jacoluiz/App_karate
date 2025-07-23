package br.com.shubudo.di.modules

import br.com.shubudo.network.services.*
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

    private const val BASE_URL = "https://api.calendariokarate.click/"

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    // Serviços Retrofit

    @Provides
    @Singleton
    fun provideFaixasService(retrofit: Retrofit): FaixasServices =
        retrofit.create(FaixasServices::class.java)

    @Provides
    @Singleton
    fun provideDefesaPessoalService(retrofit: Retrofit): DefesaPessoalServices =
        retrofit.create(DefesaPessoalServices::class.java)

    @Provides
    @Singleton
    fun provideKataService(retrofit: Retrofit): KataServices =
        retrofit.create(KataServices::class.java)

    @Provides
    @Singleton
    fun provideMovimentoService(retrofit: Retrofit): MovimentoService =
        retrofit.create(MovimentoService::class.java)

    @Provides
    @Singleton
    fun provideSequenciaDeCombateService(retrofit: Retrofit): SequenciaDeCombateService =
        retrofit.create(SequenciaDeCombateService::class.java)

    @Provides
    @Singleton
    fun provideUsuarioService(retrofit: Retrofit): UsuarioService =
        retrofit.create(UsuarioService::class.java)

    @Provides
    @Singleton
    fun provideProjecaoService(retrofit: Retrofit): ProjecaoServices =
        retrofit.create(ProjecaoServices::class.java)

    @Provides
    @Singleton
    fun provideDefesaPessoalExtraBannerService(retrofit: Retrofit): DefesaPessoalExtraBannerServices =
        retrofit.create(DefesaPessoalExtraBannerServices::class.java)

    @Provides
    @Singleton
    fun provideArmamentoService(retrofit: Retrofit): ArmamentoServices =
        retrofit.create(ArmamentoServices::class.java)

    @Provides
    @Singleton
    fun provideDefesaArmaService(retrofit: Retrofit): DefesaArmaServices =
        retrofit.create(DefesaArmaServices::class.java)

    @Provides
    @Singleton
    fun provideEventoService(retrofit: Retrofit): EventoService =
        retrofit.create(EventoService::class.java)

    @Provides
    @Singleton
    fun provideTecnicaChaoService(retrofit: Retrofit): TecnicaChaoService =
        retrofit.create(TecnicaChaoService::class.java)
}
