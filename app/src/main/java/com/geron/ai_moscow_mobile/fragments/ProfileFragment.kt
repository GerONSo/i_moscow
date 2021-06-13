package com.geron.ai_moscow_mobile.fragments

import android.os.Bundle
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.geron.ai_moscow_mobile.R

class ProfileFragment : Fragment() {
    lateinit var btn_edit:Button
    lateinit var btn_no_edit:Button
    lateinit var edit_profile:View
    lateinit var no_edit_profile_la:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_edit = view.findViewById(R.id.edit_profile)
        btn_no_edit = view.findViewById(R.id.no_edit_profile)

        edit_profile = view.findViewById(R.id.edit_profile_la)
        no_edit_profile_la = view.findViewById(R.id.no_edit_profile_la)

        btn_edit.setOnClickListener {

            no_edit_profile_la.visibility = View.INVISIBLE
            edit_profile.visibility = View.VISIBLE


        }
        btn_no_edit.setOnClickListener {
            edit_profile.visibility = View.INVISIBLE
            no_edit_profile_la.visibility =  View.VISIBLE
        }
    }
}