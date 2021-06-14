package com.geron.ai_moscow_mobile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import com.geron.ai_moscow_mobile.AccountTypeRepository
import com.geron.ai_moscow_mobile.CallbackHelper
import com.geron.ai_moscow_mobile.R
import com.geron.ai_moscow_mobile.ServerHelper
import com.geron.ai_moscow_mobile.viewmodels.AllProjectsViewModel
import com.geron.ai_moscow_mobile.viewmodels.MyProjectsViewModel
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ProjectFragment(
    val position: Int,
    val isMy: Boolean
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

    val allProjects: AllProjectsViewModel by activityViewModels()
    val myProjects: MyProjectsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(isMy) {
            loadMy()
        }
        else {
            load()
        }
    }

    private fun loadMy() {
        openChatButton.text = "Открыть чат"

        if(AccountTypeRepository.type == AccountTypeRepository.AccountType.MASTER) {
            val masterProject = myProjects.getMyMasterProjectList().value?.get(position)
            nameTextView.text = masterProject?.name
            Picasso.get().load(ServerHelper.BASE_URL + masterProject?.photoLink)
                .into(logoImageView)
            openChatButton.setOnClickListener {
                CallbackHelper.onOpenChat(masterProject?.chatId!!)
            }

        }
        else {
            val slaveProject = myProjects.getMySlaveProjectList().value?.get(position)
            nameTextView.text = slaveProject?.name
            Picasso.get().load(ServerHelper.BASE_URL + slaveProject?.photoLink)
                .into(logoImageView)
            openChatButton.setOnClickListener {
                CallbackHelper.onOpenChat(slaveProject?.chatId!!)
            }
        }

    }

    private fun load() {
        val project = allProjects.getProjectList().value?.get(position)
        openChatButton.text = "Подать заявку"
        nameTextView.text = project?.name
    }
}