package com.jimzrt.umsmounter.fragments


import android.graphics.Color.GREEN
import android.graphics.Color.RED
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.appintro.SlidePolicy
import com.jimzrt.umsmounter.R
import com.jimzrt.umsmounter.databinding.FragmentSlide2Binding
import com.jimzrt.umsmounter.databinding.FragmentSlide3Binding
import com.jimzrt.umsmounter.utils.SharedPrefsHelper
import com.topjohnwu.superuser.io.SuFile

class SlideThirdFragment : Fragment(), SlidePolicy {
    private val CONFIGFS_DIR = "/config/usb_gadget/g1/functions/mass_storage.0/lun.0"
    private val NOT_COMPATIBLE_TEXT = "Not compatible"
    private val COMPATIBLE_TEXT = "Compatible"

    private lateinit var bindings : FragmentSlide3Binding
    private var isCompatible = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindings = FragmentSlide3Binding.inflate(layoutInflater)
        return bindings.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindings.statusKernelCompatibility.visibility = View.INVISIBLE
        bindings.checkCompatibilityButton.setOnClickListener {
            var textCompatibility = bindings.statusKernelCompatibility
            bindings.statusKernelCompatibility.visibility = View.VISIBLE
            if (SuFile(CONFIGFS_DIR).exists()) {
                textCompatibility.setTextColor(GREEN)
                textCompatibility.text = COMPATIBLE_TEXT
                isCompatible = true
            } else {
                textCompatibility.setTextColor(RED)
                textCompatibility.text = NOT_COMPATIBLE_TEXT
            }
        }
    }

    override val isPolicyRespected: Boolean
        get() = isCompatible

    override fun onUserIllegallyRequestedNextPage() {
        Toast.makeText(
            requireContext(),
            "Sorry, you kernel does not support ConfigFS USB Gadget",
            Toast.LENGTH_SHORT
        ).show()
    }




}