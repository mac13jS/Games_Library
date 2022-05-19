package com.example.gameslibrary.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.gameslibrary.R
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class RegisterFragment : Fragment() {
    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if(resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
            inflater.inflate(R.layout.register_fragment, container, false)
        }
        else {
            inflater.inflate(R.layout.register_fragment_landscape, container, false)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailEditText = view.findViewById(R.id.registerEmail)
        passwordEditText = view.findViewById(R.id.registerPassword)

        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                hideSoftKeyboard(this.requireActivity())
                view.findViewById<EditText>(R.id.registerEmail).clearFocus()
                view.findViewById<EditText>(R.id.registerPassword).clearFocus()
                false
            }
        }

        view.findViewById<Button>(R.id.registerButton).setOnClickListener {
            var email: String ?= null
            var password: String ?= null

            if(emailEditText.text.toString() == "") {
                Toast.makeText(this.context, "E-mail field cannot be empty", Toast.LENGTH_LONG).show()
            }
            else {
                email = emailEditText.text.toString()
            }

            if(passwordEditText.text.toString() == "") {
                Toast.makeText(this.context, "Password field cannot be empty", Toast.LENGTH_LONG).show()
            }
            else {
                password = passwordEditText.text.toString()
            }

            if (email != null && password != null) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this.context, "Registration successful", Toast.LENGTH_SHORT).show()

                            emailEditText.text.clear()
                            passwordEditText.text.clear()

                            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                        }
                        else {
                            Toast.makeText(this.context, task.exception!!.message, Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

        view.findViewById<TextView>(R.id.registerToLogin).setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

            emailEditText.text = null
            passwordEditText.text = null
        }
    }

    private fun hideSoftKeyboard(activity: Activity) {
        try {
            val inputMethodManager: InputMethodManager = activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            if (inputMethodManager.isAcceptingText) {
                inputMethodManager.hideSoftInputFromWindow(
                    activity.currentFocus!!.windowToken,
                    0
                )
            }
        }
        catch(e: Exception) {}
    }
}