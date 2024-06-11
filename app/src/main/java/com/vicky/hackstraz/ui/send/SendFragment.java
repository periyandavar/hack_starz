package com.vicky.hackstraz.ui.send;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.vicky.hackstraz.R;
import com.vicky.hackstraz.RequestHandler;
import com.vicky.hackstraz.SharedPrefManager;
import com.vicky.hackstraz.URLs;
import com.vicky.hackstraz.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SendFragment extends Fragment {
    private EditText editTextUsername,editTextFeed,editTextmail;
    //    String username,email,feed;
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
    private SendViewModel sendViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sendViewModel =
                ViewModelProviders.of(this).get(SendViewModel.class);
        View root = inflater.inflate(R.layout.fragment_send, container, false);
//        final TextView textView = root.findViewById(R.id.text_send);
//        sendViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        editTextFeed=(root).findViewById(R.id.editTextFeedback);

        progressBar = root.findViewById(R.id.progressBar);

        ImageView img = (ImageView)(root.findViewById(R.id.backs1));
        img.setAlpha(25);



        (root).findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String feed = editTextFeed.getText().toString();

               if (TextUtils.isEmpty(feed)) {
                    editTextFeed.setError("Please enter your Feedback!!");
                    editTextFeed.requestFocus();
                    return;
                }

                else if(!isNetworkAvailable())
                {
                    Toast.makeText(getContext(),"Sorry!! I need Internet Connection to continue!",Toast.LENGTH_LONG).show();
                }
                else{
                    feedback(feed);
                }

            }
        });
        return root;
    }
    public void feedback(final String feed)
    {
        class AddFeedback extends AsyncTask<Void, Void, String> {



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
//                    Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    //if no error in response
                    if (!obj.getBoolean("error")) {
//                        Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        String result = obj.getString("result");
                        Toast.makeText(getContext(),result,Toast.LENGTH_SHORT).show();

                        //creating a new user object
//                        User user = new User(
//
//                                userJson.getString("username"),
//                                userJson.getString("email")
//
//                        );

                        //storing the user in shared preferences
//                        SharedPrefManager.getInstance(getContext()).userLogin(user);

                        //starting the profile activity
//                        getActivity().finish();
//                        startActivity(new Intent(getContext(), Main2Activity.class));
                    } else {
                        Toast.makeText(getContext(), "Sorry, Network Error!!", Toast.LENGTH_SHORT).show();
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
                User user = SharedPrefManager.getInstance(getContext()).getUser();
                params.put("username",user.getUsername());
                params.put("feed",feed);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_FEEDBACK_1, params);
            }
        }
        Toast.makeText(getActivity(),"Please wait!!",Toast.LENGTH_SHORT).show();
        AddFeedback af = new AddFeedback();
        af.execute();

    }
}