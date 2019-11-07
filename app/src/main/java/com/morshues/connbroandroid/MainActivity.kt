package com.morshues.connbroandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.morshues.connbroandroid.repo.ConnbroRepository
import com.morshues.connbroandroid.ui.main.OnFragmentInteractionListener
import com.morshues.connbroandroid.ui.main.MainFragment
import com.morshues.connbroandroid.ui.main.FriendCreateFragment

enum class Page { MAIN, NEW_FRIEND }

class MainActivity : AppCompatActivity(), OnFragmentInteractionListener {

    private lateinit var mRepository: ConnbroRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mRepository = ConnbroRepository(application)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(R.id.container)) {
            is FriendCreateFragment -> onFragmentChange(Page.MAIN)
            else -> super.onBackPressed()
        }
    }

    // OnFragmentInteractionListener
    override fun onFragmentChange(page: Page) {
        val fragment = when (page) {
            Page.MAIN -> MainFragment.newInstance()
            Page.NEW_FRIEND -> FriendCreateFragment.newInstance()
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    // OnFragmentInteractionListener
    override fun getRepository(): ConnbroRepository {
        return mRepository
    }

}
