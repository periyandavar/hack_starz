package com.vicky.hackstraz.ui.gallery;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.vicky.hackstraz.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Home1Fragment extends Fragment {

    private HomeViewModel1 galleryViewModel;
    private TextView txtDay, txtHour, txtMinute, txtSecond;
    private TextView tvEventStart;
    private Handler handler;
    private Runnable runnable;
    private LinearLayout lid1,lid2,lid3,lid4;
    TextView lid6,lid5;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(HomeViewModel1.class);
        View root = inflater.inflate(R.layout.fragment_home1, container, false);
        txtDay = (TextView) root.findViewById(R.id.txtDay);
        txtHour = (TextView) root.findViewById(R.id.txtHour);
        txtMinute = (TextView) root.findViewById(R.id.txtMinute);
        txtSecond = (TextView) root.findViewById(R.id.txtSecond);
        View backgroundimage = root.findViewById(R.id.back);
        Drawable background = backgroundimage.getBackground();
        background.setAlpha(200);
        lid6=root.findViewById(R.id.textViewheader2);
        lid5=root.findViewById(R.id.textViewheader1);
        lid4=(LinearLayout)root.findViewById(R.id.LinearLayout4);
        lid3=(LinearLayout)root.findViewById(R.id.LinearLayout3);
        lid2=(LinearLayout)root.findViewById(R.id.LinearLayout2);
        lid1=(LinearLayout)root.findViewById(R.id.LinearLayout1);
        tvEventStart = (TextView) root.findViewById(R.id.tveventStart);

        countDownStart();

//        final TextView textView = root.findViewById(R.id.text_gallery);
//        galleryViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
    public void countDownStart() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd");
                    // Please here set your event date//YYYY-MM-DD
                    Date futureDate = dateFormat.parse("2020-3-13");
                    Date currentDate = new Date();
                    if (!currentDate.after(futureDate)) {
                        long diff = futureDate.getTime()
                                - currentDate.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;
                        txtDay.setText("" + String.format("%02d", days));
                        txtHour.setText("" + String.format("%02d", hours));
                        txtMinute.setText(""
                                + String.format("%02d", minutes));
                        txtSecond.setText(""
                                + String.format("%02d", seconds));
                    } else {
                        tvEventStart.setVisibility(View.VISIBLE);
                        tvEventStart.setText("The event started!");
                        textViewGone();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1 * 1000);
    }

    public void textViewGone() {

        lid1.setVisibility(View.GONE);
       lid2.setVisibility(View.GONE);
        lid3.setVisibility(View.GONE);
        lid4.setVisibility(View.GONE);
        lid5.setVisibility(View.GONE);
        lid6.setVisibility(View.GONE);
    }
}