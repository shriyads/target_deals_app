package com.target.targetcasestudy.core.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<T>)

    @Delete
    suspend fun delete(item: T)

}
