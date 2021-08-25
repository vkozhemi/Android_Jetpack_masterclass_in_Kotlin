package com.example.androidjetpackmasterclassinkotlin.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.androidjetpackmasterclassinkotlin.model.DogDao
import com.example.androidjetpackmasterclassinkotlin.model.DogDatabase
import com.example.androidjetpackmasterclassinkotlin.model.DogsApiService
import com.example.androidjetpackmasterclassinkotlin.model.DogModel
import com.example.androidjetpackmasterclassinkotlin.util.NotificationsHelper
import com.example.androidjetpackmasterclassinkotlin.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.lang.NumberFormatException

class ListViewModel(application: Application) : BaseViewModel(application) {
    private var prefHelper = SharedPreferencesHelper(getApplication())
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L

    private val service = DogsApiService()
    private val disposable = CompositeDisposable()

    val dogs = MutableLiveData<List<DogModel>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        checkCacheDuration()
        var updateTime = prefHelper.getUpdateTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            fetchFromDatabase()
        } else {
            fetchFromRemote()
        }
    }

    private fun checkCacheDuration() {
        val cachePreference = prefHelper.getCacheDuration()
        try {
            val cachePreferenceInt = cachePreference?.toInt() ?: 5 * 60
            refreshTime = cachePreferenceInt.times(1000 * 1000 * 1000L)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    private fun fetchFromDatabase() {
        loading.value = true
        launch {
            val dogs = DogDatabase(getApplication()).dogDao().getAll()
            dogsRetrieved(dogs)
            Toast.makeText(getApplication(), "Retrieved from database", Toast.LENGTH_LONG).show()
        }
    }

    fun refreshBypassCache() {
        fetchFromRemote()
    }

    private fun fetchFromRemote() {
        loading.value = true
        disposable.add(
            service.getAll()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DogModel>>() {

                    override fun onSuccess(list: List<DogModel>) {
                        storeDogsLocally(list)
                        Toast.makeText(getApplication(), "Retrieved from network", Toast.LENGTH_LONG).show()
                        NotificationsHelper(getApplication()).createNotification()
                    }

                    override fun onError(e: Throwable) {
                        dogsLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                        Log.d("LOG_TAG", "onError::Throwable: $e")
                    }
                })
        )
    }

    private fun dogsRetrieved(dogList: List<DogModel>) {
        dogs.value = dogList
        dogsLoadError.value = false
        loading.value = false
    }

    private fun storeDogsLocally(list: List<DogModel>) {
        launch {
            val dao : DogDao = DogDatabase(getApplication()).dogDao()
            dao.deleteAll()
            val result: List<Long> = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].uuid = result[i].toInt()
                ++i
            }
            dogsRetrieved(list)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}