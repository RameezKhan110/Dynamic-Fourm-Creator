package com.example.multidropdown

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.example.multidropdown.databinding.ActivityCreateFourmBinding
import com.example.multidropdown.databinding.CircularImageLayoutBinding
import com.example.multidropdown.databinding.TextInputLayoutBinding
import com.example.multidropdown.model.HintsModel
import com.example.multidropdown.utils.ViewType
import com.google.android.material.textfield.TextInputLayout

class CreateForumActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateFourmBinding
    private var hintsList = mutableListOf<String>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateFourmBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val selectedItems = intent.getStringArrayListExtra("SelectedItems")

        binding.applyButton.setOnClickListener {

            binding.dropDownContainerSubmittedActivity.children.forEach {
                if (it is ViewGroup) {
                    it.children.forEach {
                        if (it is TextInputLayout) {
                            hintsList.add(it.editText?.text.toString())
                        }
                    }
                }
            }
            val items = HintsModel(selectedItems ?: arrayListOf(), hintsList)
            Log.d("TAG", "testing list: $items")

            val intent = Intent(this, SubmitForumActivity::class.java)
            intent.putExtra("itemsAndHints", items)
            startActivity(intent)
            hintsList.clear()
        }

        if (selectedItems.isNullOrEmpty().not()) {
            selectedItems?.forEach { item ->
                when (item) {
                    ViewType.TYPE_TEXTVIEW.key -> {

                        viewTypesHeading(ViewType.TYPE_TEXTVIEW.key)
                        setUpTextInputLayout()
                    }

                    ViewType.TYPE_BUTTON.key -> {

                        viewTypesHeading(ViewType.TYPE_BUTTON.key)
                        setUpTextInputLayout()
                    }

                    ViewType.TYPE_RADIO.key -> {

                        viewTypesHeading(ViewType.TYPE_RADIO.key)
                        setUpTextInputLayout()
                    }

                    ViewType.TYPE_CHECKBOX.key -> {

                        viewTypesHeading(ViewType.TYPE_CHECKBOX.key)
                        setUpTextInputLayout()
                    }

                    ViewType.TYPE_IMAGEVIEW.key -> {

                        viewTypesHeading(ViewType.TYPE_IMAGEVIEW.key)
                        circularImageView()
                    }

                    ViewType.TYPE_EDITTEXT.key -> {

                        viewTypesHeading(ViewType.TYPE_EDITTEXT.key)
                        setUpTextInputLayout()
                    }
                }
            }

        }
    }

    private fun setHeadingLayoutParams(): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setUpTextInputLayout() {

        val textInputLayoutBinding = TextInputLayoutBinding.inflate(layoutInflater)
        textInputLayoutBinding.textInputLayout.hint = "Give text"
        binding.dropDownContainerSubmittedActivity.addView(textInputLayoutBinding.root)

    }

    private fun viewTypesHeading(viewType: String) {

        val viewTypeHeading = TextView(this)
        viewTypeHeading.text = viewType
        viewTypeHeading.textSize = 16F
        viewTypeHeading.layoutParams = setHeadingLayoutParams()
        binding.dropDownContainerSubmittedActivity.addView(viewTypeHeading)
    }

    private fun circularImageView() {

        val circularImageViewBinding = CircularImageLayoutBinding.inflate(layoutInflater)
        binding.dropDownContainerSubmittedActivity.addView(circularImageViewBinding.root)

    }

}