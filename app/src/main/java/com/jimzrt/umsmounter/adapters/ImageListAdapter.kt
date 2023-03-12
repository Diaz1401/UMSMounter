package com.jimzrt.umsmounter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.jimzrt.umsmounter.R
import com.jimzrt.umsmounter.model.ImageItem
import com.jimzrt.umsmounter.model.ImageType

class ImageListAdapter(private val context: Context, private val images: ArrayList<ImageItem>)
    : ArrayAdapter<ImageItem>(context, R.layout.custom_list, images) {


    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val rowView = inflater.inflate(R.layout.custom_list, null, true)

        val titleText = rowView.findViewById(R.id.title) as TextView
        val imageView = rowView.findViewById(R.id.icon) as ImageView
        val subtitleText = rowView.findViewById(R.id.description) as TextView

        titleText.text = images[position].name
        if (images[position].type == ImageType.ISO) {
            imageView.setImageResource(R.drawable.ic_cd)
        } else {
            imageView.setImageResource(R.drawable.ic_usb)
        }
        subtitleText.text = images[position].size

        return rowView
    }



    private fun isImgFile(filename : String) : Boolean {
        if (filename.contains('.')) {
            var extension = filename.substringAfterLast('.')
            return extension == ".img"
        }
        return false
    }
}