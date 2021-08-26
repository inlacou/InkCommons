package com.inlacou.inkbetterandroidviews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inlacou.inkandroidextensions.view.setDrawableRes
import com.inlacou.inkbetterandroidviews.R

class SimpleCheckboxRvAdapter(
    private val itemList: MutableList<out Row>,
    private val onClick: ((selectedItems: List<Row>) -> Unit)? = null,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    fun setSelected(item: Row) {
        selectedItems.add(item)
    }
    
    private val selectedItems = mutableListOf<Row>()
    
    override fun getItemViewType(position: Int): Int = 0
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_simple_selectable, parent, false))
    }
    
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MyViewHolder).let { viewHolder ->
            viewHolder.tv.text = itemList[position].displayAsRow
            viewHolder.cb.setImageResource(if(selectedItems.contains(itemList[position])) R.drawable.ic_check_box_checked else R.drawable.ic_check_box_unchecked)
            viewHolder.base.setOnClickListener {
                if(selectedItems.contains(itemList[position])) {
                    selectedItems.remove(itemList[position])
                    viewHolder.cb.setDrawableRes(R.drawable.ic_check_box_unchecked)
                } else {
                    selectedItems.add(itemList[position])
                    viewHolder.cb.setDrawableRes(R.drawable.ic_check_box_checked)
                }
                onClick?.invoke(selectedItems)
            }
        }
    }
    
    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val base: View = itemView.findViewById(R.id.view)
        val tv: TextView = itemView.findViewById(R.id.tv)
        val cb: ImageView = itemView.findViewById(R.id.iv_cb)
    }
    
    override fun getItemCount(): Int {
        return this.itemList.size
    }
    
    interface Row {
        val displayAsRow: String
    }
}
