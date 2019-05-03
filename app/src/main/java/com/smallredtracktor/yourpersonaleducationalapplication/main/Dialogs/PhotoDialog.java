package com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class PhotoDialog extends Dialog {

    private final int type;
    private final boolean isQuestion;
    private final String content;
    private final Context context;

    public PhotoDialog(Context context, String content, int type, boolean isQuestion) {
        super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        this.context = context;
        this.content = content;
        this.type = type;
        this.isQuestion = isQuestion;
    }

    @Override
    public void show() {
        WebView webView = new WebView(context);
        webView.setBackgroundColor(Color.BLACK);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setScrollbarFadingEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadUrl("file://" + content);
        this.setContentView(webView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        super.show();
    }

}
