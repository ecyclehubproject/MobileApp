package com.cyclehub.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cyclehub.model.ModelServices
import com.cyclehub.model.UserData

@Dao
interface ECycleHubDao {


    @Query("SELECT * FROM services")
    fun getAllServices(): LiveData<List<ModelServices>>

    @Query("SELECT * FROM services WHERE id = :id")
    fun getServices(id: Int): LiveData<ModelServices>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllServices(characters: List<ModelServices>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: ModelServices)

    @Delete
    fun deleteService(service: ModelServices)

    @Query("SELECT * FROM user_table")
    fun getAllUser(): LiveData<UserData>


    @Query("SELECT * FROM user_table WHERE id = :id")
    fun getUser(id: Int): LiveData<UserData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUser(characters: UserData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(character: UserData)

    @Delete
    fun deleteUser(service: UserData)
}
