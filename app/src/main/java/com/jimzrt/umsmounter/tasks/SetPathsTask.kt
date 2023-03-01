package com.jimzrt.umsmounter.tasks

import com.jimzrt.umsmounter.model.BaseTask

class SetPathsTask : BaseTask() {
    override fun execute(): Boolean {
        val rootPath = System.getenv("EXTERNAL_STORAGE")
        val userPath = ctx!!.get()!!.getExternalFilesDir("UMSMount")?.absolutePath

        return true
    }

    init {
        name = "Set paths"
        description = "Setting paths..."
    }
}