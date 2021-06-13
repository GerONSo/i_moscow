package com.geron.ai_moscow_mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.geron.ai_moscow_mobile.fragments.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val eventsFragment = EventsFragment()
        val startFragment = StartFragment()
        openFragment(startFragment)
        setCallbacks()
    }

    //tewse
    private fun setCallbacks() {
        CallbackHelper.onLogin = {
            replaceFragment(EventsFragment())
        }
        CallbackHelper.onEventItemClicked = { position ->
            openFragment(EventFragment(position))
        }
        CallbackHelper.onMenuClicked = {
            openFragment(MenuFragment())
        }
        CallbackHelper.onMenuBackButtonClicked = {
            supportFragmentManager.popBackStack()
        }
        CallbackHelper.onAllProjects = {
            openFragment(AllProjectsFragment())
        }
        CallbackHelper.onMyEvents = {
            openFragment(MyEventsFragment())
        }
        CallbackHelper.onMyProjects = {
            openFragment(MyProjectsFragment())
        }
        CallbackHelper.onProfileOpen = {
            openFragment(ProfileFragment())
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            add(R.id.fragment_container, fragment, fragment::class.simpleName)
            setReorderingAllowed(true)
            addToBackStack(fragment::class.simpleName)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.fragment_container, fragment, fragment::class.simpleName)
            setReorderingAllowed(true)
            addToBackStack(fragment::class.simpleName)
        }
    }

    override fun onBackPressed() {
        val index = supportFragmentManager.backStackEntryCount - 1
        val backEntry = supportFragmentManager.getBackStackEntryAt(index)
        val tag = backEntry.name
        val fragment = supportFragmentManager.findFragmentByTag(tag)
        if (fragment is StartFragment) {
            CallbackHelper.onBackPressedStartFragment()
            return
        }
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}