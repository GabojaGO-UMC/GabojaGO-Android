package org.techtown.gabojago.menu.goAgain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.techtown.gabojago.databinding.FragmentGoagainBinding

class GoagainFragment : Fragment() {


    lateinit var binding: FragmentGoagainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGoagainBinding.inflate(inflater, container, false)
        val cin = "dkdkkdkd"

        return binding.root
    }

}