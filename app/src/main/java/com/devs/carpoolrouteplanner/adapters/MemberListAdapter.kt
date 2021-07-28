package com.devs.carpoolrouteplanner.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.devs.carpoolrouteplanner.R
import com.devs.carpoolrouteplanner.viewmodals.MemberModel

class MemberListAdapter(private val context:Context,private val listOfItems:Array<MemberModel>) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return listOfItems.size
    }

    override fun getItem(p0: Int): MemberModel {
        return listOfItems[p0]
    }

    override fun getItemId(p0: Int): Long {
        return listOfItems[p0].hashCode() + 0L
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.list_item_member, p2, false)
        val titleTextView = rowView.findViewById(R.id.memberListItemTitle) as TextView
        val subtitleTextView = rowView.findViewById<TextView>(R.id.memberListItemSubtitle)

        val item = getItem(p0) as MemberModel

        titleTextView.text = item.name
        subtitleTextView.text = item.latitude.toString()+ "/" +item.longitude.toString()

        return rowView
    }

}