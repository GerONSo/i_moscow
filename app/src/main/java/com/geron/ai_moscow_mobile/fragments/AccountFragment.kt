package com.geron.ai_moscow_mobile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.geron.ai_moscow_mobile.AccountTypeRepository
import com.geron.ai_moscow_mobile.R
import com.geron.ai_moscow_mobile.ServerHelper
import com.geron.ai_moscow_mobile.adapters.AllAccountsAdapter
import com.geron.ai_moscow_mobile.viewmodels.AllProjectsViewModel
import com.geron.ai_moscow_mobile.viewmodels.MyProjectsViewModel
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class AccountFragment(
    val position: Int
) : Fragment() {

    val openChatButton: Button by lazy {
        requireView().findViewById(R.id.btn_join_event)
    }

    val nameTextView: TextView by lazy {
        requireView().findViewById(R.id.tv_name)
    }

    val logoImageView: CircleImageView by lazy {
        requireView().findViewById(R.id.iv_event_logo)
    }

    val allAccountsViewModel: AllProjectsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        load()
    }

    private fun load() {
        val account = allAccountsViewModel.getSlaveList().value?.get(position)
        openChatButton.text = "Пригласить в команду"
        nameTextView.text = account?.name
        Picasso.get().load(ServerHelper.BASE_URL + account?.photoLink)
            .into(logoImageView)
    }
}