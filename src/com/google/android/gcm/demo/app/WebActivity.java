package com.google.android.gcm.demo.app;

import org.apache.http.util.EncodingUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends Activity {
	private WebView _webView;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	/**protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webwiew);
		
		Log.d("WebActivity.onCreate", "Dentro");
		Bundle b = getIntent().getExtras();
		Log.d("WebActivity", b.getString("state"));
		
	//		JSONArray arr;
	//		String state ="";
	//		try {
	//			arr = new JSONArray(b.getString("state"));
	//			JSONObject jObj = arr.getJSONObject(0);
	//			state = jObj.getString("state");
	//			Log.d("WebActivity.onCreate", state);
	//		} catch (JSONException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}

		String state = b.getString("state");
		String post = "";
		post+="user=alumno";
		post+="&pass=alumno";
		post+="&jack="+state;		
		
		_webView = (WebView)findViewById(R.id.webwiew);
		if(_webView == null ) Log.d("onCreate", "_webView es null");
		_webView.getSettings().setJavaScriptEnabled(true);
		_webView.setWebViewClient(new WebViewClient());
		_webView.postUrl("http://usevilla.ingenia.es/login/indexuse.php", EncodingUtils.getBytes(post,"base64"));
	
	}**/
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webwiew);
		String url = getIntent().getStringExtra("url");// get url that pass from
														// the other screen
		_webView = (WebView) findViewById(R.id.webwiew);
		WebSettings webSetting = _webView.getSettings(); // create new settings
														// for webView
		webSetting.setJavaScriptEnabled(true); // enabled javascript
		_webView.setWebViewClient(new WebViewClient()); // set up webviewclient,
														// this set not to open
														// the default browser
														// when link click
		_webView.loadUrl(url);// load the web page
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
		if ((keyCode == KeyEvent.KEYCODE_BACK) && _webView.canGoBack()){
			_webView.goBack();
			return true;
		}  
		return super.onKeyDown(keyCode, event); 
	} 
}
