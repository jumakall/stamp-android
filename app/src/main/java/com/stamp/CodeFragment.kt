package com.stamp


import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.stamp.databinding.FragmentCodeBinding
import kotlinx.android.synthetic.main.fragment_code.*
import java.util.*


private const val ARG_CODE = "code"

// todo: needs some work, generate right size code for device
class CodeFragment : Fragment() {

    private lateinit var binding: FragmentCodeBinding

    private var code: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            code = it.getString(ARG_CODE)
        }

        Thread {
            val codeBitmap = generateQRCode(code!!, 1024)

            progressBar?.post {
                progressBar.visibility = View.GONE
            }

            imageQRCode?.post {
                imageQRCode.setImageBitmap(codeBitmap)
            }
        }.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_code, container, false)
        return binding.root
    }

    private fun generateQRCode(data: String, size: Int): Bitmap {
        val writer = MultiFormatWriter()
        val encodedData = Uri.encode(data)

        // configure encoder
        val hints = EnumMap<EncodeHintType, Int>(EncodeHintType::class.java)
        hints[EncodeHintType.MARGIN] = 0

        val bm = writer.encode(encodedData, BarcodeFormat.QR_CODE, size, size, hints)
        val image = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

        for (i: Int in 0 until size)
            for (j: Int in 0 until size)
                image.setPixel(i, j, if (bm.get(i, j)) Color.BLACK else Color.TRANSPARENT)

        return image
    }


    companion object {
        @JvmStatic
        fun newInstance(code: String?) =
            CodeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CODE, code)
                }
            }
    }


}
