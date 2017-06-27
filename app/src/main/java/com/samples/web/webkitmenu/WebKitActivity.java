package com.samples.web.webkitmenu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class WebKitActivity extends Activity {

    private static final int IDM_URB = 101;
    private static final int IDM_REFRESH = 102;
    private static final int IDM_BACK = 103;
    private static final int IDM_FORWARD = 104;
    private static final int IDM_SETTINGS = 105;
    private static final int IDM_EXIT = 106;

    private LinearLayout layoutBar;
    private WebView browser;
    private EditText textUrl;
    private Button buttonUrl;

    private Button.OnClickListener buttonUrlOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            browser.loadUrl(textUrl.getText().toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_kit);

        layoutBar = (LinearLayout)findViewById(R.id.url_bar);
        browser = (WebView)findViewById(R.id.browser);
        textUrl = (EditText)findViewById(R.id.editeText);
        buttonUrl = (Button)findViewById(R.id.bLoad);
        buttonUrl.setOnClickListener(buttonUrlOnClick);
        browser.setWebViewClient(new WebViewClient());
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(this);
        // Стартовая страница
        String url = prefs.getString(getString(R.string.pk_url), "http://");
        browser.loadUrl(url);
        // Разрешение загрузки графики на страницу
        boolean allowImages =
                prefs.getBoolean(getString(R.string.pk_images), true);
        // Разрешение использования Java-скриптов
        boolean allowJScript =
                prefs.getBoolean(getString(R.string.pk_jscript), true);
        // Разрешение отображать всплывающие окна
        boolean allowPopup =
                prefs.getBoolean(getString(R.string.pk_popup), false);
        // Устанавливаем новые настройки браузера
        WebSettings settings = browser.getSettings();
        settings.setBlockNetworkImage(allowImages);
        settings.setJavaScriptEnabled(allowJScript);
        settings.setJavaScriptCanOpenWindowsAutomatically(allowPopup);
        textUrl.setText(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, IDM_URB, 0, R.string.mn_urlbar);
        menu.add(0, IDM_SETTINGS, 0, R.string.mn_pref);
        menu.add(0, IDM_EXIT, 0, R.string.mn_exit);
        menu.add(0, IDM_BACK, 0, R.string.mn_back);
        menu.add(0, IDM_REFRESH, 0, R.string.mn_refresh);
        menu.add(0, IDM_FORWARD, 0, R.string.mn_forward);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case IDM_URB:
                if (layoutBar.getVisibility() == View.VISIBLE) {
                    layoutBar.setVisibility(View.GONE);
                }
                else {
                    layoutBar.setVisibility(View.VISIBLE);
                }
                break;
            case IDM_REFRESH:
                browser.reload();
                break;
            case IDM_BACK:
                if(browser.canGoBack())
                    browser.goBack();
                break;
            case IDM_FORWARD:
                if(browser.canGoForward())
                    browser.goForward();
                break;
            case IDM_SETTINGS:
                Intent i = new Intent();
                i.setClass(this, WebKitPreferencesActivity.class);
                startActivity(i);
                break;
            case IDM_EXIT:
                this.finish();
                break;
        }
        return true;
    }
}
