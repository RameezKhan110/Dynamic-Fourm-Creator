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
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.example.multidropdown.databinding.ActivitySubmitFourmBinding
import com.example.multidropdown.databinding.ButtonLayoutBinding
import com.example.multidropdown.databinding.CheckboxLayoutBinding
import com.example.multidropdown.databinding.CircularImageLayoutBinding
import com.example.multidropdown.databinding.RadioGroupLayoutBinding
import com.example.multidropdown.databinding.RadioLayoutBinding
import com.example.multidropdown.databinding.TextInputLayoutBinding
import com.example.multidropdown.databinding.TextviewLayoutBinding
import com.example.multidropdown.model.BaseItem
import com.example.multidropdown.model.HintsModel
import com.example.multidropdown.model.ViewsData
import com.example.multidropdown.utils.ViewType
import com.google.android.material.textfield.TextInputLayout
import de.hdodenhof.circleimageview.CircleImageView

class SubmitForumActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubmitFourmBinding
    private val hintsList = mutableListOf<String>()
    private val imageViewList = mutableListOf<CircleImageView>()
    private var currentImageIndex: Int = 0
    private val data = mutableListOf<BaseItem>()
    private var image: String = ""

    @SuppressLint("SetTextI18n", "InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubmitFourmBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val items = intent.getSerializableExtra("itemsAndHints") as HintsModel

        Log.d("TAG", "items: $items")
        items.hints.forEach {
            hintsList.add(it)
        }

        var index = 0
        if (items.viewTypes.isEmpty().not()) {
            while (index < items.viewTypes.size) {
                val item = items.viewTypes[index]
                when (item) {
                    ViewType.TYPE_TEXTVIEW.key -> {

                        val textViewLayoutBinding = TextviewLayoutBinding.inflate(layoutInflater)
                        textViewLayoutBinding.textView.text = hintsList.first()
                        hintsList.remove(hintsList.first())
                        binding.dropDownContainerFinalActivity.addView(textViewLayoutBinding.root)
                        index++
                    }

                    ViewType.TYPE_BUTTON.key -> {

                        val buttonLayoutBinding = ButtonLayoutBinding.inflate(layoutInflater)
                        buttonLayoutBinding.button.text = hintsList.first()
                        hintsList.remove(hintsList.first())
                        binding.dropDownContainerFinalActivity.addView(buttonLayoutBinding.root)
                        buttonLayoutBinding.button.setOnClickListener {

                            binding.dropDownContainerFinalActivity.children.forEach {
                                if(it is ViewGroup) {
                                    if(it is RadioGroup) {
                                        val checkRadioGroupId = it.checkedRadioButtonId
                                        val radioButtonValue = findViewById<RadioButton>(checkRadioGroupId)
                                        data.add(BaseItem(ViewType.TYPE_RADIO, null, radioButtonValue.text.toString(), null, null, null))
                                    }
                                    it.children.forEach {
                                        if(it is TextInputLayout) {
                                            val text = it.editText?.text.toString()
                                            val hint = it.editText?.hint.toString()
                                            data.add(BaseItem(ViewType.TYPE_EDITTEXT, hint, text, null, null, null))

                                        } else if(it is CheckBox) {
                                            val checkboxText = it.text.toString()
                                            data.add(BaseItem(ViewType.TYPE_CHECKBOX, null, checkboxText, null, null, null))

                                        } else if(it is ImageView) {
                                            data.add(BaseItem(ViewType.TYPE_IMAGEVIEW, null, null, image, null, null))

                                        } else if(it is Button) {
                                            val buttonText = it.text.toString()
                                            data.add(BaseItem(ViewType.TYPE_BUTTON, null, buttonText, null, null, null))

                                        } else if(it is TextView) {
                                            val text = it.text.toString()
                                            data.add(BaseItem(ViewType.TYPE_TEXTVIEW, null, text, null, null, null))
                                        }
                                    }
                                }
                            }

                            val viewsData = ViewsData(data)
                            val intent = Intent(this, GeneratePDFActivity::class.java)
                            intent.putExtra("viewsData", viewsData)
                            startActivity(intent)
                            finish()
                            data.clear()
                        }
                        index ++
                    }

                    ViewType.TYPE_RADIO.key -> {

                        val radioGroupLayoutBinding =
                            RadioGroupLayoutBinding.inflate(layoutInflater)

                        for (i in index until items.viewTypes.size) {
                            if (items.viewTypes[i] == ViewType.TYPE_RADIO.key) {
                                val radioButtonLayoutBinding =
                                    RadioLayoutBinding.inflate(layoutInflater)
                                radioButtonLayoutBinding.radioButton.id = View.generateViewId()
                                radioButtonLayoutBinding.radioButton.text = hintsList.first()
                                hintsList.remove(hintsList.first())
                                radioGroupLayoutBinding.radioGroup.addView(radioButtonLayoutBinding.radioButton)
                                index++
                            } else {
                                index = i
                                break
                            }
                        }
                        binding.dropDownContainerFinalActivity.addView(radioGroupLayoutBinding.radioGroup)
                    }

                    ViewType.TYPE_CHECKBOX.key -> {

                        val checkBoxLayoutBinding = CheckboxLayoutBinding.inflate(layoutInflater)
                        checkBoxLayoutBinding.checkBox.text = hintsList.first()
                        hintsList.remove(hintsList.first())
                        binding.dropDownContainerFinalActivity.addView(checkBoxLayoutBinding.root)
                        index++
                    }

                    ViewType.TYPE_IMAGEVIEW.key -> {

                        val circularImageView = CircularImageLayoutBinding.inflate(layoutInflater)
                        imageViewList.add(circularImageView.circularImageView)
                        binding.dropDownContainerFinalActivity.addView(circularImageView.root.rootView)
                        circularImageView.circularImageView.setOnClickListener {
                            showImageSourceDialog()
                        }
                        index++
                    }

                    ViewType.TYPE_EDITTEXT.key -> {

                        val editTextLayoutBinding = TextInputLayoutBinding.inflate(layoutInflater)
                        editTextLayoutBinding.textInputLayout.hint = hintsList.first()
                        hintsList.remove(hintsList.first())
                        binding.dropDownContainerFinalActivity.addView(editTextLayoutBinding.root)
                        index++
                    }
                }
            }
        }
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
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
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
                Log.d("TAG", "camera image: $imageUri")

                if (currentImageIndex < imageViewList.size) {
                    imageViewList[currentImageIndex].setImageURI(imageUri)
                    image = imageUri.toString()
                    currentImageIndex++

                    if (currentImageIndex >= imageViewList.size) {
                        currentImageIndex = 0
                    }
                }
            }
        }

    private val startGalleryForResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { imageUri ->

                if (currentImageIndex < imageViewList.size) {
                    imageViewList[currentImageIndex].setImageURI(imageUri)
                    image = imageUri.toString()
                    currentImageIndex++

                    if (currentImageIndex >= imageViewList.size) {
                        currentImageIndex = 0
                    }
                }
            }
        }

    private fun requestPermissionForGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
                100
            )
        else
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                200
            )
    }

    private fun requestPermissionForCamera() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 300)
    }
}