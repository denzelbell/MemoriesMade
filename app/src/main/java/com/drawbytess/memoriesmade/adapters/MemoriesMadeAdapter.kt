package com.drawbytess.memoriesmade.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.drawbytess.memoriesmade.R
import com.drawbytess.memoriesmade.activities.AddLocationActivity
import com.drawbytess.memoriesmade.activities.MainActivity
import com.drawbytess.memoriesmade.models.MemoriesModel
import kotlinx.android.synthetic.main.activity_add_location.view.*
import kotlinx.android.synthetic.main.activity_memories_detail.view.*
import kotlinx.android.synthetic.main.item_memories.view.*
import kotlinx.android.synthetic.main.item_memories.view.iv_place_image

open class MemoriesMadeAdapter (
    private val context: Context,
    private var list: ArrayList<MemoriesModel>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

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

            holder.itemView.setOnClickListener {
                if (onClickListener != null){
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }
    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int){
        val intent = Intent(context, AddLocationActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, list[position])
        activity.startActivityForResult(intent, requestCode)
        notifyItemChanged(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: MemoriesModel)
    }

    private class MyViewHolder(view:View) : RecyclerView.ViewHolder(view)
}