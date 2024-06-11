package com.vicky.hackstraz.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.vicky.hackstraz.Main3Activity;
import com.vicky.hackstraz.R;
import com.vicky.hackstraz.RequestHandler;
import com.vicky.hackstraz.SharedPrefManager;
import com.vicky.hackstraz.URLs;
import com.vicky.hackstraz.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {
    ProgressBar progressBar;
    private HomeViewModel homeViewModel;
    private int index=0;
    private ListView list;
    private List<String> categories;
    private LinearLayout lnew;
    private Button btn;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        progressBar = root.findViewById(R.id.progressBar);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        lnew=root.findViewById(R.id.lnew);
        btn=root.findViewById(R.id.btn);
        ImageView img = (ImageView)(root.findViewById(R.id.backs1));
        img.setAlpha(25);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userPost();
            }
        });
        userPost();
        return root;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void userPost() {
        //first getting the values


        //validating inputs
        if(!isNetworkAvailable())
        {
            Toast.makeText(getContext(),"Sorry!! I need Internet Connection to continue!",Toast.LENGTH_LONG).show();
            return;
        }
        //if everything is fine

        class UserPost extends AsyncTask<Void, Void, String> {



            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);


                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        if(obj.getBoolean("cnt"))
                        {
                            String temp;
                            //getting the user from the response
                            JSONArray dataJson = obj.getJSONArray("code");
                            for(int i=0;i<dataJson.length();i++)
                            {
                                temp = dataJson.getJSONObject(i).getString("code");
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                LinearLayout ll = new LinearLayout(getContext());
                                ll.setLayoutParams(params);
                                ll.setOrientation(LinearLayout.VERTICAL);
                                WebView webView = new WebView(getContext());
                                webView.setLayoutParams(params);
////                                webView.getSettings().setJavaScriptEnabled(true);
//                                webView.setWebViewClient(new WebViewClient(){
//                                    @Override
//                                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                                        return false;
//                                    }
//                                });
                                webView.setWebViewClient(new WebViewClient() {
                                    @Override
                                    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                                        handler.proceed(); // Ignore SSL certificate errors
                                    }
                                });
                                webView.setWebChromeClient(new WebChromeClient());
                                WebSettings ws = webView.getSettings();
                                ws.setJavaScriptEnabled(true);
//                                ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//                                ws.setPluginState(WebSettings.PluginState.ON);

//                                webView.setVerticalScrollBarEnabled(false);
//                                webView.setScrollbarFadingEnabled(true);
//                                webView.getSettings().setAllowFileAccess(true);
//                                webView.getSettings().setJavaScriptEnabled(true);
//                                webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//                                webView.getSettings().getPluginState();
//                                webView.getSettings().setLoadWithOverviewMode(true);

//                                webView.getSettings().loadWithOverviewMode = true;
//
//                                ws.setJavaScriptEnabled(true);
//                                if (Build.VERSION.SDK_INT < 8) {
//                                    webView.getSettings().setPluginsEnabled(true);
//                                } else {
//                                    webView.getSettings().setPluginState(WebSettings.PluginState.ON);
//                                }
//                                webView.getSettings().setPluginsEnabled(true);
                                webView.loadData("<html><body>"+temp+"</html></body>", "text/html", null);
                                ll.addView(webView);
                                lnew.addView(ll);
                                if(i==5)
                                {
                                    progressBar.setVisibility(View.GONE);
                                }
//                                categories.add(temp);
                            }
                            index=obj.getInt("limit");
                            btn.setText("Load More Data !!");
//                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), R.layout.display, categories);

                            // Drop down layout style - list view with radio button
//                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            // attaching data adapter to spinner
//                            list.setAdapter(dataAdapter);
                        }
                        else{
                            btn.setText("No More Data Found !!");
                        }
                        //getting the user from the response
//                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object
                        //storing the user in shared preferences

                        //starting the profile activity

                    } else {
                        Toast.makeText(getContext(), "Network Error!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("id", Integer.toString(index));


                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_POST, params);
            }
        }

        UserPost up = new UserPost();
        up.execute();
    }
}