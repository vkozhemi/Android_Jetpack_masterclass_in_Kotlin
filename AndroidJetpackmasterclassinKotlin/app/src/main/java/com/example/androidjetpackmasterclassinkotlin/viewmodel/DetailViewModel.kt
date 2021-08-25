package com.example.androidjetpackmasterclassinkotlin.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidjetpackmasterclassinkotlin.model.DogDatabase
import com.example.androidjetpackmasterclassinkotlin.model.DogModel
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : BaseViewModel(application) {
    val dogLiveData = MutableLiveData<DogModel>()

    fun fetch(uuid: Int) {
        launch {
            val dog = DogDatabase(getApplication()).dogDao().getOne(uuid)
            dogLiveData.value = dog
        }
    }
}