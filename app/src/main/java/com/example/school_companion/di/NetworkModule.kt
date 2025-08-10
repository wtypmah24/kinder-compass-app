package com.example.school_companion.di

import com.example.school_companion.data.api.ApiService
import com.example.school_companion.data.api.AuthApi
import com.example.school_companion.data.api.ChildrenApi
import com.example.school_companion.data.api.EntryApi
import com.example.school_companion.data.api.EventApi
import com.example.school_companion.data.api.GoalApi
import com.example.school_companion.data.api.NoteApi
import com.example.school_companion.data.api.ParamApi
import com.example.school_companion.data.api.SpecialNeedApi
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
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
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
} 