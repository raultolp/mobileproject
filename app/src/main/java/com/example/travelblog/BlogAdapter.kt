/*
@file:Suppress("LossyEncoding")

package com.example.travelblog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
//import kotlinx.android.synthetic.main.single_recipe.view.*  //praksis ka see
import com.example.travelblog.MainActivity
import com.example.travelblog.room.BlogEntity

//pulls data from a RecipeViewModels entityArray member variable.
//It is a custom adapter, based on BaseAdapter.
//In previous lab, we were referring to a list of items inside adapter, now we are referring
// to a ViewModel object instead.


// This adapter is used in MainActivity.

class BlogAdapter(var model: BlogViewModel,  var activity: MainActivity) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(parent?.context)
            view = layoutInflater.inflate(R.layout.single_recipe, parent, false)
        } else{
            view = convertView
        }

        val recipe = getItem(position)

        view.recipeTitleTextview.text = recipe.title
        view.recipeAuthorTextview.text = recipe.author
        view.recipePreptimeTextview.text = recipe.prepTime.toString() //added

        view.setOnClickListener {
            activity.openDetails(recipe.id)
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

*/
