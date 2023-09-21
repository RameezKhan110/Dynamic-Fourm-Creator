package com.example.multidropdown

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.R
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.multidropdown.databinding.DropDownLayoutBinding
import com.example.multidropdown.model.MyItem
import com.example.multidropdown.utils.ApplicationClass

class ChooseViewAdapter :
    ListAdapter<MyItem, RecyclerView.ViewHolder>(DiffUtil()) {

    var onMinusCLicked: ((MyItem) -> Unit)? = null
    var onLongClickListener: (() -> Unit)? = null

    inner class ChooseViewsViewHolder(private val binding: DropDownLayoutBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyItem) {
            binding.apply {
                val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(ApplicationClass.application.baseContext, R.layout.select_dialog_item, item.spinnerItems)
                spinnerAdapter.setDropDownViewResource(R.layout.select_dialog_singlechoice)
                dropDownList.adapter = spinnerAdapter

                minusDropDown.text = item.minusBtn

                minusDropDown.setOnClickListener {
                    onMinusCLicked?.invoke(item)
                }
                binding.root.setOnLongClickListener {
                    onLongClickListener?.invoke()
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = DropDownLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChooseViewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = getItem(position)
        (holder as ChooseViewsViewHolder).bind(item)
    }

    fun onItemClickListener(listener: () -> Unit) {
        onLongClickListener = listener
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<MyItem>() {
        override fun areItemsTheSame(oldItem: MyItem, newItem: MyItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MyItem, newItem: MyItem): Boolean {
            return oldItem == newItem
        }
    }




}