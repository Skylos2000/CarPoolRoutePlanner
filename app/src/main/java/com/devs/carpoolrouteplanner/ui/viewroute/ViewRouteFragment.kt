package com.devs.carpoolrouteplanner.ui.viewroute

import android.os.Bundle
import android.view.Gravity.apply
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.devs.carpoolrouteplanner.ui.viewroute.ViewRouteFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.devs.carpoolrouteplanner.R

import com.devs.carpoolrouteplanner.ui.viewroute.ViewRouteViewModel
import java.util.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat.apply
import androidx.recyclerview.widget.LinearLayoutManager
import com.devs.carpoolrouteplanner.adapters.RecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.view.*

import kotlinx.android.synthetic.main.route_recycler_view.*


class ViewRouteFragment : Fragment() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RecyclerAdapter

    private var titleList = mutableListOf<String>()
    private var descriptionList = mutableListOf<String>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.route_recycler_view, container, false)
        }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        recycler_view.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            val unParsedData = getData()
            titleList = unParsedData[0]
            descriptionList = unParsedData[1]
            // set the custom adapter to the RecyclerView
            adapter = RecyclerAdapter(titleList,descriptionList)
            //val itemTouchHelper = ItemTouchHelper(simpleCallback)
            //itemTouchHelper.attachToRecyclerView(recyclerView)
        }
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
        return listOf(listOf("30","-90","Home"),listOf("29","-90","Work"),listOf("29","-89","Louisiana Tech"),listOf("29","-89.5","Tractor Supply"))
    }

    private var simpleCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP.or(
        ItemTouchHelper.DOWN),0){
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
    /*private lateinit var routeViewModel: ViewRouteViewModel
    private var _binding: FragmentRouteviewBinding? = null // just naming it
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        routeViewModel = ViewModelProvider(this).get(ViewRouteViewModel::class.java)
        _binding = FragmentRouteviewBinding.inflate(inflater,container,false)
        val root: View = binding.root

        val textView: TextView = binding.textViewRoute
       routeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }


override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

     */
}
