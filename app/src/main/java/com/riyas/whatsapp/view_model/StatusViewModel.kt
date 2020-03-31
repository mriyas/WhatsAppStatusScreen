package com.riyas.whatsapp.view_model

import android.R
import android.app.Application
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.lifecycle.MutableLiveData
import com.riyas.whatsapp.model.*
import kotlinx.android.synthetic.main.fragment_status.*
import java.util.*
import kotlin.collections.ArrayList

class StatusViewModel(application: Application) : BaseViewModel(application) {
    val TAG = javaClass.simpleName
    var mLastLoadedIndex = 0
    private val items = ArrayList<StatusItem>()
    private val statusProgressBars = ArrayList<ProgressBar>()
    private val statusProgressTimers = ArrayList<Timer>()
    /*  Sample Data
      § A few video URLs
      § http://www.exit109.com/~dnn/clips/RW20seconds_1.mp4
      § http://www.exit109.com/~dnn/clips/RW20seconds_2.mp4
      § http://commondatastorage.googleapis.com/gtv-videosbucket/sample/ForBiggerEscapes.mp4
      § A few image URLs
      § https://i.picsum.photos/id/1011/900/1600.jpg
      § https://i.picsum.photos/id/1012/900/1600.jpg
      § https://i.picsum.photos/id/1013/900/1600.jpg
      § https://i.picsum.photos/id/1014/900/1600.jpg*/

    init {
        initItems()
    }

    fun getStatusItems(): ArrayList<StatusItem> {
        return items
    }

    fun getProgressItems(pos: Int): Pair<ProgressBar, Timer> {
        return Pair(statusProgressBars.get(pos), statusProgressTimers.get(pos))
    }
    fun getCurrentTimer(pos: Int):  Timer {
        return  statusProgressTimers.get(pos)
    }

    fun setIndicator(container: LinearLayout) {
        val size = getStatusItems()?.size
        for (x in 0 until size) {
            container.weightSum = size.toFloat()

            val progressBar = ProgressBar(
                getApplication(), null,
                R.attr.progressBarStyleHorizontal
            )
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                30
            )
            params.weight = 1.0f
            params.setMargins(20, 0, 20, 0)
            progressBar.setLayoutParams(params)
            progressBar.progress = 0
            progressBar.max = 30
            container.addView(progressBar)
            statusProgressBars.add(progressBar)
            statusProgressTimers.add(Timer())

        }

    }

    private fun initItems() {


        var statusItem = StatusItem(StatusItemType.IMAGE)
        statusItem.description = "id/1011/900/1600.jpg"
        statusItem.url = "https://i.picsum.photos/id/1011/900/1600.jpg"
        items.add(statusItem)

        statusItem = StatusItem(StatusItemType.VIDEO)
        statusItem.description = "/clips/RW20seconds_1.mp4"
        statusItem.url = "http://www.exit109.com/~dnn/clips/RW20seconds_1.mp4"
        items.add(statusItem)

        statusItem = StatusItem(StatusItemType.IMAGE)
        statusItem.description = "id/1012/900/1600.jpg"
        statusItem.url = "https://i.picsum.photos/id/1012/900/1600.jpg"
        items.add(statusItem)

        statusItem = StatusItem(StatusItemType.VIDEO)
        statusItem.description = "/clips/RW20seconds_1.mp4"
        statusItem.url = "http://www.exit109.com/~dnn/clips/RW20seconds_1.mp4"
        items.add(statusItem)


        statusItem = StatusItem(StatusItemType.IMAGE)
        statusItem.description = "id/1013/900/1600.jpg"
        statusItem.url = "https://i.picsum.photos/id/1013/900/1600.jpg"
        items.add(statusItem)

        statusItem = StatusItem(StatusItemType.VIDEO)
        statusItem.description = "/gtv-videosbucket/sample/ForBiggerEscapes.mp4"
        statusItem.url =
            "http://commondatastorage.googleapis.com/gtv-videosbucket/sample/ForBiggerEscapes.mp4"
        items.add(statusItem)

        statusItem = StatusItem(StatusItemType.IMAGE)
        statusItem.description = "id/1014/900/1600.jpg"
        statusItem.url = "https://i.picsum.photos/id/1014/900/1600.jpg"
        items.add(statusItem)

    }

    fun changeStatusOfCurrentStatus(mCurrentPage: Int, status: Int) {
        val data = getProgressItems(mCurrentPage!!)
        data?.first?.progress = status
        try {
            data?.second?.cancel()
        }catch (ex: Exception){

        }

    }


}