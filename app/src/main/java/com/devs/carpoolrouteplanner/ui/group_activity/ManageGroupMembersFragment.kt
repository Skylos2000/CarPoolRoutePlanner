package com.devs.carpoolrouteplanner.ui.group_activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devs.carpoolrouteplanner.R
import com.devs.carpoolrouteplanner.adapters.RecyclerAdapter
import com.devs.carpoolrouteplanner.ui.MainGroupActivity
import com.devs.carpoolrouteplanner.utils.GroupDestination
import com.devs.carpoolrouteplanner.utils.getConfigValue
import com.devs.carpoolrouteplanner.utils.httpClient
import io.ktor.client.features.*
import io.ktor.client.features.get
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.android.synthetic.main.fragment_manage_group_members.*
import kotlinx.coroutines.runBlocking
import java.util.*


class ManageGroupMembersFragment : Fragment() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RecyclerAdapter
    private val backendUrl get() = requireContext().getConfigValue("backend_url")!!
    private var titleList = mutableListOf<String>()
    private var descriptionList = mutableListOf<String>()
    private var users = listOf<String>()
    val gid get() = (activity as MainGroupActivity).gid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_group_members, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        recycler_view.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)

//            val unParsedData = getData()
//            titleList = unParsedData[0]
//            descriptionList = unParsedData[1]

            getDataFromDb()
            // set the custom adapter to the RecyclerView
            adapter = RecyclerAdapter(titleList,descriptionList)
            val itemTouchHelper = ItemTouchHelper(simpleCallback)
            itemTouchHelper.attachToRecyclerView(this)

            var invitemembersbutton = invite_members
            invitemembersbutton.setOnClickListener{
                findNavController().navigate(R.id.action_navigation_group_manage_members_to_nav_qr_invite)
            }


        }
    }
    var simpleCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.RIGHT,ItemTouchHelper.LEFT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder,
        ): Boolean {
            var startPosition = viewHolder.bindingAdapterPosition
            var endPosition = target.bindingAdapterPosition
            recyclerView.adapter?.notifyItemMoved(startPosition, endPosition)//send back to db here
            return true
        }
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            deleteMember(gid,users[viewHolder.bindingAdapterPosition].toInt())
        }
    }
    private fun deleteMember(gid: Int,uid: Int){
        //TODO add route to db
//        runBlocking {
//            httpClient.get<String>("/groups/$gid/members/$uid/delete")
//        }
//        reload()
    }
    private fun reload(){
        findNavController().navigate(R.id.action_reload_members)
    }
//    private fun getData(): Array<MutableList<String>> {
//        var routedata = getDataFromDb()
//        var cords = mutableListOf<String>()
//        var title = mutableListOf<String>()
//        for (aList in routedata) {
//            cords.add("Coordinates: " + aList.elementAt(0) + "," + aList.elementAt(1))
//            title.add("" + aList.elementAt(2))
//        }
//        return arrayOf(title, cords)
//
//    }

    private fun getDataFromDb() {
        val gid = (activity as MainGroupActivity).gid
        val members = runBlocking {
            httpClient.get<List<Pair<String, Int>>>(requireContext().getConfigValue("backend_url")!! + "/groups/$gid/members")
        }
        titleList = members.map { it.first }.toMutableList()
        descriptionList = members.map { it.second.toString() }.toMutableList()
        users=descriptionList

//        return listOf(listOf("30","-90","Landon"),listOf("29","-90","Kyle"),listOf("29","-89","Solomon"),listOf("29","-89.5","Leron")
    }
}