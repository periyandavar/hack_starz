package com.vicky.hackstraz.ui.home;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.vicky.hackstraz.Main2Activity;
import com.vicky.hackstraz.R;
import com.vicky.hackstraz.RequestHandler;
import com.vicky.hackstraz.SharedPrefManager;
import com.vicky.hackstraz.URLs;
import com.vicky.hackstraz.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Home2Fragment extends Fragment {
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    private boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url.toLowerCase());
        return m.matches();
    }
    public final static boolean isValidPhoneNumber(CharSequence target) {
        if (target == null || target.length() < 6 || target.length() > 13) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private Home2ViewModel homeViewModel;
private Button reg;
private Spinner spinner;
    private Spinner spinner1;
private List<String> categories;
    private List<String> categories1;
String clg,dept,mail,link,p1,p2,p3,p4,p5,p6,p7,mbl,Event,entry;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(Home2ViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_register, container, false);
        if (SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), Main2Activity.class));

        }
         spinner = (Spinner) root.findViewById(R.id.spiner);
        spinner1 = (Spinner) root.findViewById(R.id.spiner1);
         categories = new ArrayList<String>();
        categories1 = new ArrayList<String>();
        progressBar = (root.findViewById(R.id.progressBar));
        ImageView img = (ImageView)(root.findViewById(R.id.backs1));
        img.setAlpha(25);
        if(!isNetworkAvailable())
        {
            Toast.makeText(getActivity(), "Sorry!! I require Internet connection to continue!!", Toast.LENGTH_LONG).show();
        }
        else
        {
            getData();
            getData1();
        }
        reg=root.findViewById(R.id.buttonRegister);
        reg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                entry=((EditText)root.findViewById(R.id.entry)).getText().toString().trim();
                 dept=String.valueOf(spinner1.getSelectedItem());
                clg=String.valueOf(spinner.getSelectedItem());
                mail=((EditText)root.findViewById(R.id.mail)).getText().toString().trim();
                 link=((EditText)root.findViewById(R.id.Link)).getText().toString().trim();
                 p1=((EditText)root.findViewById(R.id.p1)).getText().toString().trim();
                 p2=((EditText)root.findViewById(R.id.p2)).getText().toString().trim();
                 p3=((EditText)root.findViewById(R.id.p3)).getText().toString().trim();
                 p4=((EditText)root.findViewById(R.id.p4)).getText().toString().trim();
                 p5=((EditText)root.findViewById(R.id.p5)).getText().toString().trim();
                 p6=((EditText)root.findViewById(R.id.p6)).getText().toString().trim();
                 p7=((EditText)root.findViewById(R.id.p7)).getText().toString().trim();
                 mbl=((EditText)root.findViewById(R.id.Mobile)).getText().toString().trim();
                 Event=(((RadioButton)root.findViewById(((RadioGroup)root.findViewById(R.id.radioGender)).getCheckedRadioButtonId())).getText().toString());
                if(Event.equalsIgnoreCase("HACK-ANJAC (Technical) only"))
                    Event="A";
                else if(Event.equalsIgnoreCase("STARZ-ANJAC (Non-Technical) only"))
                    Event="B";
                else
                    Event="Both";

                if(!(isValidEmail(mail)))
                {
                    Toast.makeText(getActivity(), "Invalid Email", Toast.LENGTH_LONG).show();
                }
                else if(!isNetworkAvailable())
                {
                    Toast.makeText(getActivity(), "Sorry!! I require Internet connection to continue!!", Toast.LENGTH_LONG).show();
                }
                else if(!isValidPhoneNumber(mbl))
                {
                    Toast.makeText(getActivity(), "Invalid Mobile Number", Toast.LENGTH_LONG).show();
                }
                else if(clg.equalsIgnoreCase("")||clg.equalsIgnoreCase(" "))
                {
                    Toast.makeText(getActivity(),"College Name field is required!",Toast.LENGTH_LONG).show();
                }
                else if(dept.equalsIgnoreCase("")||dept.equalsIgnoreCase(" "))
                {
                    Toast.makeText(getActivity(),"Department Name field is required!",Toast.LENGTH_LONG).show();
                }
                else if(p1.equalsIgnoreCase("")||p1.equalsIgnoreCase(" ") )
                {
                    Toast.makeText(getActivity(),"Participant Name 1 field is required!",Toast.LENGTH_LONG).show();
                }
                else if(p2.equalsIgnoreCase("") || p2.equalsIgnoreCase(" "))
                {
                    Toast.makeText(getActivity(),"Participant Name 2 field is required!",Toast.LENGTH_LONG).show();
                }
                else if(p3.equalsIgnoreCase("") || p3.equalsIgnoreCase(" "))
                {
                    Toast.makeText(getActivity(),"Participant Name 3 field is required!",Toast.LENGTH_LONG).show();
                }
                else {
                    if(Event.equalsIgnoreCase("A") || Event.equalsIgnoreCase("Both"))
                        if(isValidUrl(link)) {
                            Toast.makeText(getActivity(), "please wait!!", Toast.LENGTH_SHORT).show();
                            registerUser();
                        }
                    else
                        {
                            Toast.makeText(getActivity(), "Invalid Url", Toast.LENGTH_LONG).show();
                        }
                    else
                    {
                        Toast.makeText(getActivity(), "please wait!!", Toast.LENGTH_SHORT).show();
                        registerUser();
                    }
                }
            }
        });
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
    private ProgressBar progressBar;
    private void registerUser() {
        class RegisterUser extends AsyncTask<Void, Void, String> {



            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("clgname", clg);
                params.put("entry", entry);
                params.put("email", mail);
                params.put("deptname", dept);
                params.put("mobile", mbl);
                params.put("event", Event);
                params.put("link", link);
                params.put("p1", p1);
                params.put("p2", p2);
                params.put("p3", p3);
                params.put("p4", p4);
                params.put("p5", p5);
                params.put("p6", p6);
                params.put("p7", p7);
                //returning the response
                return requestHandler.sendPostRequest(URLs.URL_REGISTER, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server

                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
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
                        startActivity(new Intent(getContext(), Main2Activity.class));
                    } else {
                        Toast.makeText(getContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();
    }
    private void getData() {
        class GetData extends AsyncTask<Void, Void, String> {



            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("p7", "p");
                //returning the response
                return requestHandler.sendPostRequest(URLs.URL_CLG, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server

                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                progressBar.setVisibility(View.GONE);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        String temp;
                        //getting the user from the response
                        JSONArray dataJson = obj.getJSONArray("data");
                        for(int i=0;i<dataJson.length();i++)
                        {
                            temp = dataJson.getJSONObject(i).getString("college");

                            categories.add(temp);
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);

                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // attaching data adapter to spinner
                        spinner.setAdapter(dataAdapter);
                        //creating a new user object

                        //storing the user in shared preferences
//                        SharedPrefManager.getInstance(getContext()).userLogin(user);

//                        //starting the profile activity
//                        getActivity().finish();
//                        startActivity(new Intent(getContext(), Main2Activity.class));
                    } else {
                        Toast.makeText(getContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        GetData gd = new GetData();
        gd.execute();
    }
    private void getData1() {
        class GetData extends AsyncTask<Void, Void, String> {



            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("p7", "p");
                //returning the response
                return requestHandler.sendPostRequest(URLs.URL_DEPT, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server

                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                progressBar.setVisibility(View.GONE);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        String temp;
                        //getting the user from the response
                        JSONArray dataJson = obj.getJSONArray("data");
                        for(int i=0;i<dataJson.length();i++)
                        {
                            temp = dataJson.getJSONObject(i).getString("college");

                            categories1.add(temp);
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories1);

                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // attaching data adapter to spinner
                        spinner1.setAdapter(dataAdapter);
                        //creating a new user object

                        //storing the user in shared preferences
//                        SharedPrefManager.getInstance(getContext()).userLogin(user);

//                        //starting the profile activity
//                        getActivity().finish();
//                        startActivity(new Intent(getContext(), Main2Activity.class));
                    } else {
                        Toast.makeText(getContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        GetData gd = new GetData();
        gd.execute();
    }
}