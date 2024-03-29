package com.example.homeassign1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.HashMap
import android.database.DataSetObserver
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.example.homeassign1.ContactModel




class MainActivity : AppCompatActivity() {

    lateinit var myAdapter: MyCursorAdapter
    lateinit var contactMap: HashMap<Int, ContactModel>
    var cursor: Cursor? = null
    val PERMISSION_REQ_CODE = 777

    companion object {
        var mailSentTo = mutableSetOf<Int>()
    }

    private val TAG = "MYTAG Main"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (handlePermissions()){
            setupContactList("")
            setupMyAdapter()
            setupListViewListener()
            setupSearchViewListener()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (cursor != null)
            cursor!!.close()
    }

    private fun setupMyAdapter() {
        myAdapter = MyCursorAdapter(this, cursor)
        contacts_listView.adapter = myAdapter
    }

    private fun setupSearchViewListener() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                setupContactList(query!!)
                myAdapter.changeCursor(cursor)
                myAdapter.notifyDataSetChanged()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                myAdap