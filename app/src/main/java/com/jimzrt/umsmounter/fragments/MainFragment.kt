package com.jimzrt.umsmounter.fragments

import android.content.res.ColorStateList
import android.graphics.Color.RED
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.TooltipCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.jimzrt.umsmounter.R
import com.jimzrt.umsmounter.adapters.ImageListAdapter
import com.jimzrt.umsmounter.databinding.FragmentMainBinding
import com.jimzrt.umsmounter.model.ImageItem
import com.jimzrt.umsmounter.tasks.ImagesController

/**
 * A placeholder fragment containing a simple view.
 */
class MainFragment : Fragment() {

    private lateinit var imagesController : ImagesController
    private lateinit var bindings : FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        bindings = FragmentMainBinding.inflate(layoutInflater)
        imagesController = ImagesController(requireContext())
        return bindings.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listView = view.findViewById(R.id.listView) as ListView
        // Configure ListView
        val localImages = imagesController.getLocalImages()
        listView.adapter = ImageListAdapter(requireContext(), localImages)
        listView.setOnItemClickListener{ adapterView, _, position, _ ->
            onListItemClick(adapterView, position)
        }

        // Configure FloatingActionButton
        if (imagesController.isImageMounted()) {
            bindings.fab.backgroundTintList = ColorStateList.valueOf(RED)
        }
        bindings.fab.setOnClickListener{onFloatingActionButtonClick()}

    }

    private fun onFloatingActionButtonLongClick() : Boolean {
        Log.d("TOOLTIP", "TOOLTIP")
        TooltipCompat.setTooltipText(requireView(), "This is a tooltip")
        view?.requestFocus() // Request focus for the view
        return true
    }

    private fun onFloatingActionButtonClick() {
        if (imagesController.isImageMounted()) {
            imagesController.unmountImage()
            bindings.fab.backgroundTintList = ColorStateList
                .valueOf(ContextCompat.getColor(requireContext(), R.color.bright_grey))
        }
    }

    private fun onListItemClick(adapterView: AdapterView<*>, position: Int) { // ListView
        val image = adapterView.getItemAtPosition(position) as ImageItem
        imagesController.mountImage(image)
        Toast.makeText(context, "Hosting $image", Toast.LENGTH_LONG).show()
        bindings.fab.backgroundTintList = ColorStateList.valueOf(RED)
    }



/*

    fun createImage(destFile: ImageItem) {
        model!!.unselect()
        model!!.add(destFile)
        destFile.isDownloading = true
        val oldProgress = intArrayOf(0)
        val createImageThread = Thread(Runnable {
            try {
                val sourceFile = MainActivity.USERPATH + "/fat.img"
                var fis: FileInputStream? = null
                var fos: FileOutputStream? = null
                var size: Long = 0
                try {
                    fis = FileInputStream(sourceFile)
                    fos = FileOutputStream(destFile.userPath, true)
                    size = fis.channel.size() + fos.channel.size()
                    val buffer = ByteArray(1024 * 512)
                    var pos: Long = ceil(fos.channel.size() / buffer.size.toDouble()).toLong()
                    var noOfBytes: Int


                    // read bytes from source file and write to destination file
                    while (fis.read(buffer).also { noOfBytes = it } != -1) {
                        // if(pos % 4 == 0){
                        pos += 1
                        val finalPos = pos
                        val progress = (finalPos * buffer.size * 100L / size).toInt()
                        destFile.progress = progress
                      //  destFile.size = Helper.humanReadableByteCount(finalPos * buffer.size)
                        if (progress - oldProgress[0] > 1) {
                            oldProgress[0] = progress
                            model!!.downloading(destFile)
                        }
                        fos.write(buffer, 0, noOfBytes)
                    }
                } catch (e: FileNotFoundException) {
                    println("File not found$e")
                } catch (ioe: IOException) {
                    println("Exception while copying file $ioe")
                } finally {
                    // close the streams using close method
                    try {
                        fis?.close()
                        fos?.close()
                    } catch (ioe: IOException) {
                        println("Error while closing stream: $ioe")
                    }
                    val src = File(sourceFile)
                    src.delete()
                }


                //
                val finalSize = size
                requireActivity().runOnUiThread {
                    Toast.makeText(activity, "Image successfully created", Toast.LENGTH_LONG).show()
                    destFile.isDownloading = false
                 //   destFile.size = Helper.humanReadableByteCount(finalSize)
                    model!!.downloading(destFile)
                    listView!!.smoothScrollToPosition(listViewAdapter!!.getPositionOfItem(destFile))
                    model!!.select(destFile)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
        createImageThread.isDaemon = true
        createImageThread.start()
    }

    fun addImage(imageItem: ImageItem) {
        model!!.unselect()
        model!!.remove(imageItem)
        model!!.add(imageItem)
        imageItem.isDownloading = true
        val request = Request(imageItem.url!!, MainActivity.USERPATH + "/" + imageItem.name)
        request.priority = Priority.HIGH
        request.networkType = NetworkType.WIFI_ONLY
        imageItem.downloadId = request.id
        val fetchListener: FetchListener = object : FetchListener {
            override fun onWaitingNetwork(download: Download) {
                activity!!.runOnUiThread { Toast.makeText(context, "Waiting for Network!", Toast.LENGTH_SHORT).show() }
            }

            override fun onStarted(download: Download, downloadBlocks: List<DownloadBlock>, totalBlocks: Int) {
                activity!!.runOnUiThread { Toast.makeText(context, "Download started!", Toast.LENGTH_SHORT).show() }
            }

            override fun onError(download: Download, error: Error, throwable: Throwable?) {
                model!!.remove(imageItem)
                activity!!.runOnUiThread {
                    val file = File(imageItem.userPath)
                    file.delete()
                    Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show()
                    mainFetch!!.removeListener(this)
                    mainFetch!!.remove(download.id)
                }
            }

            override fun onDownloadBlockUpdated(download: Download, downloadBlock: DownloadBlock, totalBlocks: Int) {}
            override fun onQueued(download: Download, waitingOnNetwork: Boolean) {}
            override fun onAdded(download: Download) {}
            override fun onCompleted(download: Download) {
                imageItem.isDownloading = false
            //    imageItem.size = Helper.humanReadableByteCount(download.total)
                model!!.downloading(imageItem)
                listView!!.smoothScrollToPosition(listViewAdapter!!.getPositionOfItem(imageItem))
                model!!.select(imageItem)
                activity!!.runOnUiThread { Toast.makeText(context, "Downloaded!", Toast.LENGTH_SHORT).show() }
                mainFetch!!.removeListener(this)
                mainFetch!!.remove(download.id)
            }

            override fun onProgress(download: Download, etaInMilliSeconds: Long, downloadedBytesPerSecond: Long) {
                if (request.id == download.id) {
                    imageItem.progress = download.progress
         //           imageItem.size = Helper.humanReadableByteCount(download.downloaded) + " / " + Helper.humanReadableByteCount(download.total) + " - " + Helper.humanReadableByteCount(downloadedBytesPerSecond) + "/s"
                    model!!.downloading(imageItem)
                    val progress = download.progress
                    Log.d("Fetch", "Progress Completed :$progress")
                }
            }

            override fun onPaused(download: Download) {}
            override fun onResumed(download: Download) {}
            override fun onCancelled(download: Download) {
                model!!.remove(imageItem)
                activity!!.runOnUiThread {
                    val file = File(imageItem.userPath)
                    file.delete()
                    Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show()
                    mainFetch!!.removeListener(this)
                    mainFetch!!.remove(download.id)
                }
            }

            override fun onRemoved(download: Download) {}
            override fun onDeleted(download: Download) {}
        }
        mainFetch!!.addListener(fetchListener)
        mainFetch!!.enqueue(request, Func { download: Request -> Log.i("lala", "added " + download.id) }, Func { error: Error -> Log.i("lala", "error  " + error.name) })
    }
*/

}