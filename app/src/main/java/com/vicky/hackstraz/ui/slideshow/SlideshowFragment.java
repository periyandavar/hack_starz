package com.vicky.hackstraz.ui.slideshow;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SlideshowFragment extends Fragment {
    ProgressBar progressBar;
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    TextView p1,p2,p3,p4,p5,p6,p7,event,clg,dept,team,mail,mobile;
    private SlideshowViewModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
//        final TextView textView = root.findViewById(R.id.text_slideshow);
//        slideshowViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        ImageView img = (ImageView)(root.findViewById(R.id.backs1));
        img.setAlpha(25);



        progressBar = root.findViewById(R.id.progressBar);
        event=root.findViewById(R.id.event);
        mobile=root.findViewById(R.id.mobile);
        p1=(root).findViewById(R.id.P1);
//        p1.setText("sam");
        p2=root.findViewById(R.id.P2);
        p3=root.findViewById(R.id.P3);
        p4=root.findViewById(R.id.P4);
        p5=root.findViewById(R.id.P5);
        p6=root.findViewById(R.id.P6);
        p7=root.findViewById(R.id.P7);
        mail=root.findViewById(R.id.mail);
        clg=root.findViewById(R.id.clge);
        dept=root.findViewById(R.id.Depart);
        team=root.findViewById(R.id.Pass);

        getData();
        return root;
    }
    private void getData() {
        //first getting the values

        final String username =  SharedPrefManager.getInstance(getContext()).getUser().getUsername();


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
                        JSONObject dataJson = obj.getJSONObject("data");

                        p1.setText("Participant 1 : "+dataJson.getString("p1"));
                        p2.setText("Participant 2 : "+dataJson.getString("p2"));
                        p3.setText("Participant 3 : "+dataJson.getString("p3"));
                        p4.setText("Participant 4 : "+dataJson.getString("p4"));
                        p5.setText("Participant 5 : "+dataJson.getString("p5"));
                        p6.setText("Participant 6 : "+dataJson.getString("p6"));
                        p7.setText("Participant 7 : "+dataJson.getString("p7"));
                        team.setText("TeamName/Password : "+dataJson.getString("team"));
                        clg.setText("College : "+dataJson.getString("clg"));
                        dept.setText("Department : "+dataJson.getString("dept"));
                        mobile.setText("Mobile : "+dataJson.getString("mobile"));
                        mail.setText("Registered Mail Id : "+dataJson.getString("mail"));
                        event.setText("Event : "+dataJson.getString("events"));

                        //creating a new user object
//                        User user = new User(
//
//                                userJson.getString("username"),
//                                userJson.getString("email")
//
//                        );

                        //storing the user in shared preferences
//                        SharedPrefManager.getInstance(getContext()).userLogin(user);
                        Toast.makeText(getContext(), "Data fetched successfully!!", Toast.LENGTH_SHORT).show();
                        //starting the profile activity
//                        getActivity().finish();
//                        startActivity(new Intent(getContext(), Main3Activity.class));
                    } else {
                        Toast.makeText(getContext(), "Network Problem Unable to fetch your data!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
//                    Toast.makeText(getContext(), "Internal Error!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_DETAILS, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
//        p1.setText("sam");
//        Toast.makeText(getContext(), p1t, Toast.LENGTH_SHORT).show();
    }
}