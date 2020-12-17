package com.github.piasy.mediacodecrctest.chart;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class EchartView extends WebView {

    public EchartView(Context context) {
        this(context, null);
    }

    public EchartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EchartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportZoom(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setAllowFileAccess(true);
        this.setBackgroundColor(Color.parseColor("#ffffff"));
    }

    public void load(String url,String data){
//        Timber.e("haha load url="+url+";data="+data);
        loadUrl(url);
        this.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //最好在h5页面加载完毕后再加载数据，防止html的标签还未加载完成，不能正常显示
//                Timber.e("haha onPageFinished url="+url+";data="+data);
                String call = "javascript:createChart('" + data + "')";
                loadUrl(call); //刷新图表,不能在第一时间就用此方法来显示图表，因为第一时间html的标签还未加载完成，不能获取到标签值
            }
        });
    }
}