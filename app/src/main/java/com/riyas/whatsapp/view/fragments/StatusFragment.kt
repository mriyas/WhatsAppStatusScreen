package com.riyas.whatsapp.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.riyas.whatsapp.R
import com.riyas.whatsapp.databinding.FragmentStatusBinding
import com.riyas.whatsapp.model.StatusItemType
import com.riyas.whatsapp.view_model.StatusViewModel
import kotlinx.android.synthetic.main.fragment_status.*
import com.riyas.whatsapp.model.AppConstants




class StatusFragment : StatusBaseFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val fr=this
        mStatusViewModel.mState?.observe(activity!!, Observer {
            when (it.state) {
                AppConstants.ViewPager.NEXT_AFTER_TIMER ,
                AppConstants.ViewPager.NEXT -> {
                    var status=100
                    val item=mStatusViewModel.getStatusItems()?.get(mCurrentPage)
                    if(item.type==StatusItemType.IMAGE){
                        status=30
                    }
                    mStatusViewModel.changeStatusOfCurrentStatus(mCurrentPage,status)
                    mCurrentPage++
                    if(mCurrentPage<mStatusViewModel.getStatusItems().size) {
                        vp_main.currentItem =mCurrentPage
                    }
                }
                AppConstants.ViewPager.PREV -> {
                    mStatusViewModel.changeStatusOfCurrentStatus(mCurrentPage,0)
                    mCurrentPage--
                    if(mCurrentPage<0) {
                        mCurrentPage=0
                    }
                    vp_main.currentItem =mCurrentPage
                    mStatusViewModel.changeStatusOfCurrentStatus(mCurrentPage,0)

                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_status,
            container,
            false
        ) as FragmentStatusBinding


        binding.viewModel = mStatusViewModel
        // Inflate the layout for this fragment
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vp_main.adapter = ScreenSlidePagerAdapter(childFragmentManager)
        mStatusViewModel.setIndicator(ll_indicator)


        vp_main.addOnPageChangeListener(
            object : ViewPager.OnPageChangeListener {
                override fun onPageSelected(p0: Int) {
                    // no-op
                }

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                    // no-op
                }

                override fun onPageScrollStateChanged(p0: Int) {
                    when (p0) {
                        ViewPager.SCROLL_STATE_SETTLING -> {
                            /* if (binding.viewPager.currentItem == images.size - 1) {
                                 binding.buttonNext.text = "end"
                             } else {
                                 binding.buttonNext.text = "next"
                             }*/
                        }
                        ViewPager.SCROLL_STATE_IDLE -> {
                            //  isLastPage = binding.viewPager.currentItem == images.size - 1
                        }
                        else -> {
                            // no-op
                        }
                    }
                }
            }
        )

    }


    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    @SuppressLint("WrongConstant")
    private inner class ScreenSlidePagerAdapter(
        fm: FragmentManager,
        status: Int = FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) :
        FragmentStatePagerAdapter(fm, status) {
        override fun getCount(): Int = mStatusViewModel.getStatusItems().size

        override fun getItem(position: Int): Fragment {
            val item = mStatusViewModel.getStatusItems()?.get(position)

            val pair = mStatusViewModel.getProgressItems(position)
            val progrssBar = pair.first
            val timer = pair.second
            val statusType = item?.type
            when (statusType) {
                StatusItemType.VIDEO -> {
                    return VideoStatusFragment.newInstance(position, item?.url, item?.description)
                }
                StatusItemType.IMAGE -> {
                    return ImageStatusFragment.newInstance(
                        item?.url,
                        item?.description,
                       position
                    )
                }
                else -> {
                    return InvalidStatusFragment()
                }
            }
        }
    }

}