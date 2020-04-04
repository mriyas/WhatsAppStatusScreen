package com.riyas.whatsapp.view.fragments

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.riyas.whatsapp.model.AppConstants
import com.riyas.whatsapp.view_model.StatusViewModel
import kotlinx.android.synthetic.main.fragment_status.*
import java.util.*
import kotlin.concurrent.timer

open class StatusBaseFragment : Fragment() {
    val mStatusViewModel: StatusViewModel by activityViewModels()
    val TAG = javaClass.simpleName
    var mCurrentPage = 0
    lateinit var mProgressBar: ProgressBar

    var pulse: Int = 0
    var mTimer: Timer? = null
    lateinit var mParentView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    fun setTouchListener() {

        val displayMetrics = DisplayMetrics()
        activity?.getWindowManager()?.getDefaultDisplay()?.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val onTouch = object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                when (event?.action) {

                    MotionEvent.ACTION_DOWN -> {
                        Log.v(TAG, "setTouchListener() : ACTION_DOWN : x=${event.x} , y=${event.y}")

                        val margin = width / 2
                        if (event.x < margin) {
                            mStatusViewModel.setState(AppConstants.ViewPager.PREV)
                        } else {
                            mStatusViewModel.setState(AppConstants.ViewPager.NEXT)

                        }

                    }
                    MotionEvent.ACTION_UP -> {
                        Log.i(TAG, "setTouchListener() : ACTION_UP : x=${event.x} , y=${event.y}")
                    }

                }
                return true
            }


        }
        mParentView.setOnTouchListener(onTouch)
    }

    override fun onPause() {
        super.onPause()
        if (this !is StatusFragment) {
            cancelTimer()
        }
    }

    override fun onResume() {
        super.onResume()

        if (this !is StatusFragment) {
            runTimer()
        }

    }




    private fun runTimer() {
        if (mTimer != null) {
            cancelTimer()
        }
        mTimer = Timer()
        // val time=pulse
        val task = object : TimerTask() {
            override fun run() {
                if (pulse > 0) {
                    System.out.println("Beep!")
                    pulse--
                    activity?.runOnUiThread(Runnable {
                        mProgressBar.progress = mProgressBar.max - pulse
                    })
                } else {
                    System.out.println("Go To Next!")

                    mStatusViewModel.setState(AppConstants.ViewPager.NEXT_AFTER_TIMER)
                    cancel()
                    cancelTimer()

                }
            }

        }
        mTimer?.schedule(task, 0, (1000).toLong())
    }

    private fun cancelTimer() {
        mTimer?.cancel()
        mTimer = null
    }
}