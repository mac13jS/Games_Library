package com.example.gameslibrary.view

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.gameslibrary.R
import com.example.gameslibrary.model.LibraryAdapter
import com.google.android.material.tabs.TabLayout

class LibraryFragment : Fragment() {
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.library_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.viewPager)

        val adapter = LibraryAdapter(childFragmentManager)
        adapter.addFragment(AllGamesFragment(), "All Games")
        adapter.addFragment(FavouriteFragment(), "Favourite")
        adapter.addFragment(AlreadyPlayedFragment(), "Already played")

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_libraryFragment_to_mainFragment)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}