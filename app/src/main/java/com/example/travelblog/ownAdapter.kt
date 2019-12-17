package com.example.travelblog

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast

public class ownAdapter(var myList: List<String>): BaseAdapter() {

    //View object to show for an item in the dataset:
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(parent?.context)
            view = layoutInflater.inflate(R.layout.list_row, parent, false)
        } else{
            view = convertView // if convertview already exists, it can be re-used
        }
        view.findViewById<TextView>(R.id.titleListView).text = getItem(position).toString() // item
        view.findViewById<TextView>(R.id.titleListView).tag = position.toString() // tag (added to locate the row)
/*        if (view.findViewById<TextView>(R.id.titleListView)!=null){

        }*/
        return view
    }

    // data object at given position index:
    override fun getItem(position: Int): Any {
        return myList[position] //return the object
    }

    //unique id of object at position:
    override fun getItemId(position: Int): Long {
        return myList[position].hashCode().toLong()
    }

    //total number of data elements:
    override fun getCount(): Int {
        return myList.size
    }

}