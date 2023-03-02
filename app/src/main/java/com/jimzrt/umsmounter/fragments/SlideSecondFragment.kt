package com.jimzrt.umsmounter.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.github.appintro.SlidePolicy
import com.jimzrt.umsmounter.R
import com.jimzrt.umsmounter.utils.SharedPrefsHelper
import com.topjohnwu.superuser.Shell

class SlideSecondFragment : Fragment(), SlidePolicy {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_slide2, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var button = view.findViewById<Button>(R.id.selectDirectoryButoon)
        var filePickerIntent = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
           onDirectoryPickCompleted(uri)
        }
        button.setOnClickListener {
            filePickerIntent.launch(null)
        }
    }

    override val isPolicyRespected: Boolean
        get() = SharedPrefsHelper.read(SharedPrefsHelper.HAS_RW_EXTERNAL_PERMISSION, false)

    override fun onUserIllegallyRequestedNextPage() {
        Toast.makeText(
            requireContext(),
            "You have not selected a directory or you don't have permissions",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onDirectoryPickCompleted(uri: Uri) {
        uri.let {
            context?.contentResolver?.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            Log.d("VEAMOS", "DIRPATHJAJA: ${uri.path}")
            SharedPrefsHelper.write(SharedPrefsHelper.HAS_RW_EXTERNAL_PERMISSION, true)
            SharedPrefsHelper.write(SharedPrefsHelper.USER_PATH, uri.path!!)
            /* Necesito esto para leer los ficheros
                val rootDir = DocumentFile.fromTreeUri(this, uri)
                rootDir?.listFiles()?.forEach { file ->
                    Log.d("FILES!", "Found file: ${file.name}")
                }*/
        }
    }


}