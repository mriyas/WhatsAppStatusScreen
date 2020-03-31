package com.riyas.whatsapp.view.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.Target
import com.riyas.whatsapp.R
import com.riyas.whatsapp.databinding.FragmentImageStatusBinding
import com.riyas.whatsapp.model.AppConstants
import kotlinx.android.synthetic.main.fragment_image_status.*
import java.util.*
import com.bumptech.glide.request.RequestListener as RequestListener1


class ImageStatusFragment : StatusBaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)




    }

    override fun onResume() {
        super.onResume()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_image_status,
            container,
            false
        ) as FragmentImageStatusBinding

        val args = arguments
        val desc = args?.getString("desc")
        val url = args?.getString("url")
        val pos = args?.getInt("pos")

        val data=mStatusViewModel.getProgressItems(pos!!)
        mProgressBar=data.first
        mTimer=data.second
        pulse = mProgressBar.max

        binding.desc = desc
        binding.url = url
        binding.listener = getGlidListener(binding.progressbar)
mParentView=binding.main
        // Inflate the layout for this fragment
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTouchListener()


    }

    companion object {


        fun newInstance(url: String?, desc: String?,pos:Int): ImageStatusFragment {

            val fragmentFirst = ImageStatusFragment()

            val args = Bundle()
            args.putString("url", url)
            args.putString("desc", desc)
            args.putInt("pos", pos)
            fragmentFirst.setArguments(args)
            return fragmentFirst
        }
    }

    fun getGlidListener(progressBar: ProgressBar): com.bumptech.glide.request.RequestListener<Drawable> {
        val requestListner = object : RequestListener1<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                progressBar.visibility = View.GONE
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                progressBar.visibility = View.GONE
                return false
            }

        }
        return requestListner
    }


}