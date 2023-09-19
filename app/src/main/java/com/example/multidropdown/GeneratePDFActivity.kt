package com.example.multidropdown

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.multidropdown.databinding.ActivityGeneratePdfBinding
import com.example.multidropdown.databinding.CircularImageLayoutBinding
import com.example.multidropdown.databinding.HeadingTextLayoutBinding
import com.example.multidropdown.databinding.TextviewLayoutBinding
import com.example.multidropdown.model.ViewsData
import com.example.multidropdown.utils.ViewType
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class GeneratePDFActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGeneratePdfBinding
    private var index: Int = 0
    private var radioHeading: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeneratePdfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDownloadPDF.setOnClickListener {

            val linearLayout: LinearLayout = binding.scrollViewGeneratepdf.getChildAt(0) as LinearLayout

            val pdf = PdfDocument()
            val pageInfo =
                PdfDocument.PageInfo.Builder(linearLayout.width, linearLayout.height, 1).create()
            val page = pdf.startPage(pageInfo)

            val canvas = page.canvas
            val bitmap = Bitmap.createBitmap(
                linearLayout.width,
                linearLayout.height,
                Bitmap.Config.ARGB_8888
            )
            val bitmapCanvas = Canvas(bitmap)
            linearLayout.draw(bitmapCanvas)
            canvas.drawBitmap(bitmap, 0f, 0f, null)

            pdf.finishPage(page)

            try {

                val pdfFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "my_fourm.pdf")

                val outputStream = FileOutputStream(pdfFile)
                pdf.writeTo(outputStream)
                outputStream.close()
                pdf.close()

                Toast.makeText(this, "PDF generated and send it to email", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {

                e.printStackTrace()
                Toast.makeText(this, "Error creating or sending PDF", Toast.LENGTH_SHORT).show()
            }
        }
        val viewsData = intent.getSerializableExtra("viewsData") as ViewsData
        while(index < viewsData.viewsData.size) {
            val item = viewsData.viewsData[index]
            when(item.type) {
                ViewType.TYPE_TEXTVIEW -> {

                    if(viewsData.viewsData[index + 1].type == ViewType.TYPE_RADIO) {
                        radioHeading = item.text.toString()
                    } else {
                        val textView = TextviewLayoutBinding.inflate(layoutInflater)
                        textView.textView.text = item.text
                        binding.viewsDataContainer.addView(textView.root)
                    }
                    index ++

                }
                ViewType.TYPE_EDITTEXT -> {
                    val editText = HeadingTextLayoutBinding.inflate(layoutInflater)
                    editText.heading.text = item.heading
                    editText.text.text = item.text
                    binding.viewsDataContainer.addView(editText.root)
                    index ++

                }
                ViewType.TYPE_IMAGEVIEW -> {
                    val image = CircularImageLayoutBinding.inflate(layoutInflater)
                    image.circularImageView.setImageURI(item.imageRes?.toUri())
                    binding.viewsDataContainer.addView(image.root)

                    index ++

                }
                ViewType.TYPE_CHECKBOX -> {

                    val checkBoxText = TextviewLayoutBinding.inflate(layoutInflater)
                    checkBoxText.textView.text = item.text
                    binding.viewsDataContainer.addView(checkBoxText.root)
                    index ++

                }
                ViewType.TYPE_BUTTON -> {

                    index ++

                }
                ViewType.TYPE_RADIO -> {
                    val radioText = HeadingTextLayoutBinding.inflate(layoutInflater)
                    radioText.heading.text = radioHeading
                    radioText.text.text = item.text
                    binding.viewsDataContainer.addView(radioText.root)
                    index ++

                }
            }
        }
        Log.d("TAG", "viewsData$viewsData")


    }
}