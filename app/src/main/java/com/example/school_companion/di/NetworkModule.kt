package com.example.school_companion.di

import com.example.school_companion.config.AuthInterceptor
import com.example.school_companion.data.api.AuthApi
import com.example.school_companion.data.api.ChatApi
import com.example.school_companion.data.api.ChildrenApi
import com.example.school_companion.data.api.CompanionApi
import com.example.school_companion.data.api.EntryApi
import com.example.school_companion.data.api.EventApi
import com.example.school_companion.data.api.GoalApi
import com.example.school_companion.data.api.NoteApi
import com.example.school_companion.data.api.ParamApi
import com.example.school_companion.data.api.SessionApi
import com.example.school_companion.data.api.SpecialNeedApi
import com.example.school_companion.data.api.TaskApi
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
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
//            .baseUrl("http://10.0.2.2:8080/")
//            .baseUrl("http://192.168.178.21:8080/")
            .baseUrl("https://wtypmah.duckdns.org/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideChildApi(retrofit: Retrofit): ChildrenApi {
        return retrofit.create(ChildrenApi::class.java)
    }

    @Provides
    @Singleton
    fun provideEventApi(retrofit: Retrofit): EventApi {
        return retrofit.create(EventApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGoalApi(retrofit: Retrofit): GoalApi {
        return retrofit.create(GoalApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNeedApi(retrofit: Retrofit): SpecialNeedApi {
        return retrofit.create(SpecialNeedApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNoteApi(retrofit: Retrofit): NoteApi {
        return retrofit.create(NoteApi::class.java)
    }

    @Provides
    @Singleton
    fun provideEntryApi(retrofit: Retrofit): EntryApi {
        return retrofit.create(EntryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideParamApi(retrofit: Retrofit): ParamApi {
        return retrofit.create(ParamApi::class.java)
    }

    @Provides
    @Singleton
    fun provideChatApi(retrofit: Retrofit): ChatApi {
        return retrofit.create(ChatApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSessionApi(retrofit: Retrofit): SessionApi {
        return retrofit.create(SessionApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCompanionApi(retrofit: Retrofit): CompanionApi {
        return retrofit.create(CompanionApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTaskApi(retrofit: Retrofit): TaskApi {
        return retrofit.create(TaskApi::class.java)
    }
} 