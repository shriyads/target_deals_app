package com.target.targetcasestudy.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.target.targetcasestudy.core.database.DatabaseConstants.DB_VERSION
import com.target.targetcasestudy.feature_deals.data.local.dao.DealsDao
import com.target.targetcasestudy.feature_deals.data.local.entity.DealsEntity

@Database(entities = [DealsEntity::class], version = DB_VERSION, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dealDao(): DealsDao
}
