package com.akansh.edvora.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akansh.edvora.ListAdapter
import com.akansh.edvora.ListData
import com.akansh.edvora.R

class NearestFragment constructor(context: Context): Fragment() {

    var adapter: ListAdapter? = null
    var data: ArrayList<ListData> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val v : View = inflater.inflate(
            R.layout.nearest_layout_fragment, container, false
        )

        val recyclerView = v.findViewById<View>(R.id.list) as RecyclerView
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        adapter = ListAdapter(data)

        recyclerView.adapter = adapter

        return v
    }

    fun addItem(item: ListData) {
        data.add(item)
        adapter?.addItem(item)
    }

    fun filter(data: ArrayList<ListData>,state: String, city: String) {
        this.data = adapter?.filter(data,state,city)!!
    }

    fun remove_filter(data: ArrayList<ListData>) {
        adapter?.remove_filter(data)
    }

}