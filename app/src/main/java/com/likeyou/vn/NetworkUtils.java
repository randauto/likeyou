package com.likeyou.vn;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by le on 10/10/2016.
 */

public class NetworkUtils {
    public static final int CONNECTION_TYPE_OFF = 0;

    public static final int CONNECTION_TYPE_WIFI = 1;

    public static final int CONNECTION_TYPE_3G = 2;

    public static int networkStatus;

    private static NetworkUtils instance;

    private Context context;

    private NetworkUtils(Context paramContext) {
        this.context = paramContext;
    }

    public static NetworkUtils getInstance(Context paramContext) {
        if (instance == null)
            instance = new NetworkUtils(paramContext);
        return instance;
    }

    public boolean isConnected() {
        if (context != null) {
            return getNetworkStatus(context) != CONNECTION_TYPE_OFF;
        }
        return networkStatus != CONNECTION_TYPE_OFF;

    }

    public static int getNetworkStatus(Context ctx) {
        ConnectivityManager connec = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo currentNetworkInfo = connec.getActiveNetworkInfo();

        if (currentNetworkInfo == null) {
            return CONNECTION_TYPE_OFF;
        }

        if (currentNetworkInfo.isConnected()) {
            if (currentNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                networkStatus = CONNECTION_TYPE_WIFI;
            } else {
                networkStatus = CONNECTION_TYPE_3G;
            }
        } else {
            networkStatus = CONNECTION_TYPE_OFF;
        }
        return networkStatus;
    }
}
