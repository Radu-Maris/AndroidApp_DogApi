package com.example.assignment2_maris_radu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BreedsAdapter(private val breedsList: List<String>,private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<BreedsAdapter.BreedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.breeds_item, parent, false)
        return BreedViewHolder(view)
    }

    override fun onBindViewHolder(holder: BreedViewHolder, position: Int) {
        val breed = breedsList[position]
        holder.breedTextView.text = breed

        holder.itemView.setOnClickListener {
            onItemClick(breed)
        }
    }

    override fun getItemCount(): Int = breedsList.size

    inner class BreedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val breedTextView: TextView = itemView.findViewById(R.id.breedTextView)
    }
}
