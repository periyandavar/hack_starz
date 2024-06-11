package com.vicky.hackstraz.ui.slideshow;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.vicky.hackstraz.R;
import com.vicky.hackstraz.URLs;

public class Slideshow2Fragment extends Fragment {
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private Slideshow2ViewModel slideshowViewModel;
 private  ProgressDialog progrDialog;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(Slideshow2ViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home2, container, false);
//        final TextView textView = root.findViewById(R.id.text_slideshow);
//        slideshowViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        if(!isNetworkAvailable())
        {
            Toast.makeText(getActivity(), "Sorry!! I require Internet connection to continue!!", Toast.LENGTH_LONG).show();
        }else {
            WebView myWebView = (WebView) root.findViewById(R.id.webview);
            myWebView.loadUrl(URLs.URL_LIVE);

            myWebView.setWebViewClient(new WebViewClient() {

                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                public void onLoadResource(WebView view, String url) {
                    if (progrDialog == null) {
                        progrDialog = new ProgressDialog(getContext());
                        progrDialog.setMessage("Loading...");
                        progrDialog.show();
                    }
                }

                public void onPageFinished(WebView view, String url) {
                    try {
                        if (progrDialog.isShowing()) {
                            progrDialog.dismiss();
                            progrDialog = null;
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            });
            WebSettings webSettings = myWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
        }
        return root;
    }
}