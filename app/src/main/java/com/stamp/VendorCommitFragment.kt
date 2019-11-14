package com.stamp


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.stamp.databinding.FragmentVendorCommitBinding
import com.stamp.viewModel.StampsViewModel

class VendorCommitFragment : Fragment() {

    private lateinit var binding: FragmentVendorCommitBinding
    private lateinit var stamps: StampsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        stamps = activity?.run {
            ViewModelProviders.of(this)[StampsViewModel::class.java]
        } ?: throw Exception("Invalid activity.")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vendor_commit, container, false)
        binding.lifecycleOwner = this

        val stamp = stamps.newStamp.value!!
        stamp.commitStamp()
        /*Handler().postDelayed({
            if (stamp.status.value == AdvancedViewModelStatus.IDLE)
                stamp.commitStamp()
        }, 1000)*/

        binding.stamp = stamp
        binding.closeButton.setOnClickListener {
            binding.root.findNavController()
                .navigateUp()
        }

        return binding.root
    }
}
