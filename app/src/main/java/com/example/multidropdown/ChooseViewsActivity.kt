package com.example.multidropdown

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.END
import androidx.recyclerview.widget.ItemTouchHelper.START
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multidropdown.databinding.ActivityChooseViewsBinding
import com.example.multidropdown.databinding.DropDownLayoutBinding
import com.example.multidropdown.model.MyItem
import com.example.multidropdown.utils.ViewType
import java.util.Collections

class ChooseViewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseViewsBinding
    private val viewsList = mutableListOf<String>()
    private val viewsListOfDropDown = mutableListOf<String>()
    private val chooseViewAdapter = ChooseViewAdapter()
    private val layoutItemsList = mutableListOf<MyItem>()
    private var itemId: Int = 0

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseViewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        itemTouchHelper.attachToRecyclerView(binding.dropDownContainerRV)

        ViewType.values().map {
            viewsList.add(it.key)
        }

        binding.dropDownContainerRV.layoutManager = LinearLayoutManager(this)
        binding.dropDownContainerRV.adapter = chooseViewAdapter

        binding.Add.setOnClickListener {

            itemId++
            layoutItemsList.add(MyItem(itemId, viewsList, "-"))
            chooseViewAdapter.submitList(layoutItemsList)
            chooseViewAdapter.notifyDataSetChanged()
        }

        chooseViewAdapter.onMinusCLicked = { item ->

            Log.d("TAG", "position: $item")
            val deletePos = layoutItemsList.indexOfFirst {
                it.itemId == item.itemId
            }
            if(binding.dropDownContainerRV.childCount > 1) {
                layoutItemsList.removeAt(deletePos)
                chooseViewAdapter.notifyItemRemoved(deletePos)
            } else {
                Toast.makeText(this, "This view can't be removed",Toast.LENGTH_SHORT).show()
            }

        }

        binding.choose.setOnClickListener {

            binding.dropDownContainerRV.children.forEach {
                if (it is ViewGroup) {
                    it.children.forEach { spinnerItem ->
                        if (spinnerItem is Spinner) {
                            val selectedItem = spinnerItem.selectedItem.toString()
                            viewsListOfDropDown.add(selectedItem)
                        }
                    }
                }
            }

            val intent = Intent(this, CreateForumActivity::class.java)

            intent.putStringArrayListExtra(
                "SelectedItems",
                viewsListOfDropDown as ArrayList<String>
            )
            startActivity(intent)
            viewsListOfDropDown.clear()
        }

    }

    private val itemTouchHelper by lazy {
        val itemTouchCallBack =
            object : ItemTouchHelper.SimpleCallback(UP or DOWN or START or END, 0) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val recyclerViewAdapter = recyclerView.adapter as ChooseViewAdapter
                    val from = viewHolder.adapterPosition
                    val to = target.adapterPosition
                    Collections.swap(layoutItemsList, from, to)
                    recyclerViewAdapter.notifyItemMoved(from, to)
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                }

                override fun onSelectedChanged(
                    viewHolder: RecyclerView.ViewHolder?,
                    actionState: Int
                ) {
                    super.onSelectedChanged(viewHolder, actionState)
                    if (actionState == ACTION_STATE_DRAG) {
                        viewHolder?.itemView?.scaleY = 1.3f
                        viewHolder?.itemView?.alpha = 0.7f
                    }
                }

                override fun clearView(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ) {
                    super.clearView(recyclerView, viewHolder)
                    viewHolder.itemView.scaleY = 1.0f
                    viewHolder.itemView.alpha = 1.0f
                }
            }
        ItemTouchHelper(itemTouchCallBack)
    }
}