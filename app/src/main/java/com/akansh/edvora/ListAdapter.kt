package com.akansh.edvora

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class ListAdapter(private var mList: ArrayList<ListData>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private val listFiltered: ArrayList<ListData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mList[position]
        Picasso
            .get()
            .load(data.img)
            .into(holder.image)
        holder.city_name.setText(data.city_name)
        holder.state_name.setText(data.state_name)
        holder.ride_id.setText(data.ride_id)
        holder.origin_station.setText(data.origin_station)
        holder.station_path.setText(data.station_path)
        holder.date.setText(data.date)
        holder.distance.setText(data.distance)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun addItem(item : ListData) {
        mList.add(item)
        notifyDataSetChanged()
    }

    fun filter(data : ArrayList<ListData>, state: String, city: String): ArrayList<ListData> {
        listFiltered.clear()
        if(state.length>0 && city.length == 0) {
            for(item in data) {
                if(item.state_name == state) {
                    listFiltered.add(item)
                }
            }
        }else if(state.length==0 && city.length > 0) {
            for(item in data) {
                if(item.city_name == city) {
                    listFiltered.add(item)
                }
            }
        }else{
            for (item in data) {
                if (item.state_name == state && item.city_name == city) {
                    listFiltered.add(item)
                }
            }
        }
        mList = listFiltered
        notifyDataSetChanged()
        return mList
    }

    fun remove_filter(data : ArrayList<ListData>) {
        mList = data
        notifyDataSetChanged()
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val image: ImageView = itemView.findViewById(R.id.img)
        val city_name: TextView = itemView.findViewById(R.id.city_name)
        val state_name: TextView = itemView.findViewById(R.id.state_name)
        val ride_id: TextView = itemView.findViewById(R.id.ride_id)
        val origin_station: TextView = itemView.findViewById(R.id.origin_station)
        val station_path: TextView = itemView.findViewById(R.id.station_path)
        val date: TextView = itemView.findViewById(R.id.date)
        val distance: TextView = itemView.findViewById(R.id.distance)
    }
}