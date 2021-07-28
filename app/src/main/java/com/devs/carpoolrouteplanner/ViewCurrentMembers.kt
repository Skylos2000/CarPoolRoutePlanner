package com.devs.carpoolrouteplanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.lifecycle.ViewModelProvider
import com.devs.carpoolrouteplanner.adapters.MemberListAdapter
import com.devs.carpoolrouteplanner.viewmodals.LoginViewModal
import com.devs.carpoolrouteplanner.viewmodals.ViewCurrentMembersViewModal

class ViewCurrentMembers : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewcurrentmembers)
        val currentModel: ViewCurrentMembersViewModal = ViewModelProvider(this).get(ViewCurrentMembersViewModal::class.java)

        currentModel.loadMembers()
        var listView = findViewById<ListView>(R.id.listView)

        currentModel.currentMembers.observe(this,{
            if( it!= null) {
                val adapter = MemberListAdapter(this, it)
                listView.adapter = adapter
            }
        })
        //View Current Members
    }
}