package com.morshues.connbroandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.morshues.connbroandroid.repo.ConnbroRepository
import com.morshues.connbroandroid.ui.main.OnFragmentInteractionListener
import com.morshues.connbroandroid.ui.main.MainFragment
import com.morshues.connbroandroid.ui.main.NewFriendFragment

enum class Page { MAIN, NEW_FRIEND }

class MainActivity : AppCompatActivity(), OnFragmentInteractionListener {

    private val mRepository = ConnbroRepository(application)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(mRepository))
                .commitNow()
        }
    }

    // OnFragmentInteractionListener
    override fun onFragmentChange(page: Page) {
        val fragment = when (page) {
            Page.MAIN -> MainFragment.newInstance(mRepository)
            Page.NEW_FRIEND -> NewFriendFragment.newInstance(mRepository)
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

}
