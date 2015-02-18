package main.java;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.webviewbrowser.R;


public class MainActivity extends Activity {
  private long pageStartTime = 0;
  private WebView wv;
  private ImageButton goBtn;
  private EditText et;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    wv = (WebView) findViewById(R.id.wv);
    goBtn = (ImageButton) findViewById(R.id.go);
    et = (EditText) findViewById(R.id.et);

    // setup edit text
    et.setSelected(false);
    if (getIntent().getStringExtra("url") != null) {
      et.setText(getIntent().getStringExtra("url"));
    } else {
      et.setText("");
    }

    // setup wv
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      WebView.setWebContentsDebuggingEnabled(true);
    }
    wv.setWebChromeClient(new WebChromeClient());
    wv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    WebSettings settings = wv.getSettings();
    settings.setAllowUniversalAccessFromFileURLs(true);
    settings.setJavaScriptEnabled(true);
    settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    settings.setAppCacheEnabled(false);
    settings.setDomStorageEnabled(true);
    wv.setWebViewClient(new WebViewClient() {
      @Override
      public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        et.setText(url);
        currentUrl = url;
      }
    });
    handleLoadUrl();

    // setup events
    goBtn.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
          goBtn.setColorFilter(getResources().getColor(android.R.color.holo_blue_dark));
          return false;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
          goBtn.setColorFilter(null);
          return false;
        }
        return false;
      }
    });

    goBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.putExtra("url", et.getText().toString());
        startActivity(intent);
      }
    });
    et.setOnKeyListener(new View.OnKeyListener() {
      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
          handleLoadUrl();
          return true;
        } else {
          return false;
        }
      }
    });
  }

  private void handleLoadUrl() {
    String url = et.getText().toString();
    if (url.startsWith("http://")) {
    } else if (url.startsWith("https://")) {
    } else {
      url = String.format("http://%s", url);
    }
    wv.loadUrl(url);
    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
  }

}