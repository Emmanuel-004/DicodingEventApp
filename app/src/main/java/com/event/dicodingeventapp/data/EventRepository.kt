package com.event.dicodingeventapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.event.dicodingeventapp.data.local.entity.EventEntity
import com.event.dicodingeventapp.data.local.room.EventDao
import com.event.dicodingeventapp.data.response.DetailEventResponse
import com.event.dicodingeventapp.data.response.Event
import com.event.dicodingeventapp.data.response.EventResponse
import com.event.dicodingeventapp.data.retrofit.ApiService
import com.event.dicodingeventapp.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao,
    private val appExecutors: AppExecutors
) {
    private val upcomingResult = MediatorLiveData<Result<List<EventEntity>>>()
    private val finishedResult = MediatorLiveData<Result<List<EventEntity>>>()
    private val detailResult = MediatorLiveData<Result<DetailEventResponse>>()

    fun getUpcomingEvents(): LiveData<Result<List<EventEntity>>> {
        upcomingResult.value = Result.Loading
        Log.d("EventRepository", "getUpcomingEvents: Loading state applied")

        val client = apiService.getEvents(1, "")
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val events = response.body()?.listEvents
                    val eventList = ArrayList<EventEntity>()
                    appExecutors.diskIO.execute {
                        events?.forEach { event ->
                            val isBookmarked = eventDao.isEventBookmarked(event.id.toString())
                            val eventEntity = EventEntity(
                                event.id,
                                event.mediaCover,
                                event.name,
                                isBookmarked,
                                isFinished = false
                            )
                            eventList.add(eventEntity)
                        }
                        eventDao.insertEvent(eventList)
                        Log.d("EventRepository", "getUpcomingEvents: Events successfully inserted")

                        appExecutors.mainThread.execute {
                            val localData = eventDao.getAllEvents()
                            upcomingResult.addSource(localData) { newData: List<EventEntity> ->
                                upcomingResult.value = Result.Success(newData)
                                Log.d("EventRepository", "getUpcomingEvents: Success state applied with ${newData.size} items")
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                upcomingResult.value = Result.Error(t.message.toString())
                Log.e("EventRepository", "getUpcomingEvents: Error occurred - ${t.message}")
            }
        })

        return upcomingResult
    }

    fun getFinishedEvents(): LiveData<Result<List<EventEntity>>> {
        finishedResult.value = Result.Loading
        Log.d("EventRepository", "getFinishedEvents: Loading state applied")

        val client = apiService.getEvents(0, "")
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val events = response.body()?.listEvents
                    val eventList = ArrayList<EventEntity>()
                    appExecutors.diskIO.execute {
                        events?.forEach { event ->
                            val isBookmarked = eventDao.isEventBookmarked(event.id.toString())
                            val eventEntity = EventEntity(
                                event.id,
                                event.mediaCover,
                                event.name,
                                isBookmarked,
                                isFinished = true
                            )
                            eventList.add(eventEntity)
                        }
                        eventDao.insertEvent(eventList)
                        Log.d("EventRepository", "getFinishedEvents: Events successfully inserted")

                        appExecutors.mainThread.execute {
                            val localData = eventDao.getAllEvents()
                            finishedResult.addSource(localData) { newData: List<EventEntity> ->
                                finishedResult.value = Result.Success(newData)
                                Log.d("EventRepository", "getFinishedEvents: Success state applied with ${newData.size} items")
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                finishedResult.value = Result.Error(t.message.toString())
                Log.e("EventRepository", "getFinishedEvents: Error occurred - ${t.message}")
            }
        })

        return finishedResult
    }

    fun getDetailEvents(eventId: String): LiveData<Result<DetailEventResponse>> {
        detailResult.value = Result.Loading
        Log.d("EventRepository", "getDetailEvents: Loading state applied")

        val client = apiService.getDetailEvents(eventId)
        client.enqueue(object : Callback<DetailEventResponse> {
            override fun onResponse(call: Call<DetailEventResponse>, response: Response<DetailEventResponse>) {
                if (response.isSuccessful) {
                    val event = response.body()
                    detailResult.value = Result.Success(event ?: DetailEventResponse(false, "", Event()))
                    Log.d("EventRepository", "getDetailEvents: Success state applied")
                }
            }

            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                detailResult.value = Result.Error(t.message.toString())
                Log.e("EventRepository", "getDetailEvents: Error occurred - ${t.message}")
            }
        })

        return detailResult
    }

    fun getBookmarkedEvents(): LiveData<List<EventEntity>> {
        return eventDao.getBookmarkedEvents()
    }

    fun setEventBookmark(event: EventEntity, bookmarkState: Boolean) {
        appExecutors.diskIO.execute {
            event.isBookmarked = bookmarkState
            eventDao.updateEvent(event)
            Log.d("EventRepository", "setEventBookmark: Bookmark state updated to $bookmarkState for event ${event.id}")
        }
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao,
            appExecutors: AppExecutors
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao, appExecutors)
            }.also { instance = it }
    }
}
