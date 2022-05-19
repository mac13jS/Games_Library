package com.example.gameslibrary.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gameslibrary.R
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception
import kotlin.system.exitProcess

class LoginFragment : Fragment() {
    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if(resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
            inflater.inflate(R.layout.login_fragment, container, false)
        }
        else {
            inflater.inflate(R.layout.login_fragment_landscape, container, false)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailEditText = view.findViewById(R.id.loginEmail)
        passwordEditText = view.findViewById(R.id.loginPassword)

        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                hideSoftKeyboard(this.requireActivity())
                view.findViewById<EditText>(R.id.loginEmail).clearFocus()
                view.findViewById<EditText>(R.id.loginPassword).clearFocus()
                false
            }
        }

        view.findViewById<Button>(R.id.loginButton).setOnClickListener {
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
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            emailEditText.text.clear()
                            passwordEditText.text.clear()

                            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                        }
                        else {
                            Toast.makeText(this.context, task.exception!!.message, Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

        view.findViewById<TextView>(R.id.loginToRegister).setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)

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

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure you want to close the app?")
                builder.setTitle("Warning")
                builder.setCancelable(false)
                builder.setNegativeButton("NO") { dialog, _ -> dialog.cancel() }
                builder.setPositiveButton("YES") { _, _ -> run {
                        activity!!.finish()
                        exitProcess(0)
                    }
                }

                val alert = builder.create()
                alert.show()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}