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
//                myAdapter.filter.filter(newText)
                setupContactList(newText!!)
                myAdapter.changeCursor(cursor)
                myAdapter.notifyDataSetChanged()
                return true
            }
        })
    }

    private fun setupContactList(searchString: String) {
        contactMap = HashMap()
        val order = ContactsContract.Contacts.DISPLAY_NAME + " ASC"

        // Get Emails - only these who have email address, contacts wihtout phone and email will no be added
        var projection = arrayOf(
            ContactsContract.RawContacts._ID,
            ContactsContract.CommonDataKinds.Email.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.Email.CONTACT_ID,
            ContactsContract.CommonDataKinds.Email.ADDRESS
        )

        val uri_email = ContactsContract.CommonDataKinds.Email.CONTENT_URI
//        val filter = ContactsContract.CommonDataKinds.Email.DATA + " <> ''"

        var filter: String? = null
        var filterArgs = emptyArray<String>()

        if (!searchString.isEmpty()) {
            filter = ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?"
            filterArgs = arrayOf("%" + searchString + "%")
        }

//        filter = ContactsContract.CommonDataKinds.Email.DISPLAY_NAME_PRIMARY+ " LIKE ?"
//        filterArgs = arrayOf("%r%")

        Log.i("MYTAG Search filter:", "$filter" + filterArgs.contentToString())
        Log.i("MYTAG Search filter:", "$filter" + filterArgs.contentToString())

//        val cursor_email = contentResolver.query(uri_email, projection, filter, null, order)
//        val cursor_email = contentResolver.query(uri_email, projection, filter, arrayOf(searchString), order)
//        val cursor_email = contentResolver.query(uri_email, projection, null, null, order)

        cursor = contentResolver.query(uri_email, projection, filter, filterArgs, order)

        if (cursor!!.moveToFirst()) {
            do {
                val contactModel = ContactModel()
                val id = cursor!!.getInt(2)
                contactModel.id = id
                contactModel.name = cursor!!.getString(1)
                contactModel.email = cursor!!.getString(3)
                contactMap[id] = contactModel
//                Log.i(TAG, cursor_email.getString(1) + " " + cursor_email.getString(2) + " " + cursor_email.getString(3))
            } while (cursor!!.moveToNext())
        }

        Log.i(TAG, "Emails done!" + cursor!!.count)

        // Get base contact data: ID, name, phone - only these who have phone number
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

        projection = arrayOf(
            ContactsContract.RawContacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val cursor_name = contentResolver.query(uri, projection, null, null, order)

        