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

class SlideThirdFragment : Fragment(), SlidePolicy {
    private val CONFIGFS_DIR = "/config/usb_gadget/g1/functions/mass_storage.0/lun.0"
    private val NOT_COMPATIBLE_TEXT = "Not compatible"
    private val COMPATIBLE_TEXT = "Compatible"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_slide3, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var textCompatibility = view.findViewById<TextView>(R.id.statusKernelCompatibility)
        if (SuFile(CONFIGFS_DIR).exists()) {
            textCompatibility.setTextColor(GREEN)
            textCompatibility.text = COMPATIBLE_TEXT
        } else {
            textCompatibility.setTextColor(RED)
            textCompatibility.text = NOT_COMPATIBLE_TEXT
        }
    }

    override val isPolicyRespected: Boolean
        get() = SuFile(CONFIGFS_DIR).exists()

    override fun onUserIllegallyRequestedNextPage() {
        Toast.makeText(
            requireContext(),
            "Sorry, you kernel is not supported",
            Toast.LENGTH_SHORT
        ).show()
    }




}