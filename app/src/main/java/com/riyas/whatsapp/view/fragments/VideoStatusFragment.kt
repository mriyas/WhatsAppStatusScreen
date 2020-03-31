package com.riyas.whatsapp.view.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.riyas.whatsapp.R
import com.riyas.whatsapp.databinding.FragmentVideoStatusBinding
import com.riyas.whatsapp.view.media.CacheDataSourceFactory
import com.riyas.whatsapp.view_model.StatusViewModel
import kotlinx.android.synthetic.main.fragment_image_status.*
import kotlinx.android.synthetic.main.fragment_video_status.*
import java.lang.Exception


class VideoStatusFragment : StatusBaseFragment() {
    private var playbackPosition = 0L
    private var player: SimpleExoPlayer? = null
    var shouldAutoPlay:Boolean=true
    var currentWindow:Int=0
    var url:String?=null
    var mCurrentPagePosition:Int=0
    lateinit var progressBar:ProgressBar
     var desc: String?=null
    var cache:CacheDataSourceFactory?=null
    init {

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        val args = arguments
        mCurrentPagePosition = args?.getInt("pos",0)!!
        desc = args?.getString("desc")
        url = args?.getString("url")
        val data=mStatusViewModel.getProgressItems(mCurrentPagePosition!!)
        mProgressBar=data.first
        mTimer=data.second
        pulse = mProgressBar.max
        val cachePath="cache_ws_$mCurrentPagePosition"
        cache=  CacheDataSourceFactory.getCache(activity!!, 100 * 1024 * 1024, 5 * 1024 * 1024,cachePath)
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_video_status,
            container,
            false
        ) as FragmentVideoStatusBinding


        this.progressBar=binding.progressBar
        mParentView=binding.main

        binding.desc = desc
        // Inflate the layout for this fragment
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTouchListener()




    }


    override fun onResume() {
        super.onResume()
        if ( player == null) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
       // if (Util.SDK_INT <= 23) {
            releasePlayer()
       // }
    }

    override fun onStop() {
        super.onStop()
        //if (Util.SDK_INT > 23) {
            releasePlayer()
       // }
    }


    private fun initializePlayer() {
        if (player == null) {
            val bandwidthMeter = DefaultBandwidthMeter()
            val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
            val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

            player = ExoPlayerFactory.newSimpleInstance(activity!!, trackSelector)
            val audioSource = ExtractorMediaSource(
                Uri.parse(url),cache
             ,
                DefaultExtractorsFactory(),
                null,
                null
            )
            player?.prepare(audioSource)
            simpleExoPlayerView?.setPlayer(player)

            player?.seekTo(currentWindow, playbackPosition)
            with(player!!) {
                addListener(PlayerEventListener())
                playWhenReady = shouldAutoPlay
            }
            simpleExoPlayerView?.hideController()
            

        }
    }
    private fun releasePlayer() {
        if (player != null) {
            playbackPosition = player?.getCurrentPosition()!!
            currentWindow = player?.getCurrentWindowIndex()!!
            shouldAutoPlay= player?.getPlayWhenReady()!!
            player?.release()
            player = null
        }
    }
    companion object{

        fun newInstance(pos:Int?,url: String?, desc: String?): VideoStatusFragment {
            val fragmentFirst = VideoStatusFragment()
            val args = Bundle()
            args.putString("url", url)
            args.putString("desc", desc)
            args.putInt("pos", pos!!)
            fragmentFirst.setArguments(args)
            return fragmentFirst
        }
    }


    private inner class PlayerEventListener : Player.DefaultEventListener() {

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_IDLE       // The player does not have any media to play yet.
                -> progressBar.visibility = View.VISIBLE
                Player.STATE_BUFFERING  // The player is buffering (loading the content)
                -> progressBar.visibility = View.VISIBLE
                Player.STATE_READY      // The player is able to immediately play
                -> {
                    progressBar.visibility = View.GONE
                }
                Player.STATE_ENDED      // The player has finished playing the media
                -> progressBar.visibility = View.GONE
            }
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            super.onPlayerError(error)
            Toast.makeText(activity,getString(R.string.something_went_wrong),Toast.LENGTH_LONG).show()
            try {
                progressBar.visibility = View.GONE
                player?.stop()
            } catch (e: Exception) {

            }
            initializePlayer()
        }


    }
}