package com.inlacou.inkbetterandroidviews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.inlacou.inkbetterandroidviews.R

class SimpleRvAdapter(
    private val itemList: List<Row>,
    private val onClick: ((index: Row) -> Unit)? = null,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    override fun getItemViewType(position: Int): Int = 0
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
        = MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_simple, parent, false))
    
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MyViewHolder).let {
            it.tv.text = itemList[position].displayAsRow
            it.tv.setOnClickListener {
                onClick?.invoke(itemList[position])
            }
        }
    }
    
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv: TextView = itemView.findViewById(R.id.view)
    }
    
    override fun getItemCount(): Int = this.itemList.size

    interface Row {
        val displayAsRow: String
    }
}
