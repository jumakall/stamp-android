package com.stamp


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.stamp.databinding.FragmentSplashBinding

/**
 * A simple [Fragment] subclass.
 */
class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)

        binding.buttonContinue.setOnClickListener {
            this.findNavController()
                .navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
        }

        return binding.root
    }


}
