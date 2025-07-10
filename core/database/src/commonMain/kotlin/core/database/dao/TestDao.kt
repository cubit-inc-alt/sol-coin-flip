package core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import core.database.model.TestItemEntity

@Dao
interface TestDao {
    @Query("SELECT *  from TestItemEntity")
    fun getTests(): Flow<List<TestItemEntity>>

    @Query("DELETE FROM TestItemEntity WHERE id NOT in (:testIds)")
    suspend fun deleteExceptIds(testIds: List<String>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllSync(directoryEntities: List<TestItemEntity>)

    @Transaction
    suspend fun replaceAllSync(tests: List<TestItemEntity>) {
        deleteExceptIds(tests.map { it.id })
        saveAllSync(tests)
    }

    @Query("SELECT * FROM TestItemEntity WHERE id = :testId")
    fun getTestById(testId: String): Flow<TestItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(test: TestItemEntity): Long

    @Update
    suspend fun updateTestItem(testItem: TestItemEntity)


}
