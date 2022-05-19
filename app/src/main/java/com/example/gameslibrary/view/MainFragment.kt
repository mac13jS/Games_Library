package com.example.gameslibrary.view

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.gameslibrary.R
import com.google.firebase.auth.FirebaseAuth

class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if(resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
            inflater.inflate(R.layout.main_fragment, container, false)
        }
        else {

            inflater.inflate(R.layout.main_fragment_landscape, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.addNewButton).setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addNewFragment)
        }

        view.findViewById<Button>(R.id.libraryButton).setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_libraryFragment)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}