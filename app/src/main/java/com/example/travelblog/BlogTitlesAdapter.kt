@file:Suppress("LossyEncoding")

package com.example.travelblog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.travelblog.room.BlogEntity
import kotlinx.android.synthetic.main.list_row.view.*
import com.example.travelblog.MainActivity


//pulls data from a RecipeViewModels entityArray member variable.
//It is a custom adapter, based on BaseAdapter.
//In previous lab, we were referring to a list of items inside adapter, now we are referring
// to a ViewModel object instead.


// This adapter is used in MainActivity.

class BlogTitlesAdapter(var model: BlogViewModel, var activity: MainActivity) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(parent?.context)
            view = layoutInflater.inflate(R.layout.list_row, parent, false)
        } else{
            view = convertView
        }

        val item = getItem(position)

        view.list_row_t.text = item.blogTitle
        view.list_row_t.tag = item.blogId
/*        view.recipeAuthorTextview.text = recipe.author
        view.recipePreptimeTextview.text = recipe.prepTime.toString() //added*/

        view.setOnClickListener {
            activity.openBlog(item.blogId)
        }

        return view
    }

    override fun getItem(position: Int): BlogEntity {
        return model.entityArray[position] //pulling items from model's array
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun getCount(): Int {
        return model.entityArray.size
    }
}

