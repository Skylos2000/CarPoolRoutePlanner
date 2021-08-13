package com.devs.carpoolrouteplanner.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.devs.carpoolrouteplanner.R

class ItemAdapters(var context: Context,var arrayList: ArrayList<ItemList>) :BaseAdapter() {
    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(position: Int): Any {
        return arrayList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View = View.inflate(context , R.layout.cardview_set_route_item_layout_list,null)
        var title:TextView = view.findViewById(R.id.title_card_view)
        var description:TextView = view.findViewById(R.id.description_card_view)
        var items:ItemList = arrayList.get(position)
        title.text = items.title
        description.text = items.description
        return view!!
    }


}