package com.stamp


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.stamp.databinding.FragmentVendorGrantBinding
import com.stamp.viewModel.StampsViewModel

// todo: onCreateView needs cleanup
class VendorGrantFragment : Fragment() {

    private lateinit var binding: FragmentVendorGrantBinding
    private lateinit var stamps: StampsViewModel

    // Values for preset buttons
    private val stampPresets: IntArray = intArrayOf(2, 3, 5)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vendor_grant, container, false)
        binding.lifecycleOwner = this

        stamps = activity?.run {
            ViewModelProviders.of(this)[StampsViewModel::class.java]
        } ?: throw Exception("Invalid activity.")
        binding.stamp = stamps.newStamp.value

        binding.apply {
            buttonStampAmountPresetOne.text = stampPresets[0].toString()
            buttonStampAmountPresetOne.tag = stampPresets[0]
            buttonStampAmountPresetTwo.text = stampPresets[1].toString()
            buttonStampAmountPresetTwo.tag = stampPresets[1]
            buttonStampAmountPresetThree.text = stampPresets[2].toString()
            buttonStampAmountPresetThree.tag = stampPresets[2]

            buttonStampAmountUp.setOnClickListener {
                setGrantStamps(1, true)
            }

            buttonStampAmountDown.setOnClickListener {
                setGrantStamps(-1, true)
            }

            buttonStampAmountPresetOne.setOnClickListener {
                setGrantStamps(stampPresets[0], false)
            }
            buttonStampAmountPresetTwo.setOnClickListener {
                setGrantStamps(stampPresets[1], false)
            }
            buttonStampAmountPresetThree.setOnClickListener {
                setGrantStamps(stampPresets[2], false)
            }

            buttonContinue.setOnClickListener {
                view?.findNavController()
                    ?.navigate(VendorGrantFragmentDirections.actionVendorGrantFragmentToScannerFragment())
            }
        }

        return binding.root
    }

    private fun setGrantStamps(stamps: Int = 1, relative: Boolean = false) {
        this.stamps.newStamp.value?.setAmount(
            if (relative)
                this.stamps.newStamp.value?.amount?.value?.plus(stamps) ?: stamps
            else
                stamps
        )
    }
}
