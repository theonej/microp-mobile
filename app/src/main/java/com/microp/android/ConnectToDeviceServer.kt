package com.microp.android

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.microp.android.viewModels.DeviceViewModel
import kotlinx.android.synthetic.main.fragment_connect_to_device_server.view.*
import kotlinx.android.synthetic.main.fragment_scan_device_barcode.view.*
import java.util.*

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

        connectToBluetoothServer()

        return connectToDeviceView
    }


    private fun connectToBluetoothServer(){
        try {
            println("connecting to bluetooth service")
            val adapter = BluetoothAdapter.getDefaultAdapter()
            var device = adapter.getRemoteDevice(m_DeviceViewModel.DeviceHMac)

            var serviceUUid: UUID = UUID.fromString("80000000-0000-0000-0000-000000000000")
            var socket = device.createRfcommSocketToServiceRecord(serviceUUid)

            println("opening socket")
            adapter.cancelDiscovery()
            socket.connect()

            println("sending data to outputStream")

            var message = "This is a test Message"
            var messageBytes = message.toByteArray()

            socket.outputStream.write(messageBytes)

            socket.outputStream.close()

            socket.close()
        }catch(ex:Exception){
            Toast.makeText(this.context, ex.message, Toast.LENGTH_LONG)
        }
    }
}
