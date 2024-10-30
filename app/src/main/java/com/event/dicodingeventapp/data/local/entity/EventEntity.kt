package com.event.dicodingeventapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "eventDatabase")
data class EventEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "mediaCover")
    var mediaCover: String = "",

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "bookmarked")
    var isBookmarked: Boolean,

    @ColumnInfo(name = "finished")
    var isFinished: Boolean
)