package com.example.multidropdown

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.example.multidropdown.databinding.ActivityChooseViewsBinding
import com.example.multidropdown.databinding.DropDownLayoutBinding
import com.example.multidropdown.utils.ViewType

class ChooseViewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseViewsBinding
    private val viewsList = mutableListOf<String>()
    private val viewsListOfDropDown = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseViewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewType.values().map {
            viewsList.add(it.key)
        }

        binding.Add.setOnClickListener {

            val dropDownLayout = DropDownLayoutBinding.inflate(layoutInflater)
            dropDownLayout.dropDownList.id = View.generateViewId()

            val position = binding.dropDownContainer.childCount - 1

            binding.dropDownContainer.addView(dropDownLayout.root, position)

            val spinnerAdapter: ArrayAdapter<String> =
                ArrayAdapter(this, android.R.layout.select_dialog_item, viewsList)
            spinnerAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
            dropDownLayout.dropDownList.adapter = spinnerAdapter

            dropDownLayout.minusDropDown.setOnClickListener { view ->
                if (binding.dropDownContainer.childCount > 2) {
                    binding.dropDownContainer.removeView(dropDownLayout.root)
                } else {
                    Toast.makeText(this, "This view can't be removed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.choose.setOnClickListener {

            binding.dropDownContainer.children.forEach {
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

}