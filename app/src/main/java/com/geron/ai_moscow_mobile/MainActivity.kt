package com.geron.ai_moscow_mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
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
        CallbackHelper.apply {
            onAccountClicked = { position ->
                openFragment(AccountFragment(position))
            }
            onProjectClicked = { position, isMy ->
                openFragment(ProjectFragment(position, isMy))
            }
            onLogin = {
                replaceFragment(EventsFragment())
            }
            onEventItemClicked = { position ->
                openFragment(EventFragment(position))
            }
            onMenuClicked = {
                openFragment(MenuFragment())
            }
            onMenuBackButtonClicked = {
                supportFragmentManager.popBackStack()
            }
            onAllProjects = {
                openFragment(AllProjectsFragment())
            }
            onMyEvents = {
                openFragment(MyEventsFragment())
            }
            onMyProjects = {
                openFragment(MyProjectsFragment())
            }
            onProfileOpen = {
                openFragment(ProfileFragment())
            }
            onOpenChat = { chatId ->
                openFragment(ChatFragment(chatId))
            }
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

    fun onRadioButtonClicked(view: View) {
        if(view is RadioButton) {
            val checked = view.isChecked
            if(!checked) return
            when(view.id) {
                R.id.rb_master -> {
                    AccountTypeRepository.type = AccountTypeRepository.AccountType.MASTER
                }
                R.id.rb_slave -> {
                    AccountTypeRepository.type = AccountTypeRepository.AccountType.SLAVE
                }
            }
        }
    }
}