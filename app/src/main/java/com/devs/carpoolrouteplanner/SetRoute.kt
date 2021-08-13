package com.devs.carpoolrouteplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.devs.carpoolrouteplanner.adapters.ItemAdapters
import com.devs.carpoolrouteplanner.adapters.ItemList
import com.devs.carpoolrouteplanner.utils.getConfigValue
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.launch


class SetRoute : AppCompatActivity() , AdapterView.OnItemClickListener{
    private var listView: ListView? =null
    private var itemAdapters:ItemAdapters ? =null
    private var arrayList:ArrayList<ItemList> ? =null

    private var data = "n/a"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_route)

        listView = findViewById(R.id.cardview_list_view)
        arrayList = ArrayList()
        arrayList = setDataItem()
        itemAdapters = ItemAdapters(applicationContext,arrayList!!)
        listView?.adapter = itemAdapters
        listView?.onItemClickListener = this
        getData("")

    }
    private fun setDataItem() :ArrayList<ItemList>{
        var listItem:ArrayList<ItemList> = ArrayList()
        listItem.add(
            ItemList(
                "Final Destination", data
            )
        )

        return listItem
    }
    private fun getData(verb:String?){//:String?
        val userN = AccountSignIn.creds[0]
        val passW = AccountSignIn.creds[1]
        val url = getConfigValue("backend_url")
        val gid = 456
        lifecycleScope.launch {
            val client = HttpClient(CIO) {
                install(Auth) {
                    basic {
                        credentials {
                            BasicAuthCredentials(username = userN, password = passW)
                        }
                    }
                }
                install(JsonFeature) {
                    serializer = KotlinxSerializer()
                }
            }
            var destinationString = ""
            try {
                val response: HttpResponse = client.get("$url/get_group_routes/$gid")
                destinationString += client.get<String>(url + "/get_group_routes/$gid").toString()
                var data = response.toString()
                if (response.status.value == 404) {
                    Toast.makeText(this@SetRoute, "404 not found", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this@SetRoute, "Success!", Toast.LENGTH_SHORT).show()
                    var items: ItemList = arrayList?.get(0)!!
                    items.description = destinationString
                }
            }
            catch (e: Exception) {
                Toast.makeText(
                    this@SetRoute,
                    "Problem Communicating With Server",
                    Toast.LENGTH_LONG
                ).show()
            }
            //finish()
        }

        return
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //if item.title == verb then open intent
        //startActivity(intentSetGroupDest)
        var items: ItemList = arrayList?.get(position)!!
        Toast.makeText(applicationContext,items.title,Toast.LENGTH_LONG).show()
        startActivity(Intent(this@SetRoute, SetGroupDest::class.java))
    }
}

