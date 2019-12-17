package com.example.travelblog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
//import android.support.v4.app.DialogFragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

//When using the support library, be sure that you import android.support.v4.app.DialogFragment class
// and not android.app.DialogFragment. (??)

//Source: API https://developer.android.com/guide/topics/ui/dialogs

//alert_message: msg_text, ok_button

class AlertDialogFragment : DialogFragment() {

    // Use this instance of the interface to deliver action events
    internal lateinit var listener: AlertDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater (if you wish to have custom layout)
            //val inflater = requireActivity().layoutInflater;

            //Log.i("RESULT", "TAG is: " + this.tag)
            builder.setMessage(this.tag)
                .setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, id ->
                    })
/*                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                        Log.i("RESULT", "cancel pressed in fragment")
                    })*/
            //SET methods require a title for the button and a DialogInterface.OnClickListener

            //If you wish to have custom layout:
            //builder.setView(R.layout.alert_message)


            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }



    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    interface AlertDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = context as AlertDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException((context.toString() +
                    " must implement NoticeDialogListener"))
        }
    }



}


/*
//SIIN PUNKT 3 ERINEV:
// 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
val builder: AlertDialog.Builder? = activity?.let {
    AlertDialog.Builder(it)
}
// 2. Chain together various setter methods to set the dialog characteristics
builder?.setMessage(R.string.dialog_message)
.setTitle(R.string.dialog_title)

// 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
val dialog: AlertDialog? = builder?.create()
*/
