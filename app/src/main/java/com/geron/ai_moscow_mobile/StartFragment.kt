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
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.widget.doOnTextChanged
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

class StartFragment : Fragment() {

    val startNextButton: Button by lazy {
        requireView().findViewById(R.id.btn_start_next)
    }

    val startMotionLayout: MotionLayout by lazy {
        requireView().findViewById(R.id.start_motion_layout)
    }

    val authorizationLoginEditText: EditText by lazy {
        requireView().findViewById(R.id.et_authorization_login)
    }

    val authorizationMotionLayout: MotionLayout by lazy {
        startMotionLayout.findViewById(R.id.fragment_authorization)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        startNextButton.setOnClickListener {
            startMotionLayout.transitionToEnd()
        }
        authorizationLoginEditText.apply {
            onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    showKeyboard()
                    authorizationMotionLayout.transitionToEnd()
                }
            }
            setOnEditorActionListener { textView, actionId, keyEvent ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    // TODO Search
                    true
                }
                false
            }
        }
        setKeyboardVisibilityListener()
    }

    private fun setKeyboardVisibilityListener() {
        KeyboardVisibilityEvent.setEventListener(activity as Activity) { isOpen ->
            if (!isOpen) {
                startMotionLayout.requestFocus()
            }
        }
    }

    private fun showKeyboard() {
        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}