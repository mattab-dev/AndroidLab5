package pl.edu.pwr.wiz.wzorlaboratorium5;

import android.content.res.Configuration;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebviewActivity extends AppCompatActivity {
    private WebView webview;
    private static final String TAG = "WebviewActivity";
    private String url = "https://www.google.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        /* Podpinamy się pod ActionBar */
        setupActionBar();

        /* Znajdujemy nasz WebView */
        webview = (WebView) findViewById(R.id.webview);

        /* Włączamy JavaScript */
        webview.getSettings().setJavaScriptEnabled(true);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                /* Jeśli nie przechwycimy przekierowania to spowodouje ono otwarcie
                   nowego okna z przeglądarką, czego chcemy uniknąć */
                Log.d(TAG, request.getUrl().toString());
                view.loadUrl(request.getUrl().toString());

                return true;
            }
        });
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setDomStorageEnabled(true);

        /* Ladujemy stronę */
        webview.loadUrl(url);
    }

    /* Obsługa przycisków wstecz i do przodu */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webview.canGoBack())
        {
            this.webview.goBack();
            return true;
        }
        else if ((keyCode == 125) && this.webview.canGoForward())
        {// KeyEvent.KEYCODE_FORWARD doesn't work ;/
            this.webview.goForward();
            return true;
        }
        // bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    /* Zapamiętujemy ostatnio odwiedzaną stronę, aby przywrócić ją po zmianie konfiguracji */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
       url = webview.getUrl();
       Log.d(TAG,url);
       super.onConfigurationChanged(newConfig);
    }

    /**
     * Podpięcie pod ActionBar
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
