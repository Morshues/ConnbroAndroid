package com.morshues.connbroandroid

import android.app.Application
import com.morshues.connbroandroid.repo.ConnbroRepository

class App : Application() {

    lateinit var mRepository: ConnbroRepository
        private set

    override fun onCreate() {
        super.onCreate()
        mRepository = ConnbroRepository(this)
    }

}