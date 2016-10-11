package com.likeyou.vn;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {
    // Remove the below line after defining your own ad unit ID.
    private static final String TOAST_TEXT = "Test ads are being shown. "
            + "To show live ads, replace the ad unit ID in res/values/strings.xml with your own ad unit ID.";

    private static final String HTML = "http://likeyou.vn?r=0134009a";

    private static final String TEL_PREFIX = "tel:";

    AdView adView;

    private AlertDialog.Builder builder;

    private boolean doubleBackToExitPressedOnce = false;

    private WebView wv;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        loadWebsite();
        new Handler().postDelayed(new Runnable() {
                                      public void run() {
                                          MainActivity.this.loadAdsView();
                                      }
                                  }
                , 1000L);



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void loadAdsView() {
        // Load an ad into the AdMob banner view.
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);
    }

    private void loadWebsite() {
        this.wv = ((WebView) findViewById(R.id.webview));
        this.wv.setWebViewClient(new CustomWebViewClient());
        this.wv.getSettings().setLoadsImagesAutomatically(true);
        this.wv.getSettings().setJavaScriptEnabled(true);
        this.wv.loadUrl(HTML);
    }


    private void checkNetWork() {
        if (NetworkUtils.getInstance(this).isConnected())
            return;
        this.builder = new AlertDialog.Builder(this);
        this.builder.setTitle(getString(R.string.thong_bao));
        this.builder.setMessage(getString(R.string.check_internet_msg));
        this.builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                paramDialogInterface.dismiss();
            }
        });
        this.builder.create().show();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private void showAboutUs() {
        if (this.builder == null)
            this.builder = new AlertDialog.Builder(this);
        this.builder.setTitle(getString(R.string.gioithieu));
        this.builder.setMessage(getString(R.string.about_us));
        this.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                paramDialogInterface.dismiss();
            }
        });
        this.builder.create().show();
    }

    private class CustomWebViewClient extends WebViewClient {
        private CustomWebViewClient() {
        }

        public void onPageFinished(WebView paramWebView, String paramString) {
            super.onPageFinished(paramWebView, paramString);
            MainActivity.this.setProgressBarIndeterminateVisibility(false);
        }

        public void onPageStarted(WebView paramWebView, String paramString, Bitmap paramBitmap) {
            super.onPageStarted(paramWebView, paramString, paramBitmap);
            MainActivity.this.setProgressBarIndeterminateVisibility(true);
        }

        public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString) {
            if (paramString.startsWith("tel:")) {
                Intent localIntent = new Intent("android.intent.action.DIAL");
                localIntent.setData(Uri.parse(paramString));
                MainActivity.this.startActivity(localIntent);
                return true;
            }
            return false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.gioithieu) {
            showAboutUs();
            return true;
        }

        if (id == R.id.refresh) {
            loadWebsite();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if ((this.wv != null) && (this.wv.canGoBack())) {
            this.wv.goBack();
            return;
        }

        if (this.doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.back_again), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
                                      public void run() {
                                          MainActivity.this.doubleBackToExitPressedOnce = false;
                                      }
                                  }
                , 2000L);
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        if (this.adView != null) {
            this.adView.pause();
        }

        super.onPause();
    }

    @Override
    protected void onResume() {
        checkNetWork();
        if (this.adView != null) {
            this.adView.resume();
        }

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (this.adView != null) {
            this.adView.destroy();
            this.adView = null;
        }
        this.builder = null;
        if (this.wv != null) {
            this.wv.stopLoading();
            this.wv = null;
        }
        super.onDestroy();
    }
}
