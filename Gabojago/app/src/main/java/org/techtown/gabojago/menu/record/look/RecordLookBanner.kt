package org.techtown.gabojago.menu.record.look

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.techtown.gabojago.databinding.FragmentLookVpBinding
import org.techtown.gabojago.databinding.FragmentRecordLookBinding

class RecordLookBanner(val imgRes : Int) : Fragment() {

    lateinit var binding : FragmentLookVpBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentLookVpBinding.inflate(inflater, container, false)

        binding.bannerImageIv.setImageResource(imgRes)

        return binding.root
    }
}