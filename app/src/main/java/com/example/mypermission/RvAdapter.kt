package com.example.mypermission

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item.view.*


class RvAdapter (var list: List<Contact>) : RecyclerView.Adapter<RvAdapter.VP_Vh>() {

    inner class VP_Vh(var itemView:View):
        RecyclerView.ViewHolder(itemView) {
        fun onBind(contact: Contact) {
            itemView.txt_name.text = contact.name
            itemView.txt_number.text = contact.phone


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VP_Vh {
        return VP_Vh(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false))


    }

    override fun onBindViewHolder(holder: VP_Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size



}