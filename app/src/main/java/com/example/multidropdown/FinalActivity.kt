package com.example.multidropdown

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Audio.Radio
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.net.toUri
import com.example.multidropdown.databinding.ActivityFinalBinding
import com.google.android.material.textfield.TextInputEditText
import de.hdodenhof.circleimageview.CircleImageView

class FinalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFinalBinding
    var radioGroup: RadioGroup? = null
    var currentViewType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedItems = intent.getStringArrayListExtra("selecteditems")
        val editTextValue = intent.getStringArrayListExtra("editText")
        val radioTextValue = intent.getStringArrayListExtra("radioText")
        val checkboxTextValue = intent.getStringArrayListExtra("checkboxText")
        val textViewValue = intent.getStringArrayListExtra("textView")
        val buttonTextValue = intent.getStringArrayListExtra("buttonText")
        val imageViewValue = intent.getStringArrayListExtra("imageView")

//        Log.d("TAG", "text: $editText")
//        Log.d("TAG", "text: $radioText")
//        Log.d("TAG", "text: $checkboxText")
//        Log.d("TAG", "text: $textView")
//        Log.d("TAG", "text: $buttonText")
        Log.d("TAG", "select items:" + selectedItems)

        if (!selectedItems.isNullOrEmpty()) {
            selectedItems.forEach { item ->
                if (item == "Edit Text") {
                    val textInputLayout =
                        LayoutInflater.from(this).inflate(R.layout.text_input_layout, null)
                    val editText = textInputLayout.findViewById<TextInputEditText>(R.id.editText)
                    editText.hint = editTextValue?.first()
                    editTextValue?.remove(editTextValue.first())
                    editText.id = View.generateViewId()
                    textInputLayout.id = View.generateViewId()
                    textInputLayout.layoutParams = setUpLayoutParams()
                    binding.dropDownContainerFinalActivity.addView(textInputLayout)

                    currentViewType = "Edit Text"

                } else if (item == "Button") {

                    val button = Button(this)
                    button.id = View.generateViewId()
                    button.text = buttonTextValue?.first().toString()
                    buttonTextValue?.remove(buttonTextValue.first())
                    button.layoutParams = setUpLayoutParams()
                    binding.dropDownContainerFinalActivity.addView(button)

                    currentViewType = "Button"


                } else if (item == "Text View") {

                    val textView = TextView(this)
                    textView.id = View.generateViewId()
                    textView.text = textViewValue?.first()
                    textViewValue?.remove(textViewValue.first())
                    textView.textSize = 20F
                    textView.layoutParams = setUpLayoutParams()
                    binding.dropDownContainerFinalActivity.addView(textView)

                    currentViewType = "Text View"


                } else if (item == "Check Box") {

                    val checkBox = CheckBox(this)
                    checkBox.textSize = 16F
                    checkBox.text = checkboxTextValue?.first()
                    checkboxTextValue?.remove(checkboxTextValue.first())
                    checkBox.layoutParams = setUpLayoutParams()
                    binding.dropDownContainerFinalActivity.addView(checkBox)

                    currentViewType = "Check Box"


                } else if (item == "Image") {

                    val imageView = CircleImageView(this)
                    val layoutParams = LinearLayout.LayoutParams(200, 200)
                    layoutParams.setMargins(
                        0,
                        0,
                        0,
                        resources.getDimensionPixelSize(R.dimen.bottom_margin)
                    )
                    imageView.layoutParams = layoutParams
                    imageView.id = View.generateViewId()
                    imageView.setImageURI(imageViewValue?.first()?.toUri())
                    imageViewValue?.remove(imageViewValue.first())
                    binding.dropDownContainerFinalActivity.addView(imageView)

                } else {

                    if (currentViewType != "Radio") {
                        radioGroup = RadioGroup(this)
                        radioGroup!!.layoutParams = setUpLayoutParams()
                        radioGroup!!.orientation = RadioGroup.VERTICAL
                        binding.dropDownContainerFinalActivity.addView(radioGroup)
                    }

                    val radioButton = RadioButton(this)
                    radioButton.id = View.generateViewId()
                    radioButton.layoutParams = RadioGroup.LayoutParams(
                        RadioGroup.LayoutParams.MATCH_PARENT,
                        RadioGroup.LayoutParams.WRAP_CONTENT
                    )
                    radioButton.text = radioTextValue?.first()
                    radioTextValue?.remove(radioTextValue.first())
                    radioButton.textSize = 16F
                    radioGroup?.addView(radioButton)

                    currentViewType = "Radio"
                }
            }
        }
    }

    private fun setUpLayoutParams(): LinearLayout.LayoutParams {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
        )

        layoutParams.setMargins(
            0, 0, 0,
            resources.getDimensionPixelSize(R.dimen.bottom_margin)
        )

        return layoutParams
    }
}