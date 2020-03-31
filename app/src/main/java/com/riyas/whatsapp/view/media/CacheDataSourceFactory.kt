package com.riyas.whatsapp.view.media

import android.content.Context
import android.text.TextUtils

import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.FileDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSink
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import com.riyas.whatsapp.R

import java.io.File
import java.util.HashMap

class CacheDataSourceFactory internal constructor(
    private val context: Context,
    private val maxCacheSize: Long,
    private val maxFileSize: Long,
    internal var mCachePath: String
) : DataSource.Factory {
    private val defaultDatasourceFactory: DefaultDataSourceFactory
    internal var simpleCache: SimpleCache


    init {
        val userAgent = Util.getUserAgent(context, context.getString(R.string.app_name))
        val bandwidthMeter = DefaultBandwidthMeter()
        defaultDatasourceFactory = DefaultDataSourceFactory(
            this.context,
            bandwidthMeter,
            DefaultHttpDataSourceFactory(userAgent, bandwidthMeter)
        )
        val evictor = LeastRecentlyUsedCacheEvictor(maxCacheSize)
        simpleCache = SimpleCache(File(context.cacheDir, mCachePath), evictor)

    }

    override fun createDataSource(): DataSource {
        return CacheDataSource(
            simpleCache, defaultDatasourceFactory.createDataSource(),
            FileDataSource(), CacheDataSink(simpleCache, maxFileSize),
            CacheDataSource.FLAG_BLOCK_ON_CACHE or CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null
        )
    }

    companion object {
        internal var mCacheMap: MutableMap<String, CacheDataSourceFactory>? = HashMap()

        fun getCache(
            context: Context,
            maxCacheSize: Long,
            maxFileSize: Long,
            cachePath: String
        ): CacheDataSourceFactory {

            if (mCacheMap != null && mCacheMap!!.size > 0) {
                val cach = mCacheMap!![cachePath]
                if (cach != null) {
                    return cach
                }
            }
            //if(!TextUtils.isEmpty(cachePath)){
            val cach = CacheDataSourceFactory(context, maxCacheSize, maxFileSize, cachePath)
            mCacheMap!![cachePath] = cach
            return cach
            //}
        }
    }
}