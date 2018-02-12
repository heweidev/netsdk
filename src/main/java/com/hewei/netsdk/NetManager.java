package com.hewei.netsdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.TypeAdapters;
import com.hewei.netsdk.converter.MyGsonConvertFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by fengyinpeng on 2018/2/9.
 */

public class NetManager {
    private static NetManager sInst;
    private Context mContext;
    private OkHttpClient mOkHttpClient;
    private Retrofit mRetrofit;
    private Gson mGson;
    private CryptoEngine mCryptoEngine;

    private boolean mNetworkAvailable = true;

    public static NetManager getInstance() {
        if (sInst == null) {
            sInst = new NetManager();
        }
        return sInst;
    }

    public void initGson() {
        mGson = new GsonBuilder()
                .registerTypeAdapterFactory(TypeAdapters.newFactory(boolean.class, Boolean.class, MyTypeAdapter.BOOLEAN))
                .registerTypeAdapterFactory(MyTypeAdapter.ENUM_FACTORY)
                .create();
    }

    public void initOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        mOkHttpClient = new OkHttpClient.Builder()
                .cookieJar(CookieJar.getIns())
                .addInterceptor(loggingInterceptor)
                .addNetworkInterceptor(new NetworkInterceptor(mContext, this))
                .build();
    }

    public void initRetrofit(String baseUrl) {
        if (mOkHttpClient == null) {
            throw new IllegalStateException("call initOkHttpClient first!");
        }

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(MyGsonConvertFactory.create(mGson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mOkHttpClient)
                .build();
    }

    private void initNetwork() {
        // 监控网络变化
        mContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                    NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                    mNetworkAvailable = info != null && NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable();
                }
            }
        }, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public void init(Context context, String baseUri) {
        mContext = context;
        initGson();
        initOkHttpClient();
        initRetrofit(baseUri);
        initNetwork();
    }

    public void setCryptoEngine(CryptoEngine cryptoEngine) {
        mCryptoEngine = cryptoEngine;
    }

    public boolean isNetworkAvailable() {
        return mNetworkAvailable;
    }

    public String getUserAgent() {
        return "okhttp3";
    }

    public <T> T createApi(Class<T> cls) {
        return mRetrofit.create(cls);
    }

    public Gson getGson() {
        return mGson;
    }

    public OkHttpClient getClient() {
        return mOkHttpClient;
    }

    public static String cryptoEngine(String raw) {
        return NetManager.getInstance().mCryptoEngine.encrypt(raw);
    }

    public static String md5(String raw) {
        return NetManager.getInstance().mCryptoEngine.md5(raw);
    }

    public static String decrypt(String raw) throws Exception {
        return NetManager.getInstance().mCryptoEngine.decrypt(raw);
    }
}
