package com.microp.android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*


class Dashboard : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var dashboardView = inflater.inflate(R.layout.fragment_dashboard, container, false)

        setupControls(dashboardView)

        return dashboardView
    }

    private fun setupControls (dashboardView:View){

        dashboardView.addDeviceButton.setOnClickListener {
            Navigation.findNavController(dashboardView).navigate(R.id.action_dashboard_to_scanDeviceBarcode)
        }

    }
}
