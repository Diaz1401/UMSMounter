package com.jimzrt.umsmounter.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.jimzrt.umsmounter.BuildConfig
import com.jimzrt.umsmounter.R
import com.jimzrt.umsmounter.databinding.ActivityMainBinding
import com.jimzrt.umsmounter.fragments.DownloadFragment.OnImageDownloadListener
import com.jimzrt.umsmounter.fragments.ImageCreationFragment.OnImageCreationListener
import com.jimzrt.umsmounter.fragments.MainFragment
import com.jimzrt.umsmounter.model.DownloadItem
import com.jimzrt.umsmounter.model.ImageItem
import com.jimzrt.umsmounter.tasks.*
import com.jimzrt.umsmounter.utils.BackgroundTask
import com.jimzrt.umsmounter.utils.Helper

class MainActivity : AppCompatActivity(), OnImageCreationListener, OnImageDownloadListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    private var mainFragment: MainFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        binding.appBarMain.toolbar.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main2)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_credits, R.id.nav_credits), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        if (findViewById<View?>(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return
            }
            mainFragment = MainFragment()
            val sharedPref = getSharedPreferences(null, Context.MODE_PRIVATE)
            val firstRun = sharedPref.getBoolean("firstRun", true)
            val version = sharedPref.getString("version", "")
            if (firstRun || BuildConfig.VERSION_NAME != version) {
                checkPrerequisites()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // TODO: Check initial USB function!
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_revert -> mainFragment!!.unmount("mtp,adb")
            R.id.action_check_dependencies -> checkPrerequisites()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkPrerequisites() {
        val sharedPref = getSharedPreferences(null, Context.MODE_PRIVATE)
        val context = this
        BackgroundTask(this).setDelegate(object : BackgroundTask.AsyncResponse {
            override fun processFinish(successful: Boolean?, output: String?) {
                if (successful!!) {
                    val editor = sharedPref.edit()
                    editor.putBoolean("firstRun", false)
                    editor.putString("version", BuildConfig.VERSION_NAME)
                    editor.apply()
                } else {
                    val editor = sharedPref.edit()
                    editor.clear().apply()
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage(output)
                            .setTitle("Error!")
                    builder.setPositiveButton("Ok", null)
                    val dialog = builder.create()
                    dialog.show()
                }
            }
        }
        ).setTasks(arrayOf(CheckRootTask(), CheckPermissionTask(), SetPathsTask(), CheckFolderTask(), CheckMassStorageTask())).execute()
    }

    override fun onImageCreation(imageItem: String?) {
        val imageItemObj = ImageItem(imageItem!!, "$ROOTPATH/$imageItem", "$USERPATH/$imageItem", Helper.humanReadableByteCount(0))
        mainFragment!!.createImage(imageItemObj)
    }

    override fun OnImageListClick(downloadItem: DownloadItem?) {
        val gson = GsonBuilder().create()
        val downloadItemString = gson.toJson(downloadItem)
        val intent = Intent(this, LinuxImageActivity::class.java)
        intent.putExtra("downloadItem", downloadItemString)
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Check which request we're responding to
        if (requestCode == 0) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                val name = data!!.getStringExtra("name")
                val url = data.getStringExtra("url")
                val imageItem = ImageItem(name!!, "$ROOTPATH/$name", "$USERPATH/$name", Helper.humanReadableByteCount(0))
                imageItem.url = url
                mainFragment!!.addImage(imageItem)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_PERM) {
            val sharedPref = getSharedPreferences(null, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putBoolean("hasPermission", true)
            editor.apply()
            Toast.makeText(this, "granteddd!!!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "dont know this shit", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        const val ROOTPATH = "/UMSMounter"
        const val CACHEDIR = "/cache"
        const val USERPATH = "/sdcard/UMSMounter" // TODO: Obtener auto

        const val WRITE_EXTERNAL_STORAGE_PERM = 1337
    }
}