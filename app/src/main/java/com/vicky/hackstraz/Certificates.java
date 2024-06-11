package com.vicky.hackstraz;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.vicky.hackstraz.R;
import com.vicky.hackstraz.ui.slideshow.Slideshow2ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class Certificates extends Fragment {
    ProgressBar progressBar;
    private CertificateViewModel galleryViewModel;
    private Button btn;
    private TextView txt;
    private ImageView img;
    final String username =  SharedPrefManager.getInstance(getContext()).getUser().getUsername();
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

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
                ViewModelProviders.of(this).get(CertificateViewModel.class);
        View root = inflater.inflate(R.layout.fragment_certificates, container, false);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());

        ImageView img1 = (ImageView)(root.findViewById(R.id.backs1));
        img1.setAlpha(25);



        btn=(root).findViewById(R.id.pdf);
        txt=(root).findViewById(R.id.txt);
        img=(root).findViewById(R.id.img);
        progressBar = root.findViewById(R.id.progressBar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (new CheckForSDCard().isSDCardPresent()) {

                    //Get Download Directory File
                    File apkStorage = new File(
                            Environment.getExternalStorageDirectory() + "/"
                                    + URLs.DIR_DOWNLOAD+"/"+username+".pdf");

                    //If file is not present then display Toast
                    if (!apkStorage.exists())
                        Toast.makeText(getContext(), "Certificate is Not available to view", Toast.LENGTH_SHORT).show();

                    else {

                        //If directory is present Open Folder

                        /** Note: Directory will open only if there is a app to open directory like File Manager, etc.  **/
//                        String storage = Environment.getExternalStorageDirectory().toString() + "/" + URLs.DIR_DOWNLOAD+"/"+username+".pdf";
//                        File file = apkStorage;
                        Uri uri;
                        uri = Uri.fromFile(apkStorage);
//                        if (Build.VERSION.SDK_INT < 24) {
//                            uri = Uri.fromFile(apkStorage);
//                            Toast.makeText(getContext(),"1",Toast.LENGTH_SHORT).show();
//                        } else {
////                            uri = Uri.parse(apkStorage.getPath()); // My work-around for new SDKs, doesn't work in Android 10.
//                            Toast.makeText(getContext(),"12",Toast.LENGTH_SHORT).show();
//
//                        }
//                        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
//                                + "/" + URLs.DIR_DOWNLOAD+"/Certificate.pdf");
//                        Toast.makeText(getContext(),"uri",Toast.LENGTH_SHORT).show();
                        Intent viewFile = new Intent(Intent.ACTION_VIEW);
                        viewFile.setDataAndType(uri, "application/pdf");
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(viewFile);
                    }

                } else
                    Toast.makeText(getContext(), "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

            }
        });

        if (new CheckForSDCard().isSDCardPresent()) {

            //Get Download Directory File
            File apkStorage = new File(
                    Environment.getExternalStorageDirectory() + "/"
                            + URLs.DIR_DOWNLOAD + "/"+username+".pdf");

            //If file is not present then display Toast
            if (!apkStorage.exists())
            {
                if (!isNetworkAvailable()) {
                    Toast.makeText(getActivity(), "Sorry!! I require Internet connection to continue!!", Toast.LENGTH_LONG).show();
                } else {
                    loadData();
                }
        }

            else {

                //If directory is present Open Folder
                txt.setText("Your Certificates are Ready ");
                img.setImageResource(R.drawable.tick);
                btn.setText("Open");
                /** Note: Directory will open only if there is a app to open directory like File Manager, etc.  **/

            }

        } else
            Toast.makeText(getContext(), "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();



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

            final String username =  SharedPrefManager.getInstance(getContext()).getUser().getUsername();

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
                        txt.setText("Your Certificates are Ready ");
                        img.setImageResource(R.drawable.tick);
                        if (!hasPermissions(getActivity(), PERMISSIONS)) {
                            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
                        }
                        if (ActivityCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getActivity(), "Sorry!! I need storage permission to continue", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (hasPermissions(getActivity(), PERMISSIONS)) {
                            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);

                            new DownloadTask(getContext(),btn ,obj.getString("file"),username);
                        }
//                        //getting the user from the response
//                        String dataVal = obj.getString("content");
//
//                        webView.loadData("<html><body>"+dataVal+"</html></body>", "text/html", null);
//
//                        Toast.makeText(getContext(), "Data fetched successfully!!", Toast.LENGTH_SHORT).show();
                    } else {
//                        String summary = "<html><body>Network Problem.</body></html>";
//                        webView.loadData(summary, "text/html", null);
                        txt.setText("Your Certificates are Not yet Ready !!");
                        img.setImageResource(R.drawable.wrong);
                        btn.setText("Try Again Later!!");
//                        Toast.makeText(getContext(), "Network Problem Unable to fetch your data!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
//                    String summary = "<html><body>Internal Error</html>";
//                    webView.loadData(summary, "text/html", null);
                    Toast.makeText(getContext(), "Internal Error!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("username",username);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_FILE, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
//        p1.setText("sam");
//        Toast.makeText(getContext(), p1t, Toast.LENGTH_SHORT).show();
    }
}