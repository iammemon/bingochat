package com.example.fawad.bingochat.post;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fawad.bingochat.R;

/**
 * Created by ebadf on 11/18/2017.
 */


public class PostActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "position";
    private ImageView postImage;
    private TextView title;
    private TextView summary;
    private ImageView avatar;
    private TextView name;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_post);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        postImage=findViewById(R.id.image);
        avatar=findViewById(R.id.post_activity_avatar);
        name=findViewById(R.id.post_activity_name);
        summary=findViewById(R.id.post_activity_summary);
        //getting data from intent
        Intent intent=getIntent();
        name.setText(intent.getStringExtra("NAME"));
        summary.setText(intent.getStringExtra("TEXT"));
        getSupportActionBar().setTitle(intent.getStringExtra("TITLE"));
        Glide.with(this).load(intent.getStringExtra("PIC")).into(postImage);
        Glide.with(this).load(intent.getStringExtra("AVATAR")).into(avatar);


    }
}
