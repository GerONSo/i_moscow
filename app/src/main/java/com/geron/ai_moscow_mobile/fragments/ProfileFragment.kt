package com.geron.ai_moscow_mobile.fragments

import android.os.Bundle
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.cunoraz.tagview.Tag
import com.cunoraz.tagview.TagView
import com.geron.ai_moscow_mobile.AccountTypeRepository
import com.geron.ai_moscow_mobile.CookieRepository
import com.geron.ai_moscow_mobile.R
import com.geron.ai_moscow_mobile.ServerHelper
import com.geron.ai_moscow_mobile.data_classes.Account
import com.geron.ai_moscow_mobile.viewmodels.ProfileViewModel
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.runBlocking

class ProfileFragment : Fragment() {
    lateinit var btn_edit: TextView
    lateinit var btn_no_edit: TextView
    lateinit var edit_profile: View
    lateinit var no_edit_profile_la: View

    lateinit var nameTextView: TextView
    lateinit var mainSkillTextView: TextView
    lateinit var phoneTextView: TextView
    lateinit var aboutTextView: TextView
    lateinit var educationPlaceTextView: TextView
    lateinit var educationSpecializationTextView: TextView
    lateinit var educationDegreeTextView: TextView
    lateinit var educationDateTextView: TextView

    lateinit var nameEditText: EditText
    lateinit var mainSkillEditText: EditText
    lateinit var phoneEditText: EditText
    lateinit var aboutEditText: EditText
    lateinit var educationPlaceEditText: EditText
    lateinit var educationSpecializationEditText: EditText
    lateinit var educationDegreeEditText: EditText
    lateinit var educationDateEditText: EditText
    lateinit var profileNoEditImageView: CircleImageView
    lateinit var profileEditImageView: CircleImageView

    val myProfileViewModel: ProfileViewModel by viewModels()

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
        btn_edit = view.findViewById(R.id.edit_profile)
        btn_no_edit = view.findViewById(R.id.no_edit_profile)
        edit_profile = view.findViewById(R.id.edit_profile_la)
        no_edit_profile_la = view.findViewById(R.id.no_edit_profile_la)

        nameTextView = no_edit_profile_la.findViewById(R.id.txt_name)
        mainSkillTextView = no_edit_profile_la.findViewById(R.id.main_skill)
        phoneTextView = no_edit_profile_la.findViewById(R.id.phone_number)
        aboutTextView = no_edit_profile_la.findViewById(R.id.txt_about)
        educationDateTextView = no_edit_profile_la.findViewById(R.id.set_educ_date)
        educationDegreeTextView = no_edit_profile_la.findViewById(R.id.set_educ_degree)
        educationPlaceTextView = no_edit_profile_la.findViewById(R.id.set_educ_place)
        educationSpecializationTextView = no_edit_profile_la.findViewById(R.id.set_educ_spec)
        profileNoEditImageView = no_edit_profile_la.findViewById(R.id.prof_picture)

        nameEditText= edit_profile.findViewById(R.id.txt_name)
        mainSkillEditText = edit_profile.findViewById(R.id.main_skill)
        phoneEditText = edit_profile.findViewById(R.id.phone_number)
        aboutEditText = edit_profile.findViewById(R.id.txt_about)
        educationDateEditText = edit_profile.findViewById(R.id.set_educ_date)
        educationDegreeEditText = edit_profile.findViewById(R.id.set_educ_degree)
        educationPlaceEditText = edit_profile.findViewById(R.id.set_educ_place)
        educationSpecializationEditText = edit_profile.findViewById(R.id.set_educ_spec)
        profileEditImageView = edit_profile.findViewById(R.id.prof_picture)

        runBlocking { myProfileViewModel.getMyAccount(CookieRepository.cookie!!) }
        myProfileViewModel.getMyAccountProfile().observe(viewLifecycleOwner, { myAccount ->
            nameTextView.text = myAccount.name
            nameEditText.setText(myAccount.name)
            Picasso.get().load(ServerHelper.BASE_URL + myAccount.photoLink)
                .into(profileEditImageView)
            Picasso.get().load(ServerHelper.BASE_URL + myAccount.photoLink)
                .into(profileNoEditImageView)
        })
        btn_no_edit.setOnClickListener {
            no_edit_profile_la.visibility = View.GONE
            edit_profile.visibility = View.VISIBLE
        }
        btn_edit.setOnClickListener {
            edit_profile.visibility = View.GONE
            no_edit_profile_la.visibility = View.VISIBLE
            var account = myProfileViewModel.getMyAccountProfile().value
            if(account != null && CookieRepository.cookie != null) {
                account.name = nameEditText.text.toString()
                runBlocking {
                    myProfileViewModel.updateAccount(CookieRepository.cookie!!, account)
                }
            }
        }
        when(AccountTypeRepository.type) {
            AccountTypeRepository.AccountType.MASTER -> {
                toggle.check(R.id.rb_master)
            }
            AccountTypeRepository.AccountType.SLAVE -> {
                toggle.check(R.id.rb_slave)
            }
        }
        val tagView = view.findViewById<TagView>(R.id.cloud_skils)
        tagView.apply {
            addTag(Tag("Kotlin"))
            addTag(Tag("Python"))
            addTag(Tag("Figma"))
        }
    }
}