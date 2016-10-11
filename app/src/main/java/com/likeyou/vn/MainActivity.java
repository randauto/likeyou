package com.likeyou.vn;

import android.content.ActivityNotFoundException;
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
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {
    private static final String HTML = "http://likeyou.vn?r=0134009a";

    private static final String TEL_PREFIX = "tel:";

    AdView adView;

    private AlertDialog.Builder builder;

    private boolean doubleBackToExitPressedOnce = false;

    private WebView wv;

    public static final int REQUEST_SELECT_FILE = 100;

    public ValueCallback<Uri[]> uploadMessage;

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
        this.wv.setWebChromeClient(new CustomWebChormeClient());
        this.wv.getSettings().setLoadsImagesAutomatically(true);
        this.wv.getSettings().setJavaScriptEnabled(true);
        this.wv.loadUrl(HTML);
    }

    class CustomWebChormeClient extends WebChromeClient {
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            // make sure there is no existing message
            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            }

            uploadMessage = filePathCallback;

            Intent intent = fileChooserParams.createIntent();
            try {
                startActivityForResult(intent, MainActivity.REQUEST_SELECT_FILE);
            } catch (ActivityNotFoundException e) {
                uploadMessage = null;
                Toast.makeText(MainActivity.this, "Cannot open file chooser", Toast.LENGTH_LONG).show();
                return false;
            }

            return true;
        }
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

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
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
            if (paramString.startsWith(TEL_PREFIX)) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_FILE) {
            if (uploadMessage == null){
                return;
            }
            uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
            uploadMessage = null;
        }
    }
}
