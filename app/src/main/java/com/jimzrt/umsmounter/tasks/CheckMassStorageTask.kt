package com.jimzrt.umsmounter.tasks

import android.content.Context
import android.content.SharedPreferences.Editor
import com.jimzrt.umsmounter.model.BaseTask
import com.topjohnwu.superuser.io.SuFile

const val CONFIGFS_DIR = "/config/usb_gadget/g1/functions/mass_storage.0/lun.0"
const val CONFIGFS_CDROM_DIR = "/config/usb_gadget/g1/functions/mass_storage.0/lun.0/cdrom"
const val ANDROIDUSB_DIR = "/sys/class/android_usb/android0/f_mass_storage/lun"
const val ANDROIDUSB_CDROM_DIR = "/sys/class/android_usb/android0/f_mass_storage/lun/cdrom"
const val ALT_ANDROIDUSB_DIR = "/sys/class/android_usb/android0/f_mass_storage/lun0"
const val ALT_ANDROIDUSB_CDROM_DIR = "/sys/class/android_usb/android0/f_mass_storage/lun"

class CheckMassStorageTask : BaseTask() {
    override fun execute(): Boolean {
        val sharedPref = ctx!!.get()!!.getSharedPreferences(null, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        return if (SuFile(CONFIGFS_DIR).exists()) {
            setConfigAsConfigFS(editor)
            true
        } else if (SuFile(ANDROIDUSB_DIR).exists()) {
            setConfigAsAndroidUsb(editor)
            true
        } else if (SuFile(ALT_ANDROIDUSB_DIR).exists()) {
            setConfigAsAltAndroidUsb(editor)
            true
        } else {
            result = "mass_storage not supported!\n"
            false
        }
    }

    fun setConfigAsConfigFS(editor: Editor) {
        result = "mass_storage via configfs supported!\n"
        editor.putString("usbMode", "configfs")
        editor.putString("usbPath", CONFIGFS_DIR)
        editor.putBoolean("cdrom", suPathExists(CONFIGFS_CDROM_DIR))
        editor.apply()
    }

    fun setConfigAsAndroidUsb(editor: Editor) {
        result = "mass_storage via android_usb supported!\n"
        editor.putString("usbMode", "android_usb")
        editor.putString("usbPath", ANDROIDUSB_DIR)
        editor.putBoolean("cdrom", suPathExists(ANDROIDUSB_CDROM_DIR))
        editor.apply()
    }

    fun setConfigAsAltAndroidUsb(editor: Editor) {
        result = "mass_storage via android_usb supported!\n"
        editor.putString("usbMode", "android_usb")
        editor.putString("usbPath", ALT_ANDROIDUSB_DIR)
        editor.putBoolean("cdrom", suPathExists(ALT_ANDROIDUSB_CDROM_DIR))
        editor.apply()
    }
    fun suPathExists(path: String): Boolean {
        return SuFile(path).exists()
    }

    init {
        name = "Checking mass_storage"
        description = "Checking mass_storage support..."
    }
}