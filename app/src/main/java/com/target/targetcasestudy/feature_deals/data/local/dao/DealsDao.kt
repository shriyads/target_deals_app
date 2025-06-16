package com.target.targetcasestudy.feature_deals.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.target.targetcasestudy.core.database.BaseDao
import com.target.targetcasestudy.feature_deals.data.local.entity.DealsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DealsDao: BaseDao<DealsEntity> {
    @Query("SELECT * FROM deals")
    fun getAllDeals(): Flow<List<DealsEntity>>

    @Query("SELECT * FROM deals")
    fun getDealsList(): List<DealsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllDeals(deals: List<DealsEntity>)

    @Query("DELETE  FROM deals")
    suspend fun deleteAllDeals()

    @Query("SELECT * FROM deals WHERE id = :dealId")
    fun getDealById(dealId: String): Flow<DealsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeal(deals: DealsEntity)

    @Query("SELECT * FROM deals WHERE title LIKE '%' || :query || '%'")
    fun searchDeals(query: String): Flow<List<DealsEntity>>

    @Transaction
    suspend fun refreshDeals(deals: List<DealsEntity>) {
        deleteAllDeals()
        insertAllDeals(deals)
    }


}