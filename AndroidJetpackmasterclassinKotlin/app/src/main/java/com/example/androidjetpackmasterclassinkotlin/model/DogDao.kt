package com.example.androidjetpackmasterclassinkotlin.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DogDao {
    @Insert
    suspend fun insertAll(vararg dogs: DogModel) : List<Long>

    @Query("SELECT * FROM dogmodel") // name of the table DogModel in lowercase
    suspend fun getAll() : List<DogModel>

    @Query("SELECT * FROM dogmodel WHERE uuid = :dogId")
    suspend fun getOne(dogId: Int) : DogModel

    @Query("DELETE FROM dogmodel")
    suspend fun deleteAll()
}