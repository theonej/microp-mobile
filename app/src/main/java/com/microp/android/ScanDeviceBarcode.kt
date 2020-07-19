package com.microp.android

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.SparseArray
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.isNotEmpty
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.microp.android.viewModels.DeviceViewModel
import kotlinx.android.synthetic.main.fragment_scan_device_barcode.view.*

class ScanDeviceBarcode : Fragment() {

    private val REQUEST_CAMERA_PERMISSION = 1001
    private lateinit var m_Camera: CameraSource
    private lateinit var m_Detector: BarcodeDetector
    private lateinit var m_CurrentView:View;
    private val m_DeviceViewModel:DeviceViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        m_CurrentView =  inflater.inflate(R.layout.fragment_scan_device_barcode, container, false)

        var permissionGranted = ensurePermissions()

        setupControls()

        return m_CurrentView
    }

    private fun ensurePermissions():Boolean{

        var permissionGranted = ContextCompat.checkSelfPermission(this.requireContext().applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

        if(!permissionGranted) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        }

        return ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    }

    private fun setupControls(){
        m_Detector = BarcodeDetector.Builder(this.requireContext()).build()
        m_Camera = CameraSource.Builder(this.requireContext(), m_Detector)
            .setAutoFocusEnabled(true)
            .build()

        m_CurrentView.cameraView.holder.addCallback(  surfaceCallbackHandler)
        m_Detector.setProcessor(processor)

        m_CurrentView.add_device_button.setOnClickListener {view: View ->
            Navigation.findNavController((view)).navigate(R.id.action_scanDeviceBarcode_to_connectToDeviceServer)
        }
        println("controls set up successfully")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == REQUEST_CAMERA_PERMISSION && grantResults.isNotEmpty()){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                setupControls()
            }else{
                Toast.makeText(this.requireContext().applicationContext, "Please grant permissions for the app to use your camera in order to continue", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val surfaceCallbackHandler = object: SurfaceHolder.Callback{
        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

        }

        override fun surfaceDestroyed(holder: SurfaceHolder?) {
            m_Camera.stop()
        }

        override fun surfaceCreated(holder: SurfaceHolder?) {
            try {
                m_Camera.start(holder)
            }catch(ex:Exception){
                println(ex.message)
            }
        }
    }

    private val processor = object : Detector.Processor<Barcode>{
        override fun release() {

        }

        override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
            if(detections != null && detections.detectedItems.isNotEmpty()) {
                val qrCodes: SparseArray<Barcode> = detections.detectedItems

                val firstCode = qrCodes.valueAt(0)
                println(firstCode.displayValue)

                activity?.runOnUiThread {
                    m_Camera.stop()

                    m_DeviceViewModel.DeviceHMac = firstCode.displayValue

                    m_CurrentView.add_device_button.isVisible = true
                }
            }
        }

    }
}
