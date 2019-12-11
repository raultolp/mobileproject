package com.example.travelblog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class WindowAdapter(context: Context) : GoogleMap.InfoWindowAdapter  {
    private val context: Context
    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    override fun getInfoContents(marker: Marker): View {
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.window_adapter, null)
        val tvDetails = view.findViewById(R.id.tvd) as TextView
        return view
    }

    init {
        this.context = context.getApplicationContext()
    }

}