package com.vicky.hackstraz;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.vicky.hackstraz.R;
import com.vicky.hackstraz.ui.slideshow.Slideshow2ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AboutUs extends Fragment {
    ProgressBar progressBar;
    private AboutUsViewModel galleryViewModel;
    private WebView webView;
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    //    private Slideshow2ViewModel slideshowViewModel;
    private  ProgressDialog progrDialog;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(AboutUsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_about_us, container, false);
        ImageView img = (ImageView)(root.findViewById(R.id.backs1));
        img.setAlpha(25);



        webView=(root).findViewById(R.id.wv);
        webView.setBackgroundColor(0x00000000);
        progressBar = root.findViewById(R.id.progressBar);
        if(!isNetworkAvailable())
        {
            Toast.makeText(getActivity(), "Sorry!! I require Internet connection to continue!!", Toast.LENGTH_LONG).show();
            String summary = "<html><body>Please<b> Turn on </b>your Internet Connection.</body></html>";
            webView.loadData(summary, "text/html", null);
        }else {
            loadData();
        }
//        final TextView textView = root.findViewById(R.id.text_gallery);
//        galleryViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        if(!isNetworkAvailable())
//        {
//            Toast.makeText(getActivity(), "Sorry!! I require Internet connection to continue!!", Toast.LENGTH_LONG).show();
//        }else {
//            WebView myWebView = (WebView) root.findViewById(R.id.webview);
//            myWebView.loadUrl("https://anjaccoe.org/live");
//            WebSettings webSettings = myWebView.getSettings();
//            webSettings.setJavaScriptEnabled(true);
//            myWebView.setWebViewClient(new WebViewClient() {
//
//                public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                    view.loadUrl(url);
//                    return true;
//                }
//
//                public void onLoadResource(WebView view, String url) {
//                    if (progrDialog == null) {
//                        progrDialog = new ProgressDialog(getContext());
//                        progrDialog.setMessage("Loading...");
//                        progrDialog.show();
//                    }
//                }
//
//                public void onPageFinished(WebView view, String url) {
//                    try {
//                        if (progrDialog.isShowing()) {
//                            progrDialog.dismiss();
//                            progrDialog = null;
//                        }
//                    } catch (Exception exception) {
//                        exception.printStackTrace();
//                    }
//                }
//            });
//        }
        return root;
    }
    private void loadData() {
        //first getting the values

//        final String username =  SharedPrefManager.getInstance(getContext()).getUser().getUsername();


        //validating inputs


        if(!isNetworkAvailable())
        {
            Toast.makeText(getContext(),"Sorry!! I need Internet Connection to continue!",Toast.LENGTH_LONG).show();
            return;
        }
        //if everything is fine
//        final String p1t;

        class UserLogin extends AsyncTask<Void, Void, String> {



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
//                        Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        String dataVal = obj.getString("content");

                        webView.loadData("<html><body>"+dataVal+"</html></body>", "text/html", null);

                        Toast.makeText(getContext(), "Data fetched successfully!!", Toast.LENGTH_SHORT).show();
                    } else {
                        String summary = "<html><body>Network Problem.</body></html>";
                        webView.loadData(summary, "text/html", null);
                        Toast.makeText(getContext(), "Network Problem Unable to fetch your data!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    String summary = "<html><body>Internal Error</html>";
                    webView.loadData(summary, "text/html", null);
                    Toast.makeText(getContext(), "Internal Error!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("username","vals");

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_About, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
//        p1.setText("sam");
//        Toast.makeText(getContext(), p1t, Toast.LENGTH_SHORT).show();
    }
}