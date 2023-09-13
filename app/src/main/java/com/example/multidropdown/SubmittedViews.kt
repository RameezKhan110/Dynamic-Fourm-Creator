package com.example.multidropdown

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.multidropdown.databinding.ActivitySubmittedViewsBinding
import com.google.android.material.textfield.TextInputEditText
import de.hdodenhof.circleimageview.CircleImageView

class SubmittedViews : AppCompatActivity() {

    private lateinit var binding: ActivitySubmittedViewsBinding
    private var editTextList = mutableListOf<String>()
    private var editTextViewList = mutableListOf<EditText>()
    private var radioTextViewList = mutableListOf<EditText>()
    private var radioTextList = mutableListOf<String>()
    private var checkboxViewList = mutableListOf<EditText>()
    private var checkBoxTextList = mutableListOf<String>()
    private var textViews = mutableListOf<EditText>()
    private var textViewList = mutableListOf<String>()
    private var buttonTextViewList = mutableListOf<EditText>()
    private var buttonTextList = mutableListOf<String>()
    private lateinit var imageView: CircleImageView
    private var imageList = mutableListOf<String>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubmittedViewsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val selectedItems = intent.getStringArrayListExtra("SelectedItems")

        binding.applyButton.setOnClickListener {

            editTextList.clear()
            buttonTextList.clear()
            radioTextList.clear()
            checkBoxTextList.clear()
            textViewList.clear()

            editTextViewList.forEach {
                editTextList.add(it.text.toString())
            }
            buttonTextViewList.forEach {
                buttonTextList.add(it.text.toString())
            }
            radioTextViewList.forEach {
                radioTextList.add(it.text.toString())
            }
            checkboxViewList.forEach {
                checkBoxTextList.add(it.text.toString())
            }
            textViews.forEach {
                textViewList.add(it.text.toString())
            }

            val intent = Intent(this, FinalActivity::class.java)
            intent.putStringArrayListExtra("selecteditems", selectedItems)
            intent.putStringArrayListExtra("imageView", imageList as ArrayList<String>)
            intent.putStringArrayListExtra("editText", editTextList as ArrayList<String>)
            intent.putStringArrayListExtra("radioText", radioTextList as ArrayList<String>)
            intent.putStringArrayListExtra("checkboxText", checkBoxTextList as ArrayList<String>)
            intent.putStringArrayListExtra("buttonText", buttonTextList as ArrayList<String>)
            intent.putStringArrayListExtra("textView", textViewList as ArrayList<String>)

            startActivity(intent)
        }

        if (!selectedItems.isNullOrEmpty()) {
            selectedItems.forEach { item ->
                if (item == "Edit Text") {
                    val textInputLayout =
                        LayoutInflater.from(this).inflate(R.layout.text_input_layout, null)
                    val editText = textInputLayout.findViewById<TextInputEditText>(R.id.editText)
                    editText.hint = "Write hint"
                    editText.id = View.generateViewId()
                    editTextViewList.add(editText as TextInputEditText)
                    textInputLayout.id = View.generateViewId()
                    textInputLayout.layoutParams = setUpLayoutParams()

                    val heading = TextView(this)
                    heading.text = "Edit text"
                    heading.textSize = 16F
                    heading.layoutParams = setHeadingLayoutParams()
                    binding.dropDownContainerSubmittedActivity.addView(heading)
                    binding.dropDownContainerSubmittedActivity.addView(textInputLayout)


                } else if (item == "Button") {

                    val textInputLayout =
                        LayoutInflater.from(this).inflate(R.layout.text_input_layout, null)
                    val buttonText = textInputLayout.findViewById<TextInputEditText>(R.id.editText)
                    buttonText.hint = "Button text"
                    buttonText.id = View.generateViewId()
                    buttonTextViewList.add(buttonText)
                    textInputLayout.id = View.generateViewId()
                    textInputLayout.layoutParams = setUpLayoutParams()

                    val buttonHeading = TextView(this)
                    buttonHeading.id = View.generateViewId()
                    buttonHeading.layoutParams = setHeadingLayoutParams()
                    buttonHeading.text = "Button"
                    buttonHeading.textSize = 16F

                    binding.dropDownContainerSubmittedActivity.addView(buttonHeading)
                    binding.dropDownContainerSubmittedActivity.addView(textInputLayout)

                } else if (item == "Check Box") {

                    val textInputLayout =
                        LayoutInflater.from(this).inflate(R.layout.text_input_layout, null)
                    val checkBoxText =
                        textInputLayout.findViewById<TextInputEditText>(R.id.editText)
                    checkBoxText.hint = "Checkbox text"
                    checkBoxText.id = View.generateViewId()
                    checkboxViewList.add(checkBoxText)
                    textInputLayout.id = View.generateViewId()
                    textInputLayout.layoutParams = setUpLayoutParams()

                    val checkBoxHeading = TextView(this)
                    checkBoxHeading.id = View.generateViewId()
                    checkBoxHeading.layoutParams = setHeadingLayoutParams()
                    checkBoxHeading.text = "CheckBox"
                    checkBoxHeading.textSize = 16F

                    binding.dropDownContainerSubmittedActivity.addView(checkBoxHeading)
                    binding.dropDownContainerSubmittedActivity.addView(textInputLayout)

                } else if (item == "Radio") {

                    val textInputLayout =
                        LayoutInflater.from(this).inflate(R.layout.text_input_layout, null)
                    val radioText = textInputLayout.findViewById<TextInputEditText>(R.id.editText)
                    radioText.hint = "Radio button text"
                    radioText.id = View.generateViewId()
                    radioTextViewList.add(radioText)
                    textInputLayout.id = View.generateViewId()
                    textInputLayout.layoutParams = setUpLayoutParams()

                    val radioHeading = TextView(this)
                    radioHeading.id = View.generateViewId()
                    radioHeading.layoutParams = setHeadingLayoutParams()
                    radioHeading.text = "Radio button"
                    radioHeading.textSize = 16F

                    binding.dropDownContainerSubmittedActivity.addView(radioHeading)
                    binding.dropDownContainerSubmittedActivity.addView(textInputLayout)

                } else if (item == "Image") {

                    imageView = CircleImageView(this)
                    imageView.id = View.generateViewId()
                    val heading = TextView(this)
                    heading.layoutParams = setHeadingLayoutParams()
                    heading.text = "Image"
                    heading.textSize = 16F
                    binding.dropDownContainerSubmittedActivity.addView(heading)

                    val layoutParams = LinearLayout.LayoutParams(200, 200)
                    layoutParams.setMargins(
                        0,
                        0,
                        0,
                        resources.getDimensionPixelSize(R.dimen.bottom_margin)
                    )
                    imageView.layoutParams = layoutParams
                    imageView.borderColor = resources.getColor(R.color.black)
                    imageView.borderWidth = 1
                    imageView.setImageResource(R.drawable.baseline_person_24)
                    binding.dropDownContainerSubmittedActivity.addView(imageView)

                    imageView.setOnClickListener {
                        showImageSourceDialog()
                    }

                } else {

                    val textInputLayout =
                        LayoutInflater.from(this).inflate(R.layout.text_input_layout, null)
                    val textView = textInputLayout.findViewById<TextInputEditText>(R.id.editText)
                    textView.hint = "Write Something"
                    textView.id = View.generateViewId()
                    textViews.add(textView)
                    textInputLayout.id = View.generateViewId()
                    textInputLayout.layoutParams = setUpLayoutParams()

                    val heading = TextView(this)
                    heading.id = View.generateViewId()
                    heading.textSize = 16F
                    heading.layoutParams = setHeadingLayoutParams()
                    heading.text = "Textview"

                    binding.dropDownContainerSubmittedActivity.addView(heading)
                    binding.dropDownContainerSubmittedActivity.addView(textInputLayout)
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

    private fun setHeadingLayoutParams(): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    private var imageUri: Uri? = null

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startCameraForResult.launch(cameraIntent)
    }

    private fun openGallery() {
        startGalleryForResult.launch("image/*")

    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Camera", "Gallery")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose Image Source")
        builder.setItems(options) { dialog: DialogInterface, which: Int ->
            when (which) {
                0 -> if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    requestPermissionForCamera()
                }

                1 -> if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.READ_MEDIA_IMAGES
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    openGallery()
                } else {
                    requestPermissionForGallery()
                }
            }
        }
        builder.show()
    }


    private val startCameraForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("TAG", "camera image: " + imageUri)
                imageView.setImageURI(imageUri)
                imageList.add(imageUri.toString())
            }
        }

    private val startGalleryForResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { imageUri ->
                imageView.setImageURI(imageUri)
                imageList.add(imageUri.toString())
            }
        }

    private fun requestPermissionForGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES), 100)
        else
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 200)
    }

    private fun requestPermissionForCamera() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 300)
    }
}