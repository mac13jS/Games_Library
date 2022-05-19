package com.example.gameslibrary.view

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.gameslibrary.R
import com.example.gameslibrary.model.GameObject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class AddNewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_new_fragment, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title: EditText = view.findViewById(R.id.addNewTitle)
        val developer: EditText = view.findViewById(R.id.addNewDeveloper)
        val genre: EditText = view.findViewById(R.id.addNewGenre)
        val date: EditText = view.findViewById(R.id.addNewDate)
        val price: EditText = view.findViewById(R.id.addNewPrice)
        val favourite: CheckBox = view.findViewById(R.id.addNewFavourite)
        val alreadyPlayed: CheckBox = view.findViewById(R.id.addNewPlayed)

        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                hideSoftKeyboard(this.requireActivity())
                title.clearFocus()
                developer.clearFocus()
                genre.clearFocus()
                date.clearFocus()
                price.clearFocus()
                favourite.clearFocus()
                alreadyPlayed.clearFocus()
                false
            }
        }

        view.findViewById<Button>(R.id.addNewButton).setOnClickListener {
            var titleEmpty = false
            var developerEmpty = false
            var genreEmpty = false
            var dateEmpty = false
            var priceEmpty = false

            if (title.text.toString() == "") { titleEmpty = true }
            if (developer.text.toString() == "") { developerEmpty = true }
            if (genre.text.toString() == "") { genreEmpty = true }
            if (date.text.toString() == "") { dateEmpty = true }
            if (price.text.toString() == "") { priceEmpty = true }

            if (titleEmpty && developerEmpty && genreEmpty && dateEmpty && priceEmpty) {
                Toast.makeText(this.context, "You must fill at least one field", Toast.LENGTH_LONG).show()
            }
            else {
                val currentUser = FirebaseAuth.getInstance().currentUser

                val game = GameObject(
                    title.text.toString(),
                    developer.text.toString(),
                    genre.text.toString(),
                    date.text.toString(),
                    price.text.toString().replace(",", "."),
                    currentUser!!.uid,
                    favourite.isChecked,
                    alreadyPlayed.isChecked
                )

                val firebaseDatabase = FirebaseDatabase.getInstance()
                val reference = firebaseDatabase.reference

                reference.child("game").push().setValue(game)
                Toast.makeText(this.context, "Game added successfully", Toast.LENGTH_SHORT).show()

                title.text.clear()
                developer.text.clear()
                genre.text.clear()
                date.text.clear()
                price.text.clear()
                favourite.isChecked = false
                alreadyPlayed.isChecked = false
            }
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
