package com.jimzrt.umsmounter.tasks

import android.content.Context
import com.topjohnwu.superuser.Shell
import java.io.File

class CheckMassStorageTask : BaseTask() {
    var CONFIGFS_DIR = "/config/usb_gadget/g1/functions/mass_storage.0/lun.0"
    var CONFIGFS_CDROM_DIR = "/config/usb_gadget/g1/functions/mass_storage.0/lun.0/cdrom"
    override fun execute(): Boolean {
        val sharedPref = ctx!!.get()!!.getSharedPreferences(null, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val usbDir = File("/sys/class/android_usb/android0/f_mass_storage/lun")
        val usbDir2 = File("/sys/class/android_usb/android0/f_mass_storage/lun0")
        return if (suPathExists(CONFIGFS_DIR)) {
            result = "mass_storage via configfs supported!\n"
            editor.putString("usbMode", "configfs")
            editor.putString("usbPath", CONFIGFS_DIR)
            if (suPathExists(CONFIGFS_CDROM_DIR)) {
                editor.putBoolean("cdrom", true)
            } else {
                editor.putBoolean("cdrom", false)
            }
            editor.apply()
            true
        } else if (usbDir.exists() && usbDir.isDirectory) {
            result = "mass_storage via android_usb supported!\n"
            editor.putString("usbMode", "android_usb")
            editor.putString("usbPath", usbDir.absolutePath)
            val cdrom = File("/sys/class/android_usb/android0/f_mass_storage/lun/cdrom")
            if (cdrom.exists()) {
                editor.putBoolean("cdrom", true)
            } else {
                editor.putBoolean("cdrom", false)
            }
            editor.apply()
            true
        } else if (usbDir2.exists() && usbDir2.isDirectory) {
            result = "mass_storage via android_usb supported!\n"
            editor.putString("usbMode", "android_usb")
            editor.putString("usbPath", usbDir2.absolutePath)
            val cdrom = File("/sys/class/android_usb/android0/f_mass_storage/lun/cdrom")
            if (cdrom.exists()) {
                editor.putBoolean("cdrom", true)
            } else {
                editor.putBoolean("cdrom", false)
            }
            editor.apply()
            true
        } else {
            result = "mass_storage not supported!\n"
            false
        }
    }


    fun suPathExists(path: String): Boolean {
        var result = Shell.cmd("if [ -d \"$path\" ]; echo true; fi").exec()
        return result.out[0] == "true"
    }

    init {
        name = "Checking mass_storage"
        description = "Checking mass_storage support..."
    }
}