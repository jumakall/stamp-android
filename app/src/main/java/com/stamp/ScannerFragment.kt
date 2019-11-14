package com.stamp


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.stamp.databinding.FragmentScannerBinding
import com.stamp.viewModel.StampsViewModel
import io.sentry.Sentry

private const val CAMERA_PERMISSION_REQUEST = 1011

class ScannerFragment : Fragment() {

    private lateinit var binding: FragmentScannerBinding
    private lateinit var stamps: StampsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scanner, container, false)

        stamps = activity?.run {
            ViewModelProviders.of(this)[StampsViewModel::class.java]
        } ?: throw Exception("Invalid activity.")

        stamps.newStamp.value?.eventCodeSelected?.observe(this, Observer {
            if (it == null)
                return@Observer

            this@ScannerFragment.findNavController()
                .navigate(ScannerFragmentDirections.actionScannerFragmentToVendorCommitFragment())
            stamps.newStamp.value?.eventCodeSelectedComplete()
        })

        binding.grantButton.setOnClickListener {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.surfaceScanner.visibility = View.INVISIBLE
        binding.cameraIcon.visibility = View.VISIBLE
        binding.cameraPermissionRequiredNotice.visibility = View.VISIBLE
        binding.grantButton.visibility = View.VISIBLE
        if (ActivityCompat.checkSelfPermission(activity!!.applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            init()
        }
    }

    private fun init() {
        binding.surfaceScanner.visibility = View.VISIBLE
        binding.cameraIcon.visibility = View.GONE
        binding.cameraPermissionRequiredNotice.visibility = View.GONE
        binding.grantButton.visibility = View.GONE

        val surface = binding.surfaceScanner
        val detector = BarcodeDetector.Builder(this.context)
            .setBarcodeFormats(Barcode.QR_CODE)
            .build()
        val camera = CameraSource.Builder(this.context, detector)
            .setRequestedPreviewSize(1024, 768)
            .setAutoFocusEnabled(true)
            .build()

        surface.holder.addCallback(object : SurfaceHolder.Callback {

            @SuppressLint("MissingPermission")
            override fun surfaceCreated(p0: SurfaceHolder?) {
                try {
                    camera.start(surface.holder)
                }  catch (e: Exception) {
                    Sentry.capture(e)
                }

            }

            override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {}

            override fun surfaceDestroyed(p0: SurfaceHolder?) {
                camera.stop()
            }

        })

        detector.setProcessor(object: Detector.Processor<Barcode> {
            override fun release() { }

            override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
                val barCodes: SparseArray<Barcode> = detections?.detectedItems ?: SparseArray()

                if (barCodes.size() > 0) {
                    detector.release()

                    stamps.newStamp.value?.postCode(
                        barCodes.valueAt(0).displayValue
                    )
                }
            }

        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CAMERA_PERMISSION_REQUEST -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init()
                }

            }
        }
    }
}
