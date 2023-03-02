package com.jimzrt.umsmounter.fragments


import android.graphics.Color.GREEN
import android.graphics.Color.RED
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.appintro.SlidePolicy
import com.jimzrt.umsmounter.R
import com.topjohnwu.superuser.io.SuFile

class SlideFourthFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_slide4, container, false)

}