package com.stamp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.stamp.adapter.PassAdapter
import com.stamp.databinding.FragmentPassesBinding
import com.stamp.viewModel.PassesViewModel

open class PassesFragment : Fragment() {

    private lateinit var binding: FragmentPassesBinding

    private lateinit var passesViewModel: PassesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        passesViewModel = activity?.run {
            ViewModelProviders.of(this)[PassesViewModel::class.java]
        } ?: throw Exception("Invalid activity.")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPassesBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.passesViewModel = passesViewModel

        binding.viewPasses.adapter = PassAdapter(PassAdapter.OnClickListener {
            passesViewModel.selectPass(it)
        })

        return binding.root
    }
}
