package com.stamp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.stamp.viewModel.PassListViewMode
import com.stamp.viewModel.PassesViewModel
import com.stamp.viewModel.StampsViewModel

class VendorPassesFragment : Fragment() {

    private lateinit var passes: PassesViewModel
    private lateinit var stamps: StampsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        stamps = activity?.run {
            ViewModelProviders.of(this)[StampsViewModel::class.java]
        } ?: throw Exception("Invalid activity.")

        passes = activity?.run {
            ViewModelProviders.of(this)[PassesViewModel::class.java]
        } ?: throw Exception("Invalid activity.")

        passes.setMode(PassListViewMode.VENDOR)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        stamps.newStamp()
        val view = inflater.inflate(R.layout.fragment_vendor_passes, container, false)

        // navigate when pass is selected
        stamps.newStamp.value?.eventPassSelected?.observe(this, Observer {
            if (it == null)
                return@Observer

            this@VendorPassesFragment.findNavController()
                .navigate(VendorPassesFragmentDirections.actionVendorPassesFragmentToVendorGrantFragment())
            stamps.newStamp.value?.eventPassSelectedComplete()
        })

        // handle selected pass
        passes.eventPassSelected.observe(this, Observer {
            if (it == null)
                return@Observer

            stamps.newStamp.value?.setPass(it)
            passes.eventPassSelectedComplete()
        })

        return view
    }


}
