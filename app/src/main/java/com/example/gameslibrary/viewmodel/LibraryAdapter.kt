package com.example.gameslibrary.viewmodel

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gameslibrary.R
import com.example.gameslibrary.model.GameObject
import java.util.*

class LibraryAdapter(private val list: MutableList<Map.Entry<String, GameObject>>): RecyclerView.Adapter<LibraryAdapter.ViewHolder>(), Filterable {
    var gamesListFiltered: MutableList<Map.Entry<String, GameObject>> = arrayListOf()
    var filterType: String = ""
    var onItemClick: ((Map.Entry<String, GameObject>) -> Unit)? = null

    init {
        gamesListFiltered = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.game_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gameObject = gamesListFiltered[position]

        holder.title.text = gameObject.value.title
        holder.developer.text = gameObject.value.developer
        holder.genre.text = gameObject.value.genre
        holder.date.text = gameObject.value.date
        holder.price.text = gameObject.value.price
    }

    override fun getItemCount(): Int {
        return gamesListFiltered.size
    }

    fun setFilterName(value: String) {
        filterType = value
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()

                gamesListFiltered = if (charSearch.isEmpty()) {
                    list
                } else {
                    val result: MutableList<Map.Entry<String, GameObject>> = arrayListOf()

                    for (item in list) {
                        when(filterType) {
                            "developer" -> {
                                if (item.value.developer.lowercase(Locale.ROOT).contains(constraint.toString().lowercase(Locale.ROOT))) {
                                    result.add(item)
                                }
                            }

                            "genre" -> {
                                if (item.value.genre.lowercase(Locale.ROOT).contains(constraint.toString().lowercase(Locale.ROOT))) {
                                    result.add(item)
                                }
                            }

                            "date" -> {
                                if (item.value.date.lowercase(Locale.ROOT).contains(constraint.toString().lowercase(Locale.ROOT))) {
                                    result.add(item)
                                }
                            }

                            "price" -> {
                                if (item.value.price.lowercase(Locale.ROOT).contains(constraint.toString().lowercase(Locale.ROOT))) {
                                    result.add(item)
                                }
                            }

                            else -> {
                                if (item.value.title.lowercase(Locale.ROOT).contains(constraint.toString().lowercase(Locale.ROOT))) {
                                    result.add(item)
                                }
                            }
                        }
                    }

                    result
                }

                val filterResult = FilterResults()
                filterResult.values = gamesListFiltered

                return filterResult
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                gamesListFiltered = results?.values as MutableList<Map.Entry<String, GameObject>>
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val title: TextView = itemView.findViewById(R.id.gameTitle)
        val developer: TextView = itemView.findViewById(R.id.gameDeveloper)
        val genre: TextView = itemView.findViewById(R.id.gameGenre)
        val date: TextView = itemView.findViewById(R.id.gameDate)
        val price: TextView = itemView.findViewById(R.id.gamePrice)

        init {
            view.setOnClickListener {
                onItemClick?.invoke(gamesListFiltered[adapterPosition])
            }
        }
    }
}