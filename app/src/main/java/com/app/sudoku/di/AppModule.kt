package com.app.sudoku.di

import android.content.Context
import android.content.SharedPreferences
import com.app.sudoku.data.local.SudokuLocalDataSource
import com.app.sudoku.data.remote.api.SudokuApi
import com.app.sudoku.data.repository.SudokuRepositoryImpl
import com.app.sudoku.domain.repository.SudokuRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://api.api-ninjas.com/v1/"
private const val API_KEY = "wLVPN1zV08lJYF7uXqgyPw==zVwp6TlVcAO1NLUf"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            // Header con tu API KEY para todos los requests
            .addInterceptor { chain ->
                val newRequest = chain.request()
                    .newBuilder()
                    .addHeader("X-Api-Key", API_KEY)
                    .build()
                chain.proceed(newRequest)
            }
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        gson: Gson
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    @Singleton
    fun provideSudokuApi(retrofit: Retrofit): SudokuApi =
        retrofit.create(SudokuApi::class.java)

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences =
        context.getSharedPreferences("sudoku_prefs", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideLocalDataSource(
        prefs: SharedPreferences,
        gson: Gson
    ): SudokuLocalDataSource = SudokuLocalDataSource(prefs, gson)

    @Provides
    @Singleton
    fun provideSudokuRepository(
        api: SudokuApi,
        local: SudokuLocalDataSource
    ): SudokuRepository = SudokuRepositoryImpl(api, local)
}
