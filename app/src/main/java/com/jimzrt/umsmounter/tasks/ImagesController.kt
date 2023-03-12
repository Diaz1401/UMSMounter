package com.jimzrt.umsmounter.tasks

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.jimzrt.umsmounter.model.ImageItem
import com.jimzrt.umsmounter.model.ImageType
import com.jimzrt.umsmounter.utils.SharedPrefsHelper
import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.io.SuFile
import java.lang.reflect.Method
import kotlin.math.floor
import kotlin.math.log
import kotlin.math.pow

class ImagesController(val context: Context) {

    fun getLocalImages() : ArrayList<ImageItem> {
        var path = SharedPrefsHelper.read(SharedPrefsHelper.USER_PATH, "")
        path = "content://com.android.externalstorage.documents$path"
        var images = ArrayList<ImageItem>()

        return try {
            val rootDir = DocumentFile.fromTreeUri(context, Uri.parse(path))
            rootDir?.listFiles()?.forEach { file ->
                if(isImageFile(file.name!!)) {
                    val fileSize = file.length().toDouble()
                    val imageType = getImageType(file.name!!)
                    images.add(ImageItem(file.name!!, formatBytes(fileSize), imageType))
                }
            }
            images
        } catch(e: java.lang.IllegalArgumentException) {
            ArrayList()
        }
    }

    private fun getImageType(filename : String) : ImageType {
        var extension = filename.substringAfterLast('.')
        if (extension == "img") {
            return ImageType.IMG
        }
        return ImageType.ISO
    }
    private fun isImageFile(filename : String) : Boolean {
        if (filename.contains('.')) {
            var extension = filename.substringAfterLast('.')
            return extension == "iso" || extension == "img"
        }
        return false
    }



    fun formatBytes(size: Double, precision: Int = 2): String {
        val base = log(size, 1024.0)
        val suffixes = arrayOf("", "KB", "MB", "GB", "TB")
        val suffixIndex = floor(base).toInt().coerceAtMost(suffixes.size - 1)
        val value = size / 1024.0.pow(base.toInt())

        return "${"%.${precision}f".format(value)} ${suffixes[suffixIndex]}"
    }

    fun mountImageAsMassStorage(image : ImageItem) {
        Shell.cmd("setprop sys.usb.configfs 1",
            "echo \"\" > /config/usb_gadget/g1/UDC",
            "rm /config/usb_gadget/g1/configs/b.1/f*",
            "mkdir -p /config/usb_gadget/g1/functions/mass_storage.0/lun.0/",
            "ln -s /config/usb_gadget/g1/functions/mass_storage.0 /config/usb_gadget/g1/configs/b.1/f1",
            "echo 1 > /config/usb_gadget/g1/configs/b.1/f1/lun.0/removable",
            "echo 0 > /config/usb_gadget/g1/configs/b.1/f1/lun.0/ro",
            "echo 0 > /config/usb_gadget/g1/configs/b.1/f1/lun.0/cdrom",
            "echo \"/storage/emulated/0/ISO/$image\" > /config/usb_gadget/g1/configs/b.1/f1/lun.0/file",
            "getprop sys.usb.controller > /config/usb_gadget/g1/UDC",
            "setprop sys.usb.config mass_storage").exec()
    }

    fun mountImage(image : ImageItem) {
        Shell.cmd("setprop sys.usb.configfs 1",
            "echo \"\" > /config/usb_gadget/g1/UDC",
            "rm /config/usb_gadget/g1/configs/b.1/f*",
            "mkdir -p /config/usb_gadget/g1/functions/mass_storage.0/lun.0/",
            "ln -s /config/usb_gadget/g1/functions/mass_storage.0 /config/usb_gadget/g1/configs/b.1/f1",
            "echo 1 > /config/usb_gadget/g1/configs/b.1/f1/lun.0/removable",
            "echo 0 > /config/usb_gadget/g1/configs/b.1/f1/lun.0/ro",
            "echo 1 > /config/usb_gadget/g1/configs/b.1/f1/lun.0/cdrom",
            "echo \"/storage/emulated/0/ISO/$image\" > /config/usb_gadget/g1/configs/b.1/f1/lun.0/file",
            "getprop sys.usb.controller > /config/usb_gadget/g1/UDC",
            "setprop sys.usb.config cdrom").exec()
    }

    fun unmountImage() {
        Shell.cmd("setprop sys.usb.configfs 1",
            "rm /config/usb_gadget/g1/configs/b.1/f*",
            "ln -s /config/usb_gadget/g1/functions/ffs.adb /config/usb_gadget/g1/configs/b.1/f1",
            "ls /sys/class/udc/ | grep -Eo \".*\\.dwc3\" > /config/usb_gadget/g1/UDC",
            "setprop sys.usb.config adb").exec()
    }

    fun isImageMounted() : Boolean {
        try {
            val systemPropertiesClass = Class.forName("android.os.SystemProperties")
            val getMethod: Method = systemPropertiesClass.getDeclaredMethod("get", String::class.java)
            var value = getMethod.invoke(systemPropertiesClass, "sys.usb.config") as String?
            return value == "cdrom" || value == "mass_storage"
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    fun searchDirectoryForFile(directory: SuFile, fileName: String): String? {
        if (directory.isDirectory) {
            val files = directory.listFiles()
            if (files != null) {
                for (file in files) {
                    if (file.isDirectory && file.name == fileName) {
                        return file.absolutePath
                    } else if (file.isDirectory) {
                        val path = searchDirectoryForFile(file, fileName)
                        if (path != null) {
                            return path
                        }
                    } else if (file.name == fileName) {
                        return file.absolutePath
                    }
                }
            }
        }
        return null
    }
}