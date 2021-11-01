package com.devs.carpoolrouteplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class GroupHostMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.grouphostmenu)

        val inviteMembersButton = findViewById<Button>(R.id.groupHostMenu_inviteMembersButton)
        val viewCurrentGroupsButton = findViewById<Button>(R.id.groupHostMenu_viewCurrrentGroupsButton)
        val removeMemberButton = findViewById<Button>(R.id.groupHostMenu_removeMemberButton)
        val disbandGroupButton = findViewById<Button>(R.id.groupHostMenu_disbandGroupButton)
        val logOutButton = findViewById<Button>(R.id.groupHostMenu_logOutButton)

        val hostInviteMembers = Intent(this@GroupHostMenu, HostInviteMembers::class.java)
        val viewCurrentMembers = Intent(this@GroupHostMenu, ViewCurrentMembers::class.java)
        val hostRemoveMembers = Intent(this@GroupHostMenu, HostRemoveMembers::class.java)
        val hostDisbandMembers = Intent(this@GroupHostMenu, HostDisbandMembers::class.java)
        val hostLogOut = Intent(this@GroupHostMenu, HostLogOut::class.java)

        inviteMembersButton.setOnClickListener {
            startActivity(hostInviteMembers)
        }

        viewCurrentGroupsButton.setOnClickListener {
            startActivity(viewCurrentMembers)
        }

        removeMemberButton.setOnClickListener {
            startActivity(hostRemoveMembers)
        }

        disbandGroupButton.setOnClickListener {
            startActivity(hostDisbandMembers)
        }

        logOutButton.setOnClickListener {
            startActivity(hostLogOut)
        }
    }
}