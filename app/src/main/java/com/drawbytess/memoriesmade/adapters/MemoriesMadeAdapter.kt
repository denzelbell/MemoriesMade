package com.drawbytess.memoriesmade.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.drawbytess.memoriesmade.R
import com.drawbytess.memoriesmade.models.MemoriesModel
import kotlinx.android.synthetic.main.item_memories.view.*

open class MemoriesMadeAdapter (
    private val context: Context,
    private var list: ArrayList<MemoriesModel>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_memories,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            holder.itemView.iv_place_image.setImageURI(Uri.parse(model.image))
            holder.itemView.tvTitle.text = model.title
            holder.itemView.tvDescription.text = model.description
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view:View) : RecyclerView.ViewHolder(view)
}