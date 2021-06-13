package com.geron.ai_moscow_mobile.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.geron.ai_moscow_mobile.AccountTypeRepository
import com.geron.ai_moscow_mobile.R

class ProfileFragment : Fragment() {

    val toggle: RadioGroup by lazy {
        requireView().findViewById(R.id.toggle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        when(AccountTypeRepository.type) {
            AccountTypeRepository.AccountType.MASTER -> {
                toggle.check(R.id.rb_master)
            }
            AccountTypeRepository.AccountType.SLAVE -> {
                toggle.check(R.id.rb_slave)
            }
        }
    }
}