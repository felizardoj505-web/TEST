package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Database
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "test_results")
data class TestResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val testName: String,
    val score: Double,
    val date: Long = System.currentTimeMillis(),
    val timeUsedSeconds: Int,
    val questionCount: Int
)

@Dao
interface TestResultDao {
    @Query("SELECT * FROM test_results ORDER BY date DESC")
    fun getAllResults(): Flow<List<TestResult>>

    @Insert
    suspend fun insertResult(result: TestResult)
}

@Database(entities = [TestResult::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun testResultDao(): TestResultDao
}
