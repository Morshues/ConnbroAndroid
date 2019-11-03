package com.morshues.connbroandroid

import android.app.Application
import com.morshues.connbroandroid.repo.ConnbroRepository

class App : Application() {

    lateinit var repository: ConnbroRepository
        private set

    override fun onCreate() {
        super.onCreate()

        repository = ConnbroRepository(this)
    }

}