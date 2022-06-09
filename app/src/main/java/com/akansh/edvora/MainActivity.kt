package com.akansh.edvora

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akansh.edvora.fragments.NearestFragment
import com.akansh.edvora.fragments.PastFragment
import com.akansh.edvora.fragments.UpcomingFragment
import com.akansh.edvora.navigation.TabsAdapter
import com.akansh.edvora.navigation.TabsViewModel
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import org.json.JSONException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity(), TabsAdapter.TabClickListener {

    var tabs_adapter: TabsAdapter? = null
    val nearestFragment = NearestFragment(this)
    val upcomingFragment = UpcomingFragment()
    val pastFragment = PastFragment()

    var spinnerState : PowerSpinnerView? = null
    var spinnerCity : PowerSpinnerView? = null
    var locations: DuplicateMap<String,String> = DuplicateMap()

    var all_data: ArrayList<ListData> = ArrayList()
    var u_data: ArrayList<ListData> = ArrayList()
    var p_data: ArrayList<ListData> = ArrayList()


    var filterState = ""
    var filterCity = ""

    lateinit var myDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<View>(R.id.tabs_list) as RecyclerView

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager

        val data = ArrayList<TabsViewModel>()

        data.add(TabsViewModel(Constants.TABS[0],true))
        data.add(TabsViewModel(Constants.TABS[1],false))
        data.add(TabsViewModel(Constants.TABS[2],false))

        tabs_adapter = TabsAdapter(data)
        tabs_adapter!!.initOnClickInterface(this)

        recyclerView.adapter = tabs_adapter

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frame,nearestFragment)
        transaction.addToBackStack(null)
        transaction.commit()

        volleyGet()

        val filter_btn = findViewById<ConstraintLayout>(R.id.filter_btn)
        myDialog = Dialog(this)
        val wmlp: WindowManager.LayoutParams = myDialog.getWindow()!!.getAttributes()
        wmlp.gravity = Gravity.TOP or Gravity.RIGHT
        wmlp.x = 100
        wmlp.y = 300

        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        myDialog.setCancelable(true)
        myDialog.setContentView(R.layout.filter_view)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        spinnerState = myDialog.findViewById(R.id.spinner_state)
        spinnerCity = myDialog.findViewById(R.id.spinner_city)

        val clear_filter_btn = myDialog.findViewById<ImageButton>(R.id.clear_filter)
        clear_filter_btn.setOnClickListener {
            spinnerCity?.clearSelectedItem()
            spinnerState?.clearSelectedItem()
            nearestFragment.remove_filter(all_data)
            pastFragment.remove_filter(u_data)
            upcomingFragment.remove_filter(p_data)
        }


        spinnerState?.setOnSpinnerItemSelectedListener(object :
            OnSpinnerItemSelectedListener<String?> {
            override fun onItemSelected(
                oldIndex: Int,
                oldItem: String?,
                newIndex: Int,
                newItem: String?
            ) {
                spinnerCity?.setItems(locations.get(newItem.toString()).distinct().toList())
                filterState = newItem.toString()
                spinnerCity?.clearSelectedItem()
                filterCity = ""
                filter()
            }
        })

        spinnerCity?.setOnSpinnerItemSelectedListener(object :
            OnSpinnerItemSelectedListener<String?> {
            override fun onItemSelected(
                oldIndex: Int,
                oldItem: String?,
                newIndex: Int,
                newItem: String?
            ) {
                filterCity = newItem.toString()
                filter()
            }
        })

        filter_btn.setOnClickListener{
            myDialog.show()
        }
    }

    fun filter() {
        nearestFragment.filter(all_data,filterState,filterCity)
        try {
            pastFragment.filter(u_data,filterState,filterCity)
        }catch (e: Exception){}
        try {
            upcomingFragment.filter(p_data,filterState,filterCity)
        }catch (e: Exception){}
        try {
            tabs_adapter?.setTitle(1, Constants.TABS[1]+ "(" + upcomingFragment.getSize() + ")")
            tabs_adapter?.setTitle(2, Constants.TABS[2]+ "(" + pastFragment.getSize() + ")")
        }catch (e: Exception) {}
    }

    override fun onTabClickListener(pos: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        if(pos == 0) {
            transaction.replace(R.id.frame,nearestFragment)
        }else if(pos == 1) {
            transaction.replace(R.id.frame,upcomingFragment)
        }else if(pos == 2) {
            transaction.replace(R.id.frame,pastFragment)
        }
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun volleyGet() {
        val requestQueue = Volley.newRequestQueue(this)
        val states = ArrayList<String>()
        val cities = ArrayList<String>()

        val request = JsonArrayRequest(Request.Method.GET, Constants.API_BASE, null, {
                response ->try {
            for (i in 0 until response.length()) {
                val data = response.getJSONObject(i)
                val ride_id = data.getString("id")
                val station_code = data.getString("origin_station_code")
                val station_path = data.getString("station_path")
                val map_url = data.getString("map_url")
                val state = data.getString("state")
                val city = data.getString("city")

                val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm aa")
                val dt1 = SimpleDateFormat("dd MMM yyyy hh:mm aa")
                val date = formatter.parse(data.getString("date"))

                val item = ListData("",map_url,city,state,ride_id,station_code,station_path,dt1.format(date).toString(),"0")
                if(date.compareTo(Date()) > 0 ) {
                    upcomingFragment.addItem(item)
                    u_data.add(item)
                }else if(date.compareTo(Date()) < 0) {
                    pastFragment.addItem(item)
                    p_data.add(item)
                }
                states.add(state)
                cities.add(city)
                locations.put(state,city)
                nearestFragment.addItem(item)
                all_data.add(item)
            }

            tabs_adapter?.setTitle(1, Constants.TABS[1]+ "(" + upcomingFragment.getSize() + ")")
            tabs_adapter?.setTitle(2, Constants.TABS[2]+ "(" + pastFragment.getSize() + ")")

            spinnerState?.run {
                this.setItems(states.distinct().toList())
            }
            spinnerCity?.run {
                this.setItems(cities.distinct().toList())
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            Log.d(Constants.LOG, "Request Error: "+e.toString())
        }
        }, {
            error -> error.printStackTrace()
            Log.d(Constants.LOG, "Request Error: "+error.toString())
        })
        requestQueue.add(request)
    }

}
