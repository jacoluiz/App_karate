package br.com.shubudo.di.modules

import br.com.shubudo.network.services.FaixasServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RestApiModule {
    val baseUrl = "http://3.15.7.165:3000/"

    @Provides
    @Singleton
    fun providerRetroFit(client : OkHttpClient) : Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create()).client(client).build()
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
}