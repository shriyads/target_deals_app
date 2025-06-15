package com.target.targetcasestudy.core.di

import android.content.Context
import androidx.room.Room
import com.target.targetcasestudy.core.database.AppDatabase
import com.target.targetcasestudy.feature_deals.data.local.dao.DealsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(appContext, AppDatabase::class.java, "deals_db")
            .fallbackToDestructiveMigrationOnDowngrade(true)
            .build()

    @Provides
    fun provideDealDao(database: AppDatabase): DealsDao = database.dealDao()
}
