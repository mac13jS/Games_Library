package com.example.gameslibrary.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.gameslibrary.R
import com.example.gameslibrary.model.GameObject
import com.example.gameslibrary.viewmodel.Communicator
import com.google.firebase.database.*
import kotlinx.coroutines.*

class EditFragment : Fragment() {
    private lateinit var communicator: Communicator
    private lateinit var gameObject: GameObject
    private lateinit var key: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        communicator = ViewModelProvider(requireActivity())[Communicator::class.java]

        initialize(view)
    }

    private fun initialize(view: View) = runBlocking {
        val scope = CoroutineScope(Dispatchers.Main + Job())

        scope.launch { getGameObject() }
        scope.launch { displayGameObject(view) }
    }

    private fun getGameObject() {
        gameObject = communicator.gameObject
        key = communicator.key
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun displayGameObject(view: View) {
        val title: EditText = view.findViewById(R.id.editTitle)
        val developer: EditText = view.findViewById(R.id.editDeveloper)
        val genre: EditText = view.findViewById(R.id.editGenre)
        val date: EditText = view.findViewById(R.id.editDate)
        val price: EditText = view.findViewById(R.id.editPrice)
        val favourite: CheckBox = view.findViewById(R.id.editFavourite)
        val alreadyPlayed: CheckBox = view.findViewById(R.id.editPlayed)

        title.setText(gameObject.title)
        developer.setText(gameObject.developer)
        genre.setText(gameObject.genre)
        date.setText(gameObject.date)
        price.setText(gameObject.price)
        favourite.isChecked = gameObject.favourite
        alreadyPlayed.isChecked = gameObject.alreadyPlayed

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

        view.findViewById<Button>(R.id.editButton).setOnClickListener {
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
                val game = GameObject(
                    title.text.toString(),
                    developer.text.toString(),
                    genre.text.toString(),
                    date.text.toString(),
                    price.text.toString().replace(",", "."),
                    gameObject.userId,
                    favourite.isChecked,
                    alreadyPlayed.isChecked
                )

                val firebaseDatabase = FirebaseDatabase.getInstance()
                val reference = firebaseDatabase.reference

                reference.child("game").child(key).setValue(game)
                Toast.makeText(this.context, "Game edited successfully", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<Button>(R.id.editDeleteButton).setOnClickListener {
            val builder = AlertDialog.Builder(context)

            builder.setTitle("Warning")
            builder.setMessage("Are you sure you want to delete this game?")
            builder.setCancelable(false)
            builder.setNegativeButton("NO") { dialog, _ -> dialog.cancel() }
            builder.setPositiveButton("YES") { _, _ -> deleteGameObject() }

            builder.create().show()
        }
    }

    private fun deleteGameObject() {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val reference = firebaseDatabase.reference

        reference.child("game").child(key).removeValue()
        findNavController().navigate(R.id.action_editFragment_to_libraryFragment)
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