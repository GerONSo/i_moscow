package com.geron.ai_moscow_mobile.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.viewModels
import com.geron.ai_moscow_mobile.CallbackHelper
import com.geron.ai_moscow_mobile.R
import com.geron.ai_moscow_mobile.data_classes.User
import com.geron.ai_moscow_mobile.viewmodels.AuthorizeViewModel
import com.geron.ai_moscow_mobile.viewmodels.RegisterViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.runBlocking
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

class StartFragment : Fragment() {

    val authorizeViewModel: AuthorizeViewModel by viewModels()
    val registerViewModel: RegisterViewModel by viewModels()

    val startNextButton: Button by lazy { requireView().findViewById(R.id.btn_start_next) }
    val startMotionLayout: MotionLayout by lazy { requireView().findViewById(R.id.start_motion_layout) }
    val authorizationLoginEditText: EditText by lazy { requireView().findViewById(R.id.et_authorization_login) }
    val authorizationPasswordEditText: EditText by lazy { requireView().findViewById(R.id.et_password) }
    val buttonLogin: Button by lazy { requireView().findViewById(R.id.btn_login) }
    val buttonRegister: TextView by lazy { requireView().findViewById(R.id.tv_register) }
    val buttonRegistrationRegister: TextView by lazy { requireView().findViewById(R.id.btn_register) }
    val registrationLoginEditText: EditText by lazy { requireView().findViewById(R.id.et_registration_login) }
    val registrationPasswordEditText: EditText by lazy { requireView().findViewById(R.id.et_registration_password) }
    val registrationNameEditText: EditText by lazy { requireView().findViewById(R.id.et_registration_name) }

    var currentState = 0
    var isPasswordFocused = false
    var isLoginFocused = false

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
        registerViewModel.getRegistered().observe(viewLifecycleOwner, { isRegistered ->
            if (isRegistered) {
                CallbackHelper.onBackPressedStartFragment()
                Snackbar.make(
                    startMotionLayout,
                    "Пользователь успешно зарегестрирован",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                Snackbar.make(
                    startMotionLayout,
                    "Такой пользователь уже есть",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
        authorizeViewModel.apply {
            getAccountCookie().observe(viewLifecycleOwner, {
                CallbackHelper.onLogin()
            })
            getAuthorized().observe(viewLifecycleOwner, { isAuthorized ->
                closeKeyboard()
                if (!isAuthorized) {
                    Snackbar.make(
                        startMotionLayout,
                        "Вы ввели неправильные логин или пароль",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            })
        }
        buttonRegister.setOnClickListener {
            currentState = 2
            startMotionLayout.setTransition(R.id.registration_transition)
            startMotionLayout.transitionToEnd()
        }
        buttonRegistrationRegister.setOnClickListener {
            runBlocking {
                registerViewModel.register(
                    User(
                        registrationLoginEditText.text.toString(),
                        registrationPasswordEditText.text.toString(),
                        registrationNameEditText.text.toString()
                    )
                )
            }
            closeKeyboard()
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
            when (currentState) {
                1 -> {
                    startMotionLayout.setTransition(R.id.authorization_transition)
                    startMotionLayout.transitionToStart()
                    currentState = 0
                }
                2 -> {
                    startMotionLayout.setTransition(R.id.registration_transition)
                    startMotionLayout.transitionToStart()
                    currentState = 1
                }
            }
        }
        startMotionLayout.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && currentState == 1) {
                startMotionLayout.setTransition(R.id.authorization_keyboard_transition)
                startMotionLayout.transitionToStart()
            }
        }
        authorizationPasswordEditText.setOnFocusChangeListener { view, hasFocus ->
            isPasswordFocused = hasFocus
            if (currentState != 1) return@setOnFocusChangeListener
            if (hasFocus) {
                startMotionLayout.setTransition(R.id.authorization_keyboard_transition)
                startMotionLayout.transitionToEnd()
            }
        }
        authorizationLoginEditText.apply {
            onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                isLoginFocused = hasFocus
                if (currentState != 1) return@OnFocusChangeListener
                if (hasFocus) {
                    startMotionLayout.setTransition(R.id.authorization_keyboard_transition)
                    startMotionLayout.transitionToEnd()
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
                isLoginFocused = true
                isPasswordFocused = true
                authorizationLoginEditText.clearFocus()
                authorizationPasswordEditText.clearFocus()
                startMotionLayout.requestFocus()
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