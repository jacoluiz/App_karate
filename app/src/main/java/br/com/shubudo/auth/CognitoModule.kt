package br.com.shubudo.auth

import android.content.Context
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CognitoModule {

    @Provides
    @Singleton
    fun provideCognitoAuthManager(
        @ApplicationContext context: Context
    ): CognitoAuthManager {
        return CognitoAuthManager(context)
    }

    @Provides
    @Singleton
    fun provideCognitoUserPool(
        authManager: CognitoAuthManager
    ): CognitoUserPool {
        return authManager.userPool
    }
}

