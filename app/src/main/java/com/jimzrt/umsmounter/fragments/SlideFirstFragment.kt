package com.jimzrt.umsmounter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.appintro.SlidePolicy
import com.jimzrt.umsmounter.R
import com.jimzrt.umsmounter.utils.SharedPrefsHelper
import com.topjohnwu.superuser.Shell

//import com.topjohnwu.superuser.Shell

class SlideFirstFragment : Fragment(), SlidePolicy {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_slide1, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var button = view.findViewById<Button>(R.id.adquireRootButton)
        button.setOnClickListener {
            Shell.cmd().exec() // We need this so the next function works
            val hasRootPermissions = Shell.isAppGrantedRoot()!!
            SharedPrefsHelper.write(SharedPrefsHelper.HAS_ROOT_PERMISSIONS, true)
            SharedPrefsHelper.write(SharedPrefsHelper.HAS_ROOT_PERMISSIONS, hasRootPermissions)
        }
    }

    override val isPolicyRespected: Boolean
        get() = SharedPrefsHelper.read(SharedPrefsHelper.HAS_ROOT_PERMISSIONS, false)

    override fun onUserIllegallyRequestedNextPage() {
        Toast.makeText(requireContext(), "Permissions have not been granted", Toast.LENGTH_SHORT).show()

    }


  }