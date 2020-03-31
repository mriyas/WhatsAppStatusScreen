package com.riyas.whatsapp.view.media;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSink;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;
import com.riyas.whatsapp.R;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CacheDataSourceFactory implements DataSource.Factory {
    private final Context context;
    private final DefaultDataSourceFactory defaultDatasourceFactory;
    private final long maxFileSize, maxCacheSize;
    String mCachePath;
    SimpleCache simpleCache;
    static Map<String,CacheDataSourceFactory> mCacheMap=new HashMap<>();

    public static CacheDataSourceFactory getCache(Context context, long maxCacheSize, long maxFileSize,String cachePath){

        if(mCacheMap!=null&&mCacheMap.size()>0){
            CacheDataSourceFactory cach= mCacheMap.get(cachePath);
            if(cach!=null){
              return   cach;
            }
        }
        //if(!TextUtils.isEmpty(cachePath)){
            CacheDataSourceFactory cach=new CacheDataSourceFactory(context, maxCacheSize, maxFileSize, cachePath);
            mCacheMap.put(cachePath,cach);
            return cach;
        //}
    }



     CacheDataSourceFactory(Context context, long maxCacheSize, long maxFileSize,String cachePath) {
        super();

        this.context = context;
        this.maxCacheSize = maxCacheSize;
        this.maxFileSize = maxFileSize;
        this.mCachePath=cachePath;
        String userAgent = Util.getUserAgent(context, context.getString(R.string.app_name));
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        defaultDatasourceFactory = new DefaultDataSourceFactory(this.context,
                bandwidthMeter,
                new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter));
        LeastRecentlyUsedCacheEvictor evictor = new LeastRecentlyUsedCacheEvictor(maxCacheSize);
        simpleCache  = new SimpleCache(new File(context.getCacheDir(), mCachePath), evictor);

    }

    @Override
    public DataSource createDataSource() {
          return new CacheDataSource(simpleCache, defaultDatasourceFactory.createDataSource(),
                new FileDataSource(), new CacheDataSink(simpleCache, maxFileSize),
                CacheDataSource.FLAG_BLOCK_ON_CACHE | CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null);
    }
}