package com.example.gameslibrary.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gameslibrary.R
import com.example.gameslibrary.model.GameObject
import com.example.gameslibrary.viewmodel.Communicator
import com.example.gameslibrary.viewmodel.LibraryAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FavouriteFragment : Fragment() {
    lateinit var spinner: ProgressBar
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: LibraryAdapter
    private lateinit var communicator: Communicator
    private var filter: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_favourite, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)

        val menuItem = menu.findItem(R.id.search)
        val searchView = menuItem.actionView as SearchView
        searchView.queryHint = "Search in library"

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.setFilterName(filter.toString())
                adapter.filter.filter(newText)
                return true
            }
        })

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId

        when(id) {
            R.id.filterTitle -> filter = "title"
            R.id.filterDeveloper -> filter = "developer"
            R.id.filterGenre -> filter = "genre"
            R.id.filterDate -> filter = "date"
            R.id.filterPrice -> filter = "price"
        }

        return if (id == R.id.search) { true }
        else super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinner = view.findViewById(R.id.favouriteProgressBar)
        recyclerView = view.findViewById(R.id.favouriteRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        communicator = ViewModelProvider(requireActivity())[Communicator::class.java]

        getData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getData() {
        val currentUser = FirebaseAuth.getInstance().currentUser

        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("game")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val gameObjectsList: MutableList<Map.Entry<String, GameObject>> = arrayListOf()
                val gameObjectsMap: MutableMap<String, GameObject> = mutableMapOf()

                val data = dataSnapshot.children
                data.forEach {
                    if (it.child("userId").value.toString() == currentUser!!.uid &&
                        it.child("favourite").value.toString().toBoolean()
                    ) {
                        val gameObject = GameObject(
                            it.child("title").value.toString(),
                            it.child("developer").value.toString(),
                            it.child("genre").value.toString(),
                            it.child("date").value.toString(),
                            it.child("price").value.toString(),
                            it.child("userId").value.toString(),
                            it.child("favourite").value.toString().toBoolean(),
                            it.child("alreadyPlayed").value.toString().toBoolean()
                        )

                        gameObjectsMap[it.key.toString()] = gameObject
                    }
                }

                gameObjectsMap.forEach { gameObjectsList.add(it) }

                adapter = LibraryAdapter(gameObjectsList)
                adapter.notifyDataSetChanged()
                spinner.visibility = View.GONE
                recyclerView.adapter = adapter

                adapter.onItemClick = { gameObject ->
                    communicator.setKey(gameObject.key)
                    communicator.setGameObject(gameObject.value)
                    findNavController().navigate(R.id.action_libraryFragment_to_editFragment)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}