package com.event.dicodingeventapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.event.dicodingeventapp.data.local.entity.EventEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM eventDatabase ORDER BY id DESC")
    fun getAllEvents(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM eventDatabase where bookmarked = 1")
    fun getBookmarkedEvents(): LiveData<List<EventEntity>>

    @Query("DELETE FROM eventDatabase WHERE bookmarked = 0")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEvent(event: List<EventEntity>)

    @Update
    fun updateEvent(event: EventEntity)

    @Query("SELECT * FROM eventDatabase where id = :id")
    fun getEventById(id: Int): LiveData<EventEntity>

    @Query("SELECT EXISTS(SELECT * FROM eventDatabase WHERE name = :name AND bookmarked = 1)")
    fun isEventBookmarked(name: String): Boolean

}