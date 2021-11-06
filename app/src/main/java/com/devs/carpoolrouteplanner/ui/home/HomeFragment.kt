package com.devs.carpoolrouteplanner.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.devs.carpoolrouteplanner.databinding.FragmentHomeBinding
import com.devs.carpoolrouteplanner.ui.MainActivity
import com.devs.carpoolrouteplanner.ui.MainGroupActivity
import com.devs.carpoolrouteplanner.utils.getConfigValue
import com.devs.carpoolrouteplanner.utils.httpClient
import io.ktor.client.request.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import com.devs.carpoolrouteplanner.utils.getConfigValue

class HomeFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RecyclerAdapter

    private var titleList = mutableListOf<String>()
    private var descriptionList = mutableListOf<String>()

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    lateinit var textView: TextView

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    fun getGroupList() {
        lifecycleScope.launch {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        textView = binding.textHome
        //homeViewModel.text.observe(viewLifecycleOwner) { textView.text = it }

        var groups: String
        runBlocking {
            groups = httpClient.get<String>("http://192.168.1.10:8080/users/me/groups")
        }
        groups = groups.replace("[", "")
        groups = groups.replace("]", "")

        val groupList: Array<MutableList<String>>


        var unParsedData: Array<MutableList<String>>
        if ("," in groups){
            groupList = myGetData(groups.split(","))
        }else{
            groupList = myGetData(listOf(groups))
        }


       val intent = Intent(this.context, MainGroupActivity::class.java)

        unParsedData = groupList
        //val unParsedData = getData()
        titleList = unParsedData[0]
        descriptionList = unParsedData[1]
        recyclerView = binding.recyclerView
        recyclerAdapter = RecyclerAdapter(titleList,descriptionList)
        var adapter = recyclerAdapter
        recyclerView.adapter = recyclerAdapter
        adapter.setOnItemClickListener { position ->
            val arr = groupList[0][position].toInt()
            intent.putExtra("groupId", groupList[0][position].toInt())
            startActivity(intent)
        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun myGetData(something: List<String>): Array<MutableList<String>> {
        var cords = mutableListOf<String>()
        var titles = mutableListOf<String>()
        for (i in something){

            cords.add("")
            titles.add(i)
        }
        return arrayOf(titles, cords)
    }

    private fun getData(): Array<MutableList<String>> {
        var routedata = getDataFromDb()
        var cords = mutableListOf<String>()
        var title = mutableListOf<String>()
        for (aList in routedata) {
            cords.add("Coordinates: " + aList.elementAt(0) + "," + aList.elementAt(1))
            title.add("" + aList.elementAt(2))
        }
        return arrayOf(title, cords)

    }

    private fun getDataFromDb(): List<List<String>>{
//        val ss: String
//        runBlocking {
//            ss = httpClient.get<String>("http://138.47.134.41:8080/users/me")
//        }
//        textView.text = "hihcenjk"


        return listOf(listOf("30","-90","Home"),listOf("29","-90","Work"),listOf("29","-89","Louisiana Tech"),listOf("29","-89.5","Tractor Supply"))
    }

    private var simpleCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP.or(ItemTouchHelper.DOWN),0){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            var startPosition = viewHolder.bindingAdapterPosition
            var endPosition = target.bindingAdapterPosition
            Collections.swap(titleList, startPosition, endPosition)
            recyclerView.adapter?.notifyItemMoved(startPosition, endPosition)//send back to db here
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        }

    }
}