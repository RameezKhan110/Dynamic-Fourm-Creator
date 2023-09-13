package com.example.multidropdown

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.multidropdown.databinding.DropDownItemBinding

class SpinnerAdapter(val context: Context, val viewsList: List<String>): BaseAdapter() {
    override fun getCount(): Int {
        return viewsList.size
    }

    override fun getItem(p0: Int): Any {
        return viewsList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, p1: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.drop_down_item, parent, false)

        val viewsItems = viewsList[position]

        val viewTv = view.findViewById<TextView>(R.id.textView)

        viewTv.text = viewsItems
        return view
    }
}