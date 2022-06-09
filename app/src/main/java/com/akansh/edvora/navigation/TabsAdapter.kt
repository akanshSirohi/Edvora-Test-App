package com.akansh.edvora.navigation

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.akansh.edvora.R

class TabsAdapter(private val mList: List<TabsViewModel>) : RecyclerView.Adapter<TabsAdapter.ViewHolder>() {

    var listener: TabClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tab, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mList[position]
        holder.title.setText(data.title)
        if(data.isActive) {
            holder.underline.isVisible = true
            holder.title.typeface = Typeface.DEFAULT_BOLD
        }else{
            holder.underline.isVisible = false
            holder.title.typeface = Typeface.DEFAULT
        }
        holder.root.setOnClickListener {
            if (listener != null) {
                listener?.onTabClickListener(position)
                activateItem(position)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun activateItem(pos: Int) {
        for (i in 0..mList.size-1) {
            if(i==pos) {
                mList[i].isActive = true
            }else{
                mList[i].isActive = false
            }
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setTitle(pos: Int, title: String) {
        mList[pos].title = title
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun initOnClickInterface(listener: TabClickListener){
        this.listener = listener
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val underline: View = itemView.findViewById(R.id.underline)
        val root: ConstraintLayout = itemView.findViewById(R.id.tab_root)
    }

    interface TabClickListener {
        fun onTabClickListener(pos: Int)
    }
}