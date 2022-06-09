package com.akansh.edvora.fragments

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

class PastFragment : Fragment() {

    var adapter: ListAdapter? = null
    var data: ArrayList<ListData> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val v : View = inflater.inflate(
            R.layout.past_layout_fragment, container, false
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

    fun getSize(): Int {
        return data.size
    }

    fun filter(data: ArrayList<ListData>,state: String, city: String) {
        this.data = adapter?.filter(data,state,city)!!
    }

    fun remove_filter(data: ArrayList<ListData>) {
        adapter?.remove_filter(data)
    }
}