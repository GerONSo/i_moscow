package com.geron.ai_moscow_mobile

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geron.ai_moscow_mobile.data_classes.Event
import com.geron.ai_moscow_mobile.viewmodels.EventsViewModel
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

class EventsFragment : Fragment() {

    lateinit var motionLayout: MotionLayout
    lateinit var buttonMenu:Button
    val model: EventsViewModel by activityViewModels()
    val eventsAdapter: EventsAdapter by lazy {
        EventsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val searchEditText = view.findViewById<EditText>(R.id.et_search)
        val searchLeftImageView = view.findViewById<ImageView>(R.id.iv_left_search)
        val searchRightImageView = view.findViewById<ImageView>(R.id.iv_right_search)
        val searchCardView = view.findViewById<CardView>(R.id.ifv_search)
        val eventsRecyclerView = view.findViewById<RecyclerView>(R.id.rv_events)
        motionLayout = view.findViewById(R.id.events_motion_layout)
        buttonMenu = view.findViewById(R.id.btn_menu_open)
        buttonMenu.setOnClickListener {
            CallbackHelper.onMenuClicked()
        }
        model.getEventList().observe(viewLifecycleOwner, { eventList ->
            eventsAdapter.updateList(eventList)
        })
        searchCardView.setOnClickListener {
            searchEditText.requestFocus()
        }
        searchEditText.apply {
            onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    showKeyboard()
                    searchEditText.hint = "Поиск"
                    if(this.text.isNotEmpty()) {
                        searchRightImageView.setImageResource(R.drawable.ic_clear)
                    }
                    else {
                        searchRightImageView.setImageDrawable(null)
                    }
                    motionLayout.transitionToEnd()
                }
            }
            doOnTextChanged { text, start, before, count ->
                if (text?.isNotEmpty() == true) {
                    searchRightImageView.setImageResource(R.drawable.ic_clear)
                } else {
                    searchRightImageView.setImageDrawable(null)
                }
            }
            setOnEditorActionListener { textView, actionId, keyEvent ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // TODO Search
                    true
                }
                false
            }
        }
        searchRightImageView.setOnClickListener {
            searchEditText.setText("")
        }
        setKeyboardVisibilityListener()
        eventsRecyclerView.apply {
            adapter = eventsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setKeyboardVisibilityListener() {
        KeyboardVisibilityEvent.setEventListener(activity as Activity) { isOpen ->
            if (!isOpen) {
                motionLayout.requestFocus()
            }
        }
    }

    fun showKeyboard() {
        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}