package com.vicky.hackstraz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {
    TextView  textViewUsername, textViewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        TextView tw=(TextView)findViewById(R.id.logout);
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            this.finish();
            startActivity(new Intent(this, MainActivity.class));

        }
        ImageView img = (ImageView)(findViewById(R.id.backs1));
        img.setAlpha(25);

        User user = SharedPrefManager.getInstance(this).getUser();
        textViewUsername = (TextView) findViewById(R.id.Pass);
        textViewEmail = (TextView) findViewById(R.id.mail);
        textViewUsername.setText("Team Name/Password : "+user.getUsername());
        textViewEmail.setText("Registered Mail/User Name : "+user.getEmail());
        tw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(getApplicationContext(), Main3Activity.class);
                startActivity(i);
            }
        });
    }
}
