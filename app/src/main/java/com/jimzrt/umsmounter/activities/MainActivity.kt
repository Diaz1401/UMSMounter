package com.jimzrt.umsmounter.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.gson.GsonBuilder
import com.jimzrt.umsmounter.BuildConfig
import com.jimzrt.umsmounter.R
import com.jimzrt.umsmounter.adapters.SlidersIntroAdapter
import com.jimzrt.umsmounter.databinding.ActivityMainBinding
import com.jimzrt.umsmounter.fragments.DownloadFragment.OnImageDownloadListener
import com.jimzrt.umsmounter.fragments.ImageCreationFragment.OnImageCreationListener
import com.jimzrt.umsmounter.model.DownloadItem
import com.jimzrt.umsmounter.model.ImageItem
import com.jimzrt.umsmounter.tasks.*
import com.jimzrt.umsmounter.utils.BackgroundTask
import com.jimzrt.umsmounter.utils.Helper
import com.jimzrt.umsmounter.utils.SharedPrefsHelper


class MainActivity : AppCompatActivity(), OnImageCreationListener, OnImageDownloadListener {


    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
   // private var mainFragment: MainFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        SharedPrefsHelper.init(this)
        setContentView(binding.root)

        if (isFirstRun()) { // Set wizard slides
            val intent = Intent(this, SlidersIntroAdapter::class.java)
            binding.root.context.startActivity(intent)
        }

        setSupportActionBar(binding.appBarMain.toolbar)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_credits, R.id.nav_credits), binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // TODO: Check initial USB function!
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //R.id.action_revert -> mainFragment!!.unmount("mtp,adb")
            //R.id.action_check_dependencies -> checkPrerequisites()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun isFirstRun() : Boolean {
        return SharedPrefsHelper.read(SharedPrefsHelper.IS_FIRST_RUN, true) ||
                 BuildConfig.VERSION_NAME != SharedPrefsHelper.read(SharedPrefsHelper.VERSION, "")
    }

    override fun onImageCreation(imageItem: String?) {
        val imageItemObj = ImageItem(imageItem!!, "$ROOTPATH/$imageItem", "$USERPATH/$imageItem", Helper.humanReadableByteCount(0))
        //mainFragment!!.createImage(imageItemObj)
    }

    override fun onImageListClick(downloadItem: DownloadItem?) {
        val gson = GsonBuilder().create()
        val downloadItemString = gson.toJson(downloadItem)
        val intent = Intent(this, LinuxImageActivity::class.java)
        intent.putExtra("downloadItem", downloadItemString)
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                val name = data!!.getStringExtra("name")
                val url = data.getStringExtra("url")
                val imageItem = ImageItem(name!!, "$ROOTPATH/$name", "$USERPATH/$name", Helper.humanReadableByteCount(0))
                imageItem.url = url
            }
        }
    }

    companion object {
        const val ROOTPATH = "/UMSMounter"
        const val CACHEDIR = "/cache"
        const val USERPATH = "/sdcard/UMSMounter" // TODO: Obtener auto
        const val WRITE_EXTERNAL_STORAGE_PERM = 1337
    }
}

/* Necesito esto para leer los ficheros
    val rootDir = DocumentFile.fromTreeUri(this, uri)
    rootDir?.listFiles()?.forEach { file ->
        Log.d("FILES!", "Found file: ${file.name}")
    }*/

