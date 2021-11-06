package com.devs.carpoolrouteplanner.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.devs.carpoolrouteplanner.databinding.FragmentHomeBinding
import com.devs.carpoolrouteplanner.ui.MainGroupActivity
import com.devs.carpoolrouteplanner.utils.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import java.util.*

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

    private fun getGroupList(): List<SerializedGroup> {
        var groupList: List<SerializedGroup>
        runBlocking {
            val userData = httpClient.get<SerializedUser>("${context?.getConfigValue("backend_url")}/users/me")
            groupList = userData.groups
        }
        return groupList
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

        val groupList = getGroupList()

        titleList = groupList.map { it.label }.toMutableList()
        descriptionList = groupList.map { it.gid.toString() }.toMutableList()
        recyclerView = binding.recyclerView
        recyclerAdapter = RecyclerAdapter(titleList, descriptionList)
        recyclerView.adapter = recyclerAdapter

        recyclerAdapter.setOnItemClickListener { position ->
            val intent = Intent(this.context, MainGroupActivity::class.java)
            intent.putExtra("groupId", groupList[position].gid)
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