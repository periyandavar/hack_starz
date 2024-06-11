package com.vicky.hackstraz.ui.tools;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.vicky.hackstraz.Main2Activity;
import com.vicky.hackstraz.Main3Activity;
import com.vicky.hackstraz.MainActivity;
import com.vicky.hackstraz.R;
import com.vicky.hackstraz.RequestHandler;
import com.vicky.hackstraz.SharedPrefManager;
import com.vicky.hackstraz.URLs;
import com.vicky.hackstraz.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Tools1Fragment extends Fragment {
    EditText editTextUsername, editTextPassword;
    private Tools1ViewModel toolsViewModel;
    ProgressBar progressBar;
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(Tools1ViewModel.class);

        View root = inflater.inflate(R.layout.fragment_tools1, container, false);
        progressBar = root.findViewById(R.id.progressBar);
        ImageView img = (ImageView)(root.findViewById(R.id.backs1));
        img.setAlpha(25);



//        final TextView textView = root.findViewById(R.id.text_tools);
//        toolsViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        editTextUsername = (EditText)root.findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) root.findViewById(R.id.editTextPassword);


        //if user presses on login
        //calling the method login
        root.findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        //if user presses on not registered
//        root.findViewById(R.id.textViewRegister).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //open register screen
//                getActivity().finish();
//                startActivity(new Intent(getContext(), MainActivity.class));
//            }
//        });

        return root;
    }
    private void userLogin() {
        //first getting the values
        final String username = editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Please enter your username");
            editTextUsername.requestFocus();
            return;
        }
        else if(!isValidEmail(username))
        {
            editTextUsername.setError("Please Enter valid Email !!");
        }
        else if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter your password");
            editTextPassword.requestFocus();
            return;
        }
        else if(!isNetworkAvailable())
        {
            Toast.makeText(getContext(),"Sorry!! I need Internet Connection to continue!",Toast.LENGTH_LONG).show();
            return;
        }
        //if everything is fine

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
                        Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object
                        User user = new User(

                                userJson.getString("username"),
                                userJson.getString("email")

                        );

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getContext()).userLogin(user);

                        //starting the profile activity
                        getActivity().finish();
                        startActivity(new Intent(getContext(), Main3Activity.class));
                    } else {
                        Toast.makeText(getContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
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
                params.put("username", username);
                params.put("password", password);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
}