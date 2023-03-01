package com.jimzrt.umsmounter.tasks

import android.util.Log
import com.jimzrt.umsmounter.model.BaseTask
import com.topjohnwu.superuser.Shell

class CheckRootTask : BaseTask() {
    override fun execute(): Boolean {
        Shell.cmd().exec() // We need this so the next function works
        return if (Shell.isAppGrantedRoot() == true) {
            result = "Root working!\n"
            true
        } else {
            Log.d("Root", "Root status ${Shell.isAppGrantedRoot().toString()}")
            result = "Root not working!\n"
            false
        }
    }

    init {
        name = "Check Root"
        description = "Checking root..."
    }
}