package com.example.fawad.bingochat.setting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fawad.bingochat.MainActivity;
import com.example.fawad.bingochat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingActivity extends AppCompatActivity {

    ImageView mAvatar;
    TextView mName;
    TextView mEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        ImageView back = (ImageView)findViewById(R.id.back_arrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        mAvatar=findViewById(R.id.setting_avatar);
        mName=findViewById(R.id.setting_name);
        mEmail=findViewById(R.id.setting_email);
        Glide.with(getApplicationContext()).load(user.getPhotoUrl()).into(mAvatar);
        mName.setText(user.getDisplayName());
        mEmail.setText(user.getEmail());
    }


}
