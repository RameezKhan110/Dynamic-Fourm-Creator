package com.example.multidropdown

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.children
import com.example.multidropdown.databinding.ActivityMainBinding
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewsList = mutableListOf<String>()
    private val viewsListOfDropDown = mutableListOf<String>()
    private lateinit var dropDown: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewsList.add("Edit Text")
        viewsList.add("Button")
        viewsList.add("Check Box")
        viewsList.add("Radio")
        viewsList.add("Image")
        viewsList.add("Text View")

        binding.Add.setOnClickListener {
            val dropDownLayout = LayoutInflater.from(this).inflate(R.layout.drop_down_layout, null)
            dropDownLayout.id = View.generateViewId()
            Log.d("TAG", "dropDownLayout : " + dropDownLayout.id)

            dropDown = dropDownLayout.findViewById(R.id.dropDownList)
            dropDown.id = View.generateViewId()
            Log.d("TAG", "dropDown id: " + dropDown.id)
            val minusDropDown = dropDownLayout.findViewById<TextView>(R.id.minusDropDown)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            dropDownLayout.layoutParams = params
            binding.dropDownContainer.addView(dropDownLayout)

            val spinnerAdapter: ArrayAdapter<String> =
                ArrayAdapter(this, android.R.layout.select_dialog_item, viewsList)
            spinnerAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
            dropDown.adapter = spinnerAdapter

            minusDropDown.setOnClickListener { view ->
                binding.dropDownContainer.removeView(dropDownLayout)
            }
        }

        binding.Submit.setOnClickListener {

            binding.dropDownContainer.children.forEach {
                if(it is ViewGroup){
                    it.children.forEach {spinnerItem ->
                        if(spinnerItem is Spinner){
                            val selectedItem = spinnerItem.selectedItem.toString()
                            viewsListOfDropDown.add(selectedItem)
                        }
                    }
                }
            }

            Log.d("TAG", "selectedItems: $viewsListOfDropDown")

            val intent = Intent(this, SubmittedViews::class.java)

            intent.putStringArrayListExtra(
                "SelectedItems",
                viewsListOfDropDown as ArrayList<String>
            )
            startActivity(intent)
            viewsListOfDropDown.clear()
        }

    }

}