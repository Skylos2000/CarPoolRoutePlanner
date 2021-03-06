package com.devs.carpoolrouteplanner.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devs.carpoolrouteplanner.R

class RecyclerAdapter(titlesList: MutableList<String>,descriptionList: MutableList<String>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    private var titles: MutableList<String> = titlesList
    private var des: MutableList<String> = descriptionList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.route_recycler_view_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.itemTitle.text = titles[position]
        holder.itemDescription.text = des[position]

    }

    override fun getItemCount(): Int {
        return titles.size
    }
    fun getTitle(position: Int): String {
        return titles[position]
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemTitle: TextView
        var itemDescription: TextView
        init{
            itemTitle = itemView.findViewById(R.id.title_card_view)
            itemDescription = itemView.findViewById(R.id.description_card_view)
        }

    }

}