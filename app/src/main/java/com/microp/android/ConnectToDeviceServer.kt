package com.microp.android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.microp.android.viewModels.DeviceViewModel
import kotlinx.android.synthetic.main.fragment_connect_to_device_server.view.*
import kotlinx.android.synthetic.main.fragment_scan_device_barcode.view.*

class ConnectToDeviceServer : Fragment() {

    private val m_DeviceViewModel: DeviceViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var connectToDeviceView =  inflater.inflate(R.layout.fragment_connect_to_device_server, container, false)

        connectToDeviceView.connectingToDeviceTextView.text = "connecting to device ${m_DeviceViewModel.DeviceHMac.toString()}"

        return connectToDeviceView
    }


}
