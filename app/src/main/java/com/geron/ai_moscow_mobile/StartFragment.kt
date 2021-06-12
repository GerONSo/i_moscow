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
import androidx.fragment.app.viewModels
import com.geron.ai_moscow_mobile.data_classes.User
import com.geron.ai_moscow_mobile.viewmodels.AuthorizeViewModel
import kotlinx.coroutines.runBlocking
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

class StartFragment : Fragment() {

    val authorizeViewModel: AuthorizeViewModel by viewModels()

    val startNextButton: Button by lazy {
        requireView().findViewById(R.id.btn_start_next)
    }

    val startMotionLayout: MotionLayout by lazy {
        requireView().findViewById(R.id.start_motion_layout)
    }

    val authorizationLoginEditText: EditText by lazy {
        startMotionLayout.findViewById(R.id.et_authorization_login)
    }

    val authorizationPasswordEditText: EditText by lazy {
        startMotionLayout.findViewById(R.id.et_password)
    }

    val buttonLogin: Button by lazy {
        requireView().findViewById(R.id.btn_login)
    }

    var currentState = 0

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
            currentState = 1
        }
        buttonLogin.setOnClickListener {
            runBlocking {
                authorizeViewModel.login(
                    User(
                        authorizationLoginEditText.text.toString(),
                        authorizationPasswordEditText.text.toString()
                    )
                )
            }
        }
        CallbackHelper.onBackPressedStartFragment = {
            if (currentState == 1) {
                startMotionLayout.setTransition(R.id.authorization_transition)
                startMotionLayout.transitionToStart()
                currentState = 0
            }
        }
        authorizationLoginEditText.apply {
            onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    startMotionLayout.setTransition(R.id.authorization_keyboard_transition)
                    startMotionLayout.transitionToEnd()
                } else {
                    startMotionLayout.setTransition(R.id.authorization_keyboard_transition)
                    startMotionLayout.transitionToStart()
                }
            }
//            setOnEditorActionListener { textView, actionId, keyEvent ->
//            }
        }
        setKeyboardVisibilityListener()
    }

    private fun setKeyboardVisibilityListener() {
        KeyboardVisibilityEvent.setEventListener(activity as Activity) { isOpen ->
            if (!isOpen) {
                startMotionLayout.requestFocus()
                authorizationLoginEditText.clearFocus()
            }
        }
    }

    private fun showKeyboard() {
        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun closeKeyboard() {
        val imm: InputMethodManager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }


}