package com.jimzrt.umsmounter.fragments

import android.Manifest.permission.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.github.appintro.SlidePolicy
import com.jimzrt.umsmounter.databinding.FragmentSlide2Binding

class SlideSecondFragment : Fragment(), SlidePolicy {
    private var isReadPermissionGranted = false
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var bindings : FragmentSlide2Binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted ->
            isReadPermissionGranted = isGranted
        }

        bindings = FragmentSlide2Binding.inflate(layoutInflater)
        return bindings.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindings.askReadPermissionsButoon.setOnClickListener {
            permissionLauncher.launch(READ_EXTERNAL_STORAGE)
        }
    }

    override val isPolicyRespected: Boolean
        get() = isReadPermissionGranted

    override fun onUserIllegallyRequestedNextPage() {
        Toast.makeText(requireContext(), "Permissions have not been granted", Toast.LENGTH_SHORT).show()
    }



}