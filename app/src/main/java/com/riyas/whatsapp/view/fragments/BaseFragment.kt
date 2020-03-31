package com.riyas.whatsapp.view.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.text.method.Touch.onTouchEvent
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.riyas.whatsapp.R
import com.riyas.whatsapp.model.AppConstants
import com.riyas.whatsapp.view_model.BaseViewModel
import java.util.logging.Logger

open class BaseFragment: Fragment() {
     val TAG=javaClass.simpleName
     var mBaseViewModel: BaseViewModel?=null

     override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          mBaseViewModel?.mState?.observe(this, Observer {
               when(it.state){
                    AppConstants.NETWORK_FAILURE->{
                        networkFailure()
                    }
               }
          })
     }

     fun networkFailure(){
          showDialog(activity?.getString(R.string.network_failure),
               activity?.getString(android.R.string.ok)
          )
     }

     fun showDialog(message: String?,
                    okMsg: String?, clickListener: DialogInterface.OnClickListener? = null

     ) {
          val dialogBuilder = AlertDialog.Builder(context!!)

          // set message of alert dialog
          dialogBuilder.setMessage(message)
               // if the dialog is cancelable
               .setCancelable(false)
               // positive button text and action
               .setPositiveButton(okMsg, DialogInterface.OnClickListener { dialog, id ->
                    clickListener?.onClick(dialog, id)
                    dialog.dismiss()
               })
          // create dialog box
          val alert = dialogBuilder.create()
          // set title for alert dialog box
          alert.setTitle(activity?.getString(R.string.app_name))
          // show alert dialog
          alert.show()
     }

}