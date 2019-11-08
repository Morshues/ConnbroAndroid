package com.morshues.connbroandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.morshues.connbroandroid.repo.ConnbroRepository
import com.morshues.connbroandroid.ui.main.OnFragmentInteractionListener
import com.morshues.connbroandroid.ui.main.MainFragment
import com.morshues.connbroandroid.ui.main.FriendCreateFragment
import com.morshues.connbroandroid.ui.main.FriendDetailFragment

sealed class Page {
    object MainPage: Page()
    object FriendCreatePage: Page()
    data class FriendDetailPage(val id: Long): Page()
}

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
            is FriendCreateFragment, is FriendDetailFragment -> onFragmentChange(Page.MainPage)
            else -> super.onBackPressed()
        }
    }

    // OnFragmentInteractionListener
    override fun onFragmentChange(page: Page) {
        val fragment = when (page) {
            is Page.MainPage -> MainFragment.newInstance()
            is Page.FriendCreatePage -> FriendCreateFragment.newInstance()
            is Page.FriendDetailPage -> FriendDetailFragment.newInstance(page.id)
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
