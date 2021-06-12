package com.geron.ai_moscow_mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val eventsFragment = EventsFragment()
        val startFragment = StartFragment()
        openFragment(startFragment)
        setCallbacks()
    }

    private fun setCallbacks() {
        CallbackHelper.onEventItemClicked = { position ->
            openFragment(EventFragment(position))
        }
        CallbackHelper.onMenuClicked = {
            openFragment(MenuFragment())
        }
        CallbackHelper.onMenuBackButtonClicked = {
            openFragment(EventsFragment())
        }
        CallbackHelper.onAllProjects= {
            openFragment(AllProjectsFragment())
        }
        CallbackHelper.onMyEvents= {
            openFragment(MyEventsFragment())
        }
        CallbackHelper.onMyProjects= {
            openFragment(MyProjectsFragment())
        }
        CallbackHelper.onProfileOpen= {
            openFragment(ProfileFragment())
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            add(R.id.fragment_container, fragment)
            addToBackStack(null)
        }
    }

}